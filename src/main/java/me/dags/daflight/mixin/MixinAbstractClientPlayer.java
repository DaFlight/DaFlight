package me.dags.daflight.mixin;

import com.mojang.authlib.GameProfile;
import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import me.dags.daflight.util.Rotation;
import me.dags.daflight.util.Vector3d;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author dags_ <dags@dags.me>
 */

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayer extends PlayerEntity {

    private final Vector3d heading = new Vector3d();
    private final Rotation rotation = new Rotation();

    public MixinAbstractClientPlayer(World worldIn) {
        super(worldIn, new GameProfile(null, null));
    }

    // Inserts between EntityPlayerSP.moveEntity() and EntityPlayer.moveEntity(), passing the modified x,y,z to EntityPlayer
    @Override
    public void move(MoverType type, Vec3d vec) {
        // Only modify if this Player is the client
        if (MCHooks.Player.isClientPlayer(this)) {
            MCHooks.Profiler.startSection("daflightMove");
            updateFlyStatus();
            heading.set(vec.x, vec.y, vec.z);
            rotation.set(rotationPitch, rotationYaw);
            DaFlight.instance().movementHandler().setMovementInput(MCHooks.Player.Input.forward(), MCHooks.Player.Input.strafe());
            DaFlight.instance().movementHandler().applyMovement(heading, rotation);
            MCHooks.Profiler.endSection();
            super.move(type, new Vec3d(heading.getX(), heading.getY(), heading.getZ()));
        } else {
            super.move(type, vec);
        }
    }

    private void updateFlyStatus() {
        if (!MCHooks.Player.isFlying() && DaFlight.instance().movementHandler().flying()) {
            MCHooks.Player.setFlying(true);
            MCHooks.Player.sendPlayerAbilities();
        }
    }
}
