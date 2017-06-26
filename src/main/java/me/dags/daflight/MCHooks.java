package me.dags.daflight;

import com.google.common.base.Charsets;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
public class MCHooks {

    public static class Game {

        public static File gameDir() {
            return Minecraft.getMinecraft().mcDataDir;
        }

        public static boolean inGame() {
            return Player.present();
        }

        public static boolean inGameHasFocus() {
            return Minecraft.getMinecraft().inGameHasFocus;
        }

        public static boolean displayDebugInfo() {
            return Minecraft.getMinecraft().gameSettings.showDebugInfo;
        }
    }

    public static class GUI {

        public static void displayScreen(GuiScreen screen) {
            Minecraft.getMinecraft().displayGuiScreen(screen);
        }

        public static int displayWidth() {
            return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
        }

        public static int stringWidth(String in) {
            return Minecraft.getMinecraft().fontRenderer.getStringWidth(in);
        }

        public static void drawRectangle(int left, int top, int right, int bottom, int color) {
            Gui.drawRect(left, top, right, bottom, color);
        }

        public static void drawString(String string, int x, int y, int colour) {
            drawString(string, x, y, colour, false);
        }

        public static void drawString(String string, int x, int y, int colour, boolean withShadow) {
            if (withShadow) {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string, x, y, colour);
            } else {
                Minecraft.getMinecraft().fontRenderer.drawString(string, x, y, colour);
            }
        }
    }

    public static class Player {

        public static boolean present() {
            return getPlayer() != null;
        }

        public static boolean isClientPlayer(Object toTest) {
            return getPlayer() == toTest;
        }

        public static BlockPos position() {
            return getPlayer().getPosition();
        }

        public static boolean allowFlying() {
            return present() && (getPlayer().capabilities.allowFlying || Minecraft.getMinecraft().isSingleplayer());
        }

        public static boolean isFlying() {
            return present() && getPlayer().capabilities.isFlying;
        }

        private static boolean isInvulnerable() {
            return present() && getPlayer().isSpectator() || getPlayer().capabilities.isCreativeMode;
        }

        public static void setFlying(boolean state) {
            if (present()) {
                getPlayer().capabilities.isFlying = state;
            }
        }

        public static void setInvincible(boolean state) {
            if (Minecraft.getMinecraft().isSingleplayer() && !isInvulnerable()) {
                MinecraftServer server = Minecraft.getMinecraft().getIntegratedServer();
                if (server != null) {
                    Entity entity = server.getEntityFromUuid(getPlayer().getUniqueID());
                    if (entity != null) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) entity;
                        playerMP.capabilities.disableDamage = state;
                    }
                }
            }
        }

        public static void sendPlayerAbilities() {
            if (present()) {
                getPlayer().sendPlayerAbilities();
            }
        }

        public static class Input {

            public static float forward() {
                return present() ? getPlayer().movementInput.moveForward : 0F;
            }

            public static float strafe() {
                return present() ? getPlayer().movementInput.moveStrafe : 0F;
            }
        }

        private static EntityPlayerSP getPlayer() {
            return Minecraft.getMinecraft().player;
        }
    }


    public static class Network {

        public static void sendChannels(String channels) {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeBytes(channels.getBytes(Charsets.UTF_8));
            CPacketCustomPayload payload = new CPacketCustomPayload("REGISTER", buffer);
            sendPayload(payload);
        }

        public static void sendMessageBytes(String channel, byte[] data) {
            CPacketCustomPayload payload = new CPacketCustomPayload(channel, new PacketBuffer(Unpooled.wrappedBuffer(data)));
            sendPayload(payload);
        }

        private static void sendPayload(CPacketCustomPayload packetCustomPayload) {
            if (Player.present() && Player.getPlayer().connection != null) {
                Player.getPlayer().connection.sendPacket(packetCustomPayload);
            }
        }
    }

    public static class Profiler {

        public static void startSection(String section) {
            Minecraft.getMinecraft().mcProfiler.startSection(section);
        }

        public static void endSection() {
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
    }
}
