package me.dags.daflight.handler;

import me.dags.daflight.DaFlight;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

/**
 * @author dags <dags@dags.me>
 */
public class OverlayHandler
{
    private int lastX = 0, lastZ = 0;
    private long lastTime = 0L;
    private String speed = "0";

    public void renderGameOverlay()
    {
        Config config = DaFlight.instance().config();
        if (config.hud && DaFlight.instance().inGameHasFocus() && !Minecraft.getMinecraft().gameSettings.showDebugInfo)
        {
            ConfigGlobal global = DaFlight.instance().globalConfig();
            MovementHandler handler = DaFlight.instance().movementHandler();
            int x = 5;
            int y = 5;
            boolean active = false;
            if (handler.flying())
            {
                active = true;
                String fly = handler.flyBoosting() ? global.flyDisplay + global.boostDisplay : global.flyDisplay;
                Minecraft.getMinecraft().fontRendererObj.drawString(fly, x, y, 0xFFFFFF);
                y += 9;
            }
            if (handler.sprinting())
            {
                active = true;
                String sprint = handler.sprintBoosting() ? global.sprintDisplay + global.boostDisplay : global.sprintDisplay;
                Minecraft.getMinecraft().fontRendererObj.drawString(sprint, x, y, 0xFFFFFF);
                y += 9;
            }
            if (active && DaFlight.instance().config().speedometer)
            {
                drawSpeed(x, y);
            }
        }
    }

    private void drawSpeed(int x, int y)
    {
        long time = System.currentTimeMillis();
        long duration = time - lastTime;
        if (duration >= 500)
        {
            lastTime = time;
            BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition();
            int dx = lastX - pos.getX();
            int dz = lastZ - pos.getZ();
            lastX = pos.getX();
            lastZ = pos.getZ();
            // checking every 500ms (1/2 second) so x2 to get distance / second
            double blocksPerSecond = 2D * Math.sqrt((dx*dx) + (dz*dz)) / (duration / 500D);
            if (blocksPerSecond > 16000)
            {
                speed = String.format("%.2f c/s", blocksPerSecond / 16D);
            }
            else
            {
                speed = String.format("%.2f b/s", blocksPerSecond);
            }
        }
        Minecraft.getMinecraft().fontRendererObj.drawString(speed, x, y, 0xFFFFFF);
    }
}
