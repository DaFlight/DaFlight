package me.dags.daflight.mixin;

import com.mojang.authlib.GameProfile;
import me.dags.daflight.DaFlight;
import me.dags.daflight.util.Rotation;
import me.dags.daflight.util.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author dags_ <dags@dags.me>
 */

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    @Shadow
    public MovementInput movementInput;

    private final Vector3d direction = new Vector3d();
    private final Rotation rotation = new Rotation();

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile)
    {
        super(worldIn, playerProfile);
        DaFlight.init(Minecraft.getMinecraft().mcDataDir);
    }

    @Inject(method = "onUpdate()V", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci)
    {
        DaFlight.instance().tick(true, Minecraft.getMinecraft().inGameHasFocus);
    }

    @Override
    public void moveEntity(double x, double y, double z)
    {
        direction.update(x, y, z);
        rotation.update(rotationPitch, rotationYaw);
        DaFlight.instance().movementHandler().setMovement(movementInput.moveForward, movementInput.moveStrafe);
        DaFlight.instance().movementHandler().applyMovement(direction, rotation);
        super.moveEntity(direction.getX(), direction.getY(), direction.getZ());
    }

    @Override
    public float getFovModifier()
    {
        if (DaFlight.instance().movementHandler().disableFov())
        {
            return 1.0F;
        }
        return super.getFovModifier();
    }

    @Override
    public void preparePlayerToSpawn()
    {
        super.preparePlayerToSpawn();
        DaFlight.instance().setSinglePlayer(Minecraft.getMinecraft().isSingleplayer());
        DaFlight.instance().setServerName(serverName());
        DaFlight.instance().updateConfig();
        DaFlight.instance().movementHandler().reset();
        DaFlight.instance().messageHandler().registerChannels();
        DaFlight.instance().messageHandler().connect();
    }

    @Override
    protected float getJumpUpwardsMotion()
    {
        return DaFlight.instance().movementHandler().jump(super.getJumpUpwardsMotion());
    }

    private static String serverName()
    {
        if (!DaFlight.instance().isSinglePlayer())
        {
            return Minecraft.getMinecraft().getCurrentServerData().serverIP.replace(":", "-").replace("-25565", "");
        }
        return "";
    }
}
