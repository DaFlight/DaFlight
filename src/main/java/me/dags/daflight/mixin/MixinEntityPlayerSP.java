package me.dags.daflight.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import io.netty.buffer.Unpooled;
import me.dags.daflight.DaFlight;
import me.dags.daflight.MovementManager;
import me.dags.daflight.PlayerStatus;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.Tuple3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow
    @Final
    private NetHandlerPlayClient sendQueue;
    @Shadow
    private MovementInput movementInput;

    private final PlayerStatus status = DaFlight.INSTANCE.status;
    private final MovementManager movement = new MovementManager(Minecraft.getMinecraft(), this, status);

    private final float defaultFlySpeed = 0.5F;
    private final float defaultWalkSpeed = 0.1F;
    private float serverFlySpeed;
    private float serverWalkSpeed;

    @SuppressWarnings("unused")
    private float maxFlySpeed = 1F;
    private float maxWalkSpeed = 1F;
    private boolean wasFlying = false;
    private boolean wasSprinting = false;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "onUpdate()V", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        if (movementInput != movement.input) {
            movementInput = movement.input;
        }

        this.checkSpeeds();

        if (status.flying && !super.capabilities.isFlying) {
            super.capabilities.isFlying = true;
            super.sendPlayerAbilities();
        }

    }

    @Override
    public void preparePlayerToSpawn() {
        super.preparePlayerToSpawn();
        sendQueue.addToSendQueue(new C17PacketCustomPayload(DaFlight.CHANNEL_CONNECT, new PacketBuffer(Unpooled.wrappedBuffer(new byte[0]))));
    }

    @Override
    protected float getJumpUpwardsMotion() {
        Config config = DaFlight.INSTANCE.config;
        if (status.sprinting && !DaFlight.INSTANCE.config.disabled) {
            float boost = status.sprintBoosting ? config.sprintBoost : 1F;
            return super.getJumpUpwardsMotion() * MovementManager.clamp(config.sprintSpeed * config.jumpModifier * boost, maxWalkSpeed * 5);
        }
        return super.getJumpUpwardsMotion();
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        if (status.flying && !DaFlight.INSTANCE.config.disabled) {
            flyToggleTimer = 0;
            Tuple3d vec = movement.moveFlying();
            super.moveEntity(vec.x, vec.y, vec.z);
        } else if (status.sprinting && !DaFlight.INSTANCE.config.disabled) {
            Tuple3d vec = movement.moveSprinting(x, y, z);
            super.moveEntity(vec.x, vec.y, vec.z);
        } else {
            super.moveEntity(x, y, z);
        }
    }

    @Override
    public float getFovModifier() {
        if (DaFlight.INSTANCE.config.disableFov) {
            if (status.sprinting || status.flying) {
                return 1.0F;
            }
            if (wasFlying) {
                wasFlying = false;
                return 1.0F;
            }
            if (wasSprinting) {
                wasSprinting = false;
                return 1.0F;
            }
        }
        return super.getFovModifier();
    }

    private void checkSpeeds() {
        if (!Minecraft.getMinecraft().isSingleplayer()) {
            if (serverFlySpeed != capabilities.getFlySpeed()) {
                serverFlySpeed = capabilities.getFlySpeed();
                maxFlySpeed = serverFlySpeed > 0.0001F ? capabilities.getFlySpeed() * 10F / defaultFlySpeed : 0F;
                wasFlying = true;
            }
            if (serverWalkSpeed != capabilities.getWalkSpeed()) {
                serverWalkSpeed = capabilities.getWalkSpeed();
                maxWalkSpeed = serverWalkSpeed > 0.0001F ? capabilities.getWalkSpeed() * 10F / defaultWalkSpeed : 0F;
                wasSprinting = true;
            }
        } else {
            maxFlySpeed = 100F;
            maxWalkSpeed = 100F;
        }
    }

}
