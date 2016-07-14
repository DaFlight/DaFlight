package me.dags.daflight.mixin;

import com.mojang.authlib.GameProfile;
import me.dags.daflight.DaFlight;
import me.dags.daflight.util.Rotation;
import me.dags.daflight.util.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author dags_ <dags@dags.me>
 */

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    private final Vector3d direction = new Vector3d();
    private final Rotation rotation = new Rotation();

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile)
    {
        super(worldIn, playerProfile);
    }

    // Can't seem to shadow this when forge is in play :/
    // @Shadow
    // void func_189810_i(float x, float z){}

    @Override
    public void moveEntity(double x, double y, double z)
    {
        Minecraft.getMinecraft().mcProfiler.startSection("daflightMove");
        updateFlyStatus();
        direction.update(x, y, z);
        rotation.update(rotationPitch, rotationYaw);

        EntityPlayerSP entityPlayerSP = (EntityPlayerSP) (Object) this;
        MovementInput movementInput = entityPlayerSP.movementInput;
        DaFlight.instance().movementHandler().setMovementInput(movementInput.moveForward, movementInput.moveStrafe);
        DaFlight.instance().movementHandler().applyMovement(direction, rotation);
        Minecraft.getMinecraft().mcProfiler.endSection();

        // replicate EntityPlayerSP.moveEntity(..), but use our x,y,z
        double posX = entityPlayerSP.posX;
        double posZ = entityPlayerSP.posZ;
        super.moveEntity(direction.getX(), direction.getY(), direction.getZ());

        // Relies on above broken shadow method - not sure what it does but looks like it could be important
        // this.func_189810_i((float)(entityPlayerSP.posX - posX), (float)(entityPlayerSP.posZ - posZ));
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

    private void updateFlyStatus()
    {
        if (!capabilities.isFlying && DaFlight.instance().movementHandler().flying())
        {
            capabilities.isFlying = true;
            sendPlayerAbilities();
        }
    }

    private static String serverName()
    {
        if (!DaFlight.instance().isSinglePlayer())
        {
            ServerData currentServer = Minecraft.getMinecraft().getCurrentServerData();
            return currentServer != null ? currentServer.serverIP.replace(":", "-").replace("-25565", "") : "";
        }
        return "";
    }
}
