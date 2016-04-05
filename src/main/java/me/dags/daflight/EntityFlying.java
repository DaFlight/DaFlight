package me.dags.daflight;

import io.netty.buffer.Unpooled;
import me.dags.daflight.gui.ConfigScreen;
import me.dags.daflight.gui.HudOverlay;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.world.World;

/**
 * @author dags_ <dags@dags.me>
 */

public class EntityFlying extends EntityPlayerSP
{
    private static final double SCALE_FACTOR = 1 / Math.sqrt(2);
    private static final String CHANNEL_FLY = "DAFLIGHT-FLY";
    private static final String CHANNEL_SPRINT = "DAFLIGHT-SPRINT";
    private static final String CHANNEL_CONNECT = "DAFLIGHT-CONNECT";

    private final ConfigGlobal configGlobal;
    private final EntityFlying.MovementInput input;

    public Config config;

    private Bind menu;
    private Bind flyBind;
    private Bind sprintBind;
    private Bind boostBind;
    private Bind flyUpBind;
    private Bind flyDownBind;

    public boolean flying = false;
    public boolean sprinting = false;
    public boolean flyBoosting = false;
    public boolean sprintBoosting = false;

    private final float defaultFlySpeed;
    private final float defaultWalkSpeed;
    private float serverFlySpeed;
    private float serverWalkSpeed;

    private float maxFlySpeed;
    private float maxWalkSpeed;
    private boolean wasFlying = false;
    private boolean wasSprinting = false;

    public EntityFlying(Minecraft mc, World w, NetHandlerPlayClient n, StatFileWriter sf)
    {
        super(mc, w, n, sf);
        configGlobal = ConfigGlobal.getOrCreate(mc.mcDataDir);

        input = new MovementInput(mc);
        defaultFlySpeed = capabilities.getFlySpeed();
        defaultWalkSpeed = capabilities.getWalkSpeed();
        serverFlySpeed = defaultFlySpeed;
        serverWalkSpeed = defaultWalkSpeed;
        maxFlySpeed = 1F;
        maxWalkSpeed = 1F;
        mc.ingameGUI = new HudOverlay(Minecraft.getMinecraft(), this, configGlobal);

        updateConfig();
    }

    @Override
    public void preparePlayerToSpawn()
    {
        super.preparePlayerToSpawn();
        super.sendQueue.addToSendQueue(new C17PacketCustomPayload(CHANNEL_CONNECT, new PacketBuffer(Unpooled.wrappedBuffer(new byte[0]))));
    }

    @Override
    public void onUpdate()
    {
        if (super.movementInput != input)
        {
            super.movementInput = input;
        }

        this.handleInput();
        this.checkSpeeds();

        if (flying && !super.capabilities.isFlying)
        {
            super.capabilities.isFlying = true;
            super.sendPlayerAbilities();
        }

        super.onUpdate();
    }

    @Override
    protected float getJumpUpwardsMotion()
    {
        if (sprinting && !config.disabled)
        {
            float boost = sprintBoosting ? config.sprintBoost : 1F;
            return super.getJumpUpwardsMotion() * clamp(config.sprintSpeed * config.jumpModifier * boost, maxWalkSpeed * 5);
        }
        return super.getJumpUpwardsMotion();
    }

    @Override
    public void moveEntity(double x, double y, double z)
    {
        if (flying && !config.disabled)
        {
            moveFlying();
        }
        else if (sprinting && !config.disabled)
        {
            moveSprinting(x, y, z);
        }
        else
        {
            super.moveEntity(x, y, z);
        }
    }

    @Override
    public float getFovModifier()
    {
        if (config.disableFov)
        {
            if (sprinting || flying)
            {
                return 1.0F;
            }
            if (wasFlying)
            {
                wasFlying = false;
                return 1.0F;
            }
            if (wasSprinting)
            {
                wasSprinting = false;
                return 1.0F;
            }
        }
        return super.getFovModifier();
    }

    public void updateConfig()
    {
        config = configGlobal.activeConfig;
        menu = Bind.from("menu", config.menu, false);
        flyBind = Bind.from("fly", config.fly, config.flyToggle);
        sprintBind = Bind.from("sprint", config.sprint, config.sprintToggle);
        boostBind = Bind.from("boost", config.boost, config.boostToggle);
        flyUpBind = Bind.from("up", config.up, true);
        flyDownBind = Bind.from("down", config.down, true);
    }

    private void checkSpeeds()
    {
        if (!Minecraft.getMinecraft().isSingleplayer())
        {
            if (serverFlySpeed != capabilities.getFlySpeed())
            {
                serverFlySpeed = capabilities.getFlySpeed();
                maxFlySpeed = serverFlySpeed > 0.0001F ? capabilities.getFlySpeed() * 10F / defaultFlySpeed : 0F;
                wasFlying = true;
            }
            if (serverWalkSpeed != capabilities.getWalkSpeed())
            {
                serverWalkSpeed = capabilities.getWalkSpeed();
                maxWalkSpeed = serverWalkSpeed > 0.0001F ? capabilities.getWalkSpeed() * 10F / defaultWalkSpeed : 0F;
                wasSprinting = true;
            }
        }
        else
        {
            maxFlySpeed = 100F;
            maxWalkSpeed = 100F;
        }
    }

    private void handleInput()
    {
        if (!Minecraft.getMinecraft().inGameHasFocus)
        {
            return;
        }

        boolean wasFlying = flying;
        boolean wasSprinting = sprinting;

        if (menu.keyPress())
        {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen(configGlobal));
        }

        if (flyBind.isToggle())
        {
            flying = flyBind.keyPress() ? !flying && super.capabilities.allowFlying : flying;
        }
        else
        {
            flying = flyBind.keyHeld() && super.capabilities.allowFlying;
        }

        if (sprintBind.isToggle())
        {
            sprinting = sprintBind.keyPress() ? !sprinting && super.capabilities.allowFlying : sprinting;
        }
        else
        {
            sprinting = sprintBind.keyHeld() && super.capabilities.allowFlying;
        }

        if (boostBind.isToggle())
        {
            if (boostBind.keyPress())
            {
                flyBoosting = flying ? !flyBoosting : flyBoosting;
                sprintBoosting = !flying && sprinting ? !sprintBoosting : sprintBoosting;
            }
        }
        else
        {
            flyBoosting = flying ? boostBind.keyHeld() : flyBoosting;
            sprintBoosting = !flying && sprinting ? boostBind.keyHeld() : sprintBoosting;
        }

        if (config.disabled || !super.capabilities.allowFlying)
        {
            boolean updated = flying || sprinting;
            flying = false;
            sprinting = false;
            flyBoosting = false;
            sprintBoosting = false;
            if (updated)
            {
                super.sendPlayerAbilities();
            }
        }

        if (wasFlying != flying)
        {
            super.capabilities.isFlying = flying;
            super.sendPlayerAbilities();
            byte val = flying ? (byte) 1 : (byte) 0;
            super.sendQueue.addToSendQueue(new C17PacketCustomPayload(CHANNEL_FLY, new PacketBuffer(Unpooled.wrappedBuffer(new byte[]{val}))));
        }
        if (wasSprinting != sprinting)
        {
            byte val = sprinting ? (byte) 1 : (byte) 0;
            super.sendQueue.addToSendQueue(new C17PacketCustomPayload(CHANNEL_SPRINT, new PacketBuffer(Unpooled.wrappedBuffer(new byte[]{val}))));
        }
    }

    private void moveFlying()
    {
        super.flyToggleTimer = 0;
        double x = 0;
        double y = 0;
        double z = 0;
        double rads = Math.toRadians(super.rotationYaw);
        double dx = -Math.sin(rads);
        double dz = Math.cos(rads);
        float boost = flyBoosting ? config.flyBoost : 1F;

        if (movementInput.moveForward != 0)
        {
            x += dx * movementInput.moveForward * clamp(config.flySpeed * boost, maxFlySpeed);
            z += dz * movementInput.moveForward * clamp(config.flySpeed * boost, maxFlySpeed);
        }
        if (movementInput.moveStrafe != 0)
        {
            x += dz * movementInput.moveStrafe * clamp(config.flySpeed * config.strafeModifier * boost, maxFlySpeed);
            z += dx * -movementInput.moveStrafe * clamp(config.flySpeed * config.strafeModifier * boost, maxFlySpeed);
        }
        if (movementInput.moveForward != 0 && movementInput.moveStrafe != 0)
        {
            x *= SCALE_FACTOR;
            z *= SCALE_FACTOR;
        }
        if (config.flight3D && movementInput.moveForward != 0)
        {
            y += -super.rotationPitch * movementInput.moveForward * (0.9F / 50F) * config.verticalModifier * clamp(config.flySpeed * boost, maxFlySpeed);
        }
        if (flyUpBind.keyHeld() && Minecraft.getMinecraft().inGameHasFocus)
        {
            y += clamp(config.flySpeed * boost * config.verticalModifier, maxFlySpeed);
        }
        if (flyDownBind.keyHeld() && Minecraft.getMinecraft().inGameHasFocus)
        {
            y -= clamp(config.flySpeed * boost * config.verticalModifier, maxFlySpeed);
        }

        super.moveEntity(x, y, z);
    }

    private void moveSprinting(double x, double y, double z)
    {
        double rads = Math.toRadians(super.rotationYaw);
        double dx = -Math.sin(rads);
        double dz = Math.cos(rads);
        float boost = sprintBoosting ? config.sprintBoost : 1F;

        if (movementInput.moveForward != 0)
        {
            x += dx * movementInput.moveForward * clamp(config.sprintSpeed * boost, maxWalkSpeed);
            z += dz * movementInput.moveForward * clamp(config.sprintSpeed * boost, maxWalkSpeed);
        }
        if (movementInput.moveStrafe != 0)
        {
            x += dz * movementInput.moveStrafe * clamp(config.sprintSpeed * config.strafeModifier * boost, maxWalkSpeed);
            z += dx * -movementInput.moveStrafe * clamp(config.sprintSpeed * config.strafeModifier * boost, maxWalkSpeed);
        }
        if (movementInput.moveForward != 0 && movementInput.moveStrafe != 0)
        {
            x *= SCALE_FACTOR;
            z *= SCALE_FACTOR;
        }

        super.moveEntity(x, y, z);
    }

    private class MovementInput extends MovementInputFromOptions
    {
        public MovementInput(Minecraft mc)
        {
            super(mc.gameSettings);
        }

        @Override
        public void updatePlayerMoveState()
        {
            super.updatePlayerMoveState();
            if ((flying || sprinting) && super.sneak)
            {
                super.moveForward /= 0.3D;
                super.moveStrafe /= 0.3D;
            }
        }
    }

    private static float clamp(float in, float max)
    {
        return in < max ? in : max;
    }
}
