package me.dags.daflight.handler;

import me.dags.daflight.DaFlight;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import net.minecraft.client.Minecraft;

/**
 * @author dags <dags@dags.me>
 */
public class OverlayHandler
{
    public void renderGameOverlay()
    {
        Config config = DaFlight.instance().config();
        if (config.hud && DaFlight.instance().inGameHasFocus() && !Minecraft.getMinecraft().gameSettings.showDebugInfo)
        {
            ConfigGlobal global = DaFlight.instance().globalConfig();
            MovementHandler handler = DaFlight.instance().movementHandler();
            int x = 5;
            int y = 5;
            if (handler.flying())
            {
                String fly = handler.flyBoosting() ? global.flyDisplay + global.boostDisplay : global.flyDisplay;
                Minecraft.getMinecraft().fontRendererObj.drawString(fly, x, y, 0xFFFFFF);
                y += 9;
            }
            if (handler.sprinting())
            {
                String sprint = handler.sprintBoosting() ? global.sprintDisplay + global.boostDisplay : global.sprintDisplay;
                Minecraft.getMinecraft().fontRendererObj.drawString(sprint, x, y, 0xFFFFFF);
            }
        }
    }
}
