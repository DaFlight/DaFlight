package me.dags.daflight;

import io.netty.buffer.Unpooled;
import me.dags.daflight.handler.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.io.File;

/**
 * @author dags <dags@dags.me>
 */
public class MCHooks {

    public static class Game {

        public static Minecraft getInstance() {
            return Minecraft.getMinecraft();
        }

        public static File gameDir() {
            return Game.getInstance().gameDir;
        }

        public static boolean isSinglePlayer() {
            return Game.getInstance().isSingleplayer();
        }

        public static boolean inGame() {
            return Player.present();
        }

        public static boolean inGameHasFocus() {
            return Game.getInstance().isWindowFocused() && Game.getInstance().currentScreen == null;
        }

        public static boolean displayDebugInfo() {
            return Game.getInstance().gameSettings.showDebugInfo;
        }
    }

    public static class GUI {

        public static void displayScreen(GuiScreen screen) {
            Game.getInstance().displayGuiScreen(screen);
        }

        public static int displayWidth() {
            return Game.getInstance().mainWindow.getScaledWidth();
        }

        public static int stringWidth(String in) {
            return Game.getInstance().fontRenderer.getStringWidth(in);
        }

        public static void drawRectangle(int left, int top, int right, int bottom, int color) {
            Gui.drawRect(left, top, right, bottom, color);
        }

        public static void drawString(String string, int x, int y, int colour) {
            drawString(string, x, y, colour, false);
        }

        public static void drawString(String string, int x, int y, int colour, boolean withShadow) {
            if (withShadow) {
                Game.getInstance().fontRenderer.drawStringWithShadow(string, x, y, colour);
            } else {
                Game.getInstance().fontRenderer.func_211126_b(string, x, y, colour);
            }
        }
    }

    public static class Input {

        public static int shift() {
            return id("key.keyboard.left.shift");
        }

        public static int escape() {
            return id("key.keyboard.escape");
        }

        public static int backspace() {
            return id("key.keyboard.backspace");
        }

        public static boolean isShiftDown() {
            return isDown(id("key.keyboard.left.shift"));
        }

        public static boolean isDown(int id) {
            return InputMappings.isKeyDown(id);
        }

        public static boolean isDown(String name) {
            return InputMappings.isKeyDown(id(name));
        }

        public static int id(String name) {
            return InputMappings.getInputByName(name).getKeyCode();
        }

        public static String display(String name) {
            return InputMappings.getInputByName(name).getName();
        }

        public static String mouseName(int id) {
            return mouse(id).getUnlocalizedName();
        }

        public static String keyboardName(int id) {
            return keyboard(id).getUnlocalizedName();
        }

        public static InputMappings.Input mouse(int id) {
            return InputMappings.Type.MOUSE.getOrMakeInput(id);
        }

        public static InputMappings.Input keyboard(int id) {
            return InputMappings.Type.KEYSYM.getOrMakeInput(id);
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
            return present() && (getPlayer().capabilities.allowFlying || Game.getInstance().isSingleplayer());
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
            if (Game.getInstance().isSingleplayer() && !isInvulnerable()) {
                MinecraftServer server = Game.getInstance().getIntegratedServer();
                if (server != null) {
                    EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(getPlayer().getUniqueID());
                    if (player != null) {
                        player.capabilities.disableDamage = state;
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
            return Game.getInstance().player;
        }
    }


    public static class Network {

        public static void sendChannels(ResourceLocation... channels) {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            for (ResourceLocation channel : channels) {
                buffer.writeResourceLocation(channel);
            }
            CPacketCustomPayload payload = new CPacketCustomPayload(MessageHandler.REGISTER, buffer);
            sendPayload(payload);
        }

        public static void sendMessageBytes(ResourceLocation channel, byte[] data) {
            PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(data));
            CPacketCustomPayload payload = new CPacketCustomPayload(channel, buf);
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
            Game.getInstance().profiler.startSection(section);
        }

        public static void endSection() {
            Game.getInstance().profiler.endSection();
        }
    }
}
