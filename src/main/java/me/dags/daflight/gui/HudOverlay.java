package me.dags.daflight.gui;

import me.dags.daflight.PlayerStatus;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

/**
 * @author dags_ <dags@dags.me>
 */

public class HudOverlay extends Gui
{
    private final FontRenderer fontRenderer;
    private final PlayerStatus player;
    private final ConfigGlobal configGlobal;

    public HudOverlay(FontRenderer fr, PlayerStatus entityFlying, ConfigGlobal manager)
    {
        fontRenderer = fr;
        player = entityFlying;
        configGlobal = manager;
    }

    public void renderGameOverlay(float partialTicks)
    {
        Config config = configGlobal.activeConfig;
        if (config.hud && Minecraft.getMinecraft().inGameHasFocus && !Minecraft.getMinecraft().gameSettings.showDebugInfo)
        {
            int x = 5;
            int y = 5;
            if (player.flying)
            {
                String fly = player.flyBoosting ? configGlobal.flyDisplay + configGlobal.boostDisplay : configGlobal.flyDisplay;
                fontRenderer.drawString(fly, x, y, 0xFFFFFF);
                y += 9;
            }
            if (player.sprinting)
            {
                String sprint = player.sprintBoosting ? configGlobal.sprintDisplay + configGlobal.boostDisplay : configGlobal.sprintDisplay;
                fontRenderer.drawString(sprint, x, y, 0xFFFFFF);
            }
        }
    }
}
