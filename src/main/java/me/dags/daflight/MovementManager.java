package me.dags.daflight;

import me.dags.daflight.util.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInputFromOptions;

public class MovementManager {

    public MovementInput input;

    private EntityPlayer player;
    private PlayerStatus status;
    
    private float maxFlySpeed = 1F;
    private float maxWalkSpeed = 1F;
    
    private static final double SCALE_FACTOR = 1 / Math.sqrt(2);
    
    public MovementManager(Minecraft mc, EntityPlayer player, PlayerStatus status) {
        input = new MovementInput(mc);
        this.player = player;
        this.status = status;
    }

    public Tuple3d moveFlying()
    {
        Config config = DaFlight.INSTANCE.config;

        double x = 0;
        double y = 0;
        double z = 0;
        double rads = Math.toRadians(player.rotationYaw);
        double dx = -Math.sin(rads);
        double dz = Math.cos(rads);
        float boost = status.flyBoosting ? config.flyBoost : 1F;

        if (input.moveForward != 0)
        {
            x += dx * input.moveForward * clamp(config.flySpeed * boost, maxFlySpeed);
            z += dz * input.moveForward * clamp(config.flySpeed * boost, maxFlySpeed);
        }
        if (input.moveStrafe != 0)
        {
            x += dz * input.moveStrafe * clamp(config.flySpeed * config.strafeModifier * boost, maxFlySpeed);
            z += dx * -input.moveStrafe * clamp(config.flySpeed * config.strafeModifier * boost, maxFlySpeed);
        }
        if (input.moveForward != 0 && input.moveStrafe != 0)
        {
            x *= SCALE_FACTOR;
            z *= SCALE_FACTOR;
        }
        if (config.flight3D && input.moveForward != 0)
        {
            y += -player.rotationPitch * input.moveForward * (0.9F / 50F) * config.verticalModifier * clamp(config.flySpeed * boost, maxFlySpeed);
        }
        if (DaFlight.INSTANCE.flyUpBind.keyHeld() && Minecraft.getMinecraft().inGameHasFocus)
        {
            y += clamp(config.flySpeed * boost * config.verticalModifier, maxFlySpeed);
        }
        if (DaFlight.INSTANCE.flyDownBind.keyHeld() && Minecraft.getMinecraft().inGameHasFocus)
        {
            y -= clamp(config.flySpeed * boost * config.verticalModifier, maxFlySpeed);
        }
        return new Tuple3d(x, y, z);
    }

    public Tuple3d moveSprinting(double x, double y, double z)
    {
        Config config = DaFlight.INSTANCE.config;

        double rads = Math.toRadians(player.rotationYaw);
        double dx = -Math.sin(rads);
        double dz = Math.cos(rads);
        float boost = status.sprintBoosting ? config.sprintBoost : 1F;

        if (input.moveForward != 0)
        {
            x += dx * input.moveForward * clamp(config.sprintSpeed * boost, maxWalkSpeed);
            z += dz * input.moveForward * clamp(config.sprintSpeed * boost, maxWalkSpeed);
        }
        if (input.moveStrafe != 0)
        {
            x += dz * input.moveStrafe * clamp(config.sprintSpeed * config.strafeModifier * boost, maxWalkSpeed);
            z += dx * -input.moveStrafe * clamp(config.sprintSpeed * config.strafeModifier * boost, maxWalkSpeed);
        }
        if (input.moveForward != 0 && input.moveStrafe != 0)
        {
            x *= SCALE_FACTOR;
            z *= SCALE_FACTOR;
        }
        return new Tuple3d(x, y, z);
    }

    public class MovementInput extends MovementInputFromOptions
    {
        public MovementInput(Minecraft mc)
        {
            super(mc.gameSettings);
        }

        @Override
        public void updatePlayerMoveState()
        {
            PlayerStatus status = DaFlight.INSTANCE.status;
            super.updatePlayerMoveState();
            if ((status.flying || status.sprinting) && super.sneak)
            {
                super.moveForward /= 0.3D;
                super.moveStrafe /= 0.3D;
            }
        }
    }
    
    public static class Tuple3d
    {
        public double x;
        public double y;
        public double z;

        public Tuple3d(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
    }
    
    public static float clamp(float in, float max)
    {
        return in > max ? max : in;
    }
}
