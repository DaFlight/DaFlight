package me.dags.daflight;

import io.netty.buffer.Unpooled;
import me.dags.daflight.gui.ConfigScreen;
import me.dags.daflight.gui.HudOverlay;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class DaFlight {

    public static final DaFlight INSTANCE = new DaFlight();

    public static final double SCALE_FACTOR = 1 / Math.sqrt(2);
    public static final String CHANNEL_FLY = "DAFLIGHT-FLY";
    public static final String CHANNEL_SPRINT = "DAFLIGHT-SPRINT";
    public static final String CHANNEL_CONNECT = "DAFLIGHT-CONNECT";

    private final Minecraft mc;
    private final ConfigGlobal configGlobal;

    public final PlayerStatus status;
    public final HudOverlay hudOverlay;
    
    public Config config;

    public Bind menu;
    public Bind flyBind;
    public Bind sprintBind;
    public Bind boostBind;
    public Bind flyUpBind;
    public Bind flyDownBind;

    
    private DaFlight() {
        mc = Minecraft.getMinecraft();
        configGlobal = ConfigGlobal.getOrCreate(mc.mcDataDir);
        updateConfig();
        status = new PlayerStatus();
        hudOverlay = new HudOverlay(Minecraft.getMinecraft().fontRendererObj, status, configGlobal);
    }

    public void updateConfig() {
        config = configGlobal.activeConfig;
        menu = Bind.from("menu", config.menu, false);
        flyBind = Bind.from("fly", config.fly, config.flyToggle);
        sprintBind = Bind.from("sprint", config.sprint, config.sprintToggle);
        boostBind = Bind.from("boost", config.boost, config.boostToggle);
        flyUpBind = Bind.from("up", config.up, true);
        flyDownBind = Bind.from("down", config.down, true);
    }

    public void handleInput()
    {
        if (!Minecraft.getMinecraft().inGameHasFocus)
        {
            return;
        }

        boolean wasFlying = status.flying;
        boolean wasSprinting = status.sprinting;

        if (menu.keyPress())
        {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen(configGlobal));
        }

        if (flyBind.isToggle())
        {
            status.flying = flyBind.keyPress() ? !status.flying && mc.thePlayer.capabilities.allowFlying : status.flying;
        }
        else
        {
            status.flying = flyBind.keyHeld() && mc.thePlayer.capabilities.allowFlying;
        }

        if (sprintBind.isToggle())
        {
            status.sprinting = sprintBind.keyPress() ? !status.sprinting && mc.thePlayer.capabilities.allowFlying : status.sprinting;
        }
        else
        {
            status.sprinting = sprintBind.keyHeld() && mc.thePlayer.capabilities.allowFlying;
        }

        if (boostBind.isToggle())
        {
            if (boostBind.keyPress())
            {
                status.flyBoosting = status.flying ? !status.flyBoosting : status.flyBoosting;
                status.sprintBoosting = !status.flying && status.sprinting ? !status.sprintBoosting : status.sprintBoosting;
            }
        }
        else
        {
            status.flyBoosting = status.flying ? boostBind.keyHeld() : status.flyBoosting;
            status.sprintBoosting = !status.flying && status.sprinting ? boostBind.keyHeld() : status.sprintBoosting;
        }

        if (config.disabled || !mc.thePlayer.capabilities.allowFlying)
        {
            boolean updated = status.flying || status.sprinting;
            status.flying = false;
            status.sprinting = false;
            status.flyBoosting = false;
            status.sprintBoosting = false;
            if (updated)
            {
                mc.thePlayer.sendPlayerAbilities();
            }
        }

        if (wasFlying != status.flying)
        {
            mc.thePlayer.capabilities.isFlying = status.flying;
            mc.thePlayer.sendPlayerAbilities();
            byte val = status.flying ? (byte) 1 : (byte) 0;
            mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload(CHANNEL_FLY, new PacketBuffer(Unpooled.wrappedBuffer(new byte[]{val}))));
        }
        if (wasSprinting != status.sprinting)
        {
            byte val = status.sprinting ? (byte) 1 : (byte) 0;
            mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload(CHANNEL_SPRINT, new PacketBuffer(Unpooled.wrappedBuffer(new byte[]{val}))));
        }
    }

}
