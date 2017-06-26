package me.dags.daflight.mixin;

import com.mojang.authlib.GameProfile;
import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import me.dags.daflight.util.Rotation;
import me.dags.daflight.util.Vector3d;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author dags_ <dags@dags.me>
 */

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer {

    private final Vector3d direction = new Vector3d();
    private final Rotation rotation = new Rotation();

    public MixinAbstractClientPlayer(World worldIn) {
        super(worldIn, new GameProfile(null, null));
    }

    // Inserts between EntityPlayerSP.moveEntity() and EntityPlayer.moveEntity(), passing the modified x,y,z to EntityPlayer
    @Override
    public void move(MoverType type, double x, double y, double z) {
        // Only modify if this Player is the client
        if (MCHooks.Player.isClientPlayer(this)) {
            MCHooks.Profiler.startSection("daflightMove");
            updateFlyStatus();
            direction.update(x, y, z);
            rotation.update(rotationPitch, rotationYaw);
            DaFlight.instance().movementHandler().setMovementInput(MCHooks.Player.Input.forward(), MCHooks.Player.Input.strafe());
            DaFlight.instance().movementHandler().applyMovement(direction, rotation);
            MCHooks.Profiler.endSection();
            super.move(type, direction.getX(), direction.getY(), direction.getZ());
        } else {
            super.move(type, x, y, z);
        }
    }

    private void updateFlyStatus() {
        if (!MCHooks.Player.isFlying() && DaFlight.instance().movementHandler().flying()) {
            MCHooks.Player.setFlying(true);
            MCHooks.Player.sendPlayerAbilities();
        }
    }
}
