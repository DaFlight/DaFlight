package me.dags.daflight.gui;

import me.dags.daflight.EntityFlying;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

/**
 * @author dags_ <dags@dags.me>
 */

public class HudOverlay extends GuiIngame
{
    private final EntityFlying player;
    private final ConfigGlobal configGlobal;

    public HudOverlay(Minecraft mcIn, EntityFlying entityFlying, ConfigGlobal manager)
    {
        super(mcIn);
        player = entityFlying;
        configGlobal = manager;
    }

    @Override
    public void renderGameOverlay(float partialTicks)
    {
        super.renderGameOverlay(partialTicks);
        Config config = configGlobal.activeConfig;
        if (config.hud && Minecraft.getMinecraft().inGameHasFocus && !Minecraft.getMinecraft().gameSettings.showDebugInfo)
        {
            int x = 5;
            int y = 5;
            if (player.flying)
            {
                String fly = player.flyBoosting ? configGlobal.flyDisplay + configGlobal.boostDisplay : configGlobal.flyDisplay;
                getFontRenderer().drawString(fly, x, y, 0xFFFFFF);
                y += 9;
            }
            if (player.sprinting)
            {
                String sprint = player.sprintBoosting ? configGlobal.sprintDisplay + configGlobal.boostDisplay : configGlobal.sprintDisplay;
                getFontRenderer().drawString(sprint, x, y, 0xFFFFFF);
            }
        }
    }
}
