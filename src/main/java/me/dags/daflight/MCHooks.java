package me.dags.daflight;

import com.google.common.base.Charsets;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.io.File;

/**
 * @author dags <dags@dags.me>
 */
public class MCHooks
{

    public static class Game
    {

        public static File gameDir()
        {
            return Minecraft.getMinecraft().mcDataDir;
        }

        public static boolean inGame()
        {
            return Player.present();
        }

        public static boolean inGameHasFocus()
        {
            return Minecraft.getMinecraft().inGameHasFocus;
        }

        public static boolean displayDebugInfo()
        {
            return Minecraft.getMinecraft().gameSettings.showDebugInfo;
        }
    }

    public static class GUI
    {

        public static void displayScreen(GuiScreen screen)
        {
            Minecraft.getMinecraft().displayGuiScreen(screen);
        }

        public static int displayWidth()
        {
            return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
        }

        public static int stringWidth(String in)
        {
            return Minecraft.getMinecraft().fontRendererObj.getStringWidth(in);
        }

        public static void drawRectangle(int left, int top, int right, int bottom, int color)
        {
            Gui.drawRect(left, top, right, bottom, color);
        }

        public static void drawString(String string, int x, int y, int colour)
        {
            drawString(string, x, y, colour, false);
        }

        public static void drawString(String string, int x, int y, int colour, boolean withShadow)
        {
            if (withShadow)
            {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(string, x, y, colour);
            } else
            {
                Minecraft.getMinecraft().fontRendererObj.drawString(string, x, y, colour);
            }
        }
    }

    public static class Player
    {
        public static boolean present()
        {
            return Minecraft.getMinecraft().thePlayer != null;
        }

        public static boolean isClientPlayer(Object toTest)
        {
            return Minecraft.getMinecraft().thePlayer == toTest;
        }

        public static BlockPos position()
        {
            return Minecraft.getMinecraft().thePlayer.getPosition();
        }

        public static boolean allowFlying()
        {
            return present() && (Minecraft.getMinecraft().thePlayer.capabilities.allowFlying || Minecraft.getMinecraft().isSingleplayer());
        }

        public static boolean isFlying()
        {
            return present() && Minecraft.getMinecraft().thePlayer.capabilities.isFlying;
        }

        private static boolean isInvulnerable()
        {
            return present() && Minecraft.getMinecraft().thePlayer.isSpectator() || Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode;
        }

        public static void setFlying(boolean state)
        {
            if (present())
            {
                Minecraft.getMinecraft().thePlayer.capabilities.isFlying = state;
            }
        }

        public static void setInvincible(boolean state)
        {
            if (Minecraft.getMinecraft().isSingleplayer() && !isInvulnerable())
            {
                MinecraftServer server = Minecraft.getMinecraft().getIntegratedServer();
                if (server != null)
                {
                    Entity entity = server.getEntityFromUuid(Minecraft.getMinecraft().thePlayer.getUniqueID());
                    if (entity != null)
                    {
                        EntityPlayerMP playerMP = (EntityPlayerMP) entity;
                        playerMP.capabilities.disableDamage = state;
                    }
                }
            }
        }

        public static void sendPlayerAbilities()
        {
            if (present())
            {
                Minecraft.getMinecraft().thePlayer.sendPlayerAbilities();
            }
        }

        public static class Input
        {

            public static float forward()
            {
                return present() ? Minecraft.getMinecraft().thePlayer.movementInput.moveForward : 0F;
            }

            public static float strafe()
            {
                return present() ? Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe : 0F;
            }
        }
    }

    public static class Network
    {

        public static void sendChannels(String channels)
        {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeBytes(channels.getBytes(Charsets.UTF_8));
            CPacketCustomPayload payload = new CPacketCustomPayload("REGISTER", buffer);
            sendPayload(payload);
        }

        public static void sendMessageBytes(String channel, byte[] data)
        {
            CPacketCustomPayload payload = new CPacketCustomPayload(channel, new PacketBuffer(Unpooled.wrappedBuffer(data)));
            sendPayload(payload);
        }

        private static void sendPayload(CPacketCustomPayload packetCustomPayload)
        {
            if (Player.present() && Minecraft.getMinecraft().thePlayer.connection != null)
            {
                Minecraft.getMinecraft().thePlayer.connection.sendPacket(packetCustomPayload);
            }
        }
    }

    public static class Profiler
    {

        public static void startSection(String section)
        {
            Minecraft.getMinecraft().mcProfiler.startSection(section);
        }

        public static void endSection()
        {
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
    }
}
