package me.dags.daflight.handler;

import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * @author dags <dags@dags.me>
 */
public class MessageHandler {

    public static final ResourceLocation FLY = new ResourceLocation("daflight", "fly");
    public static final ResourceLocation SPRINT = new ResourceLocation("daflight", "sprint");
    public static final ResourceLocation CONNECT = new ResourceLocation("daflight", "connect");
    public static final ResourceLocation REGISTER = new ResourceLocation("daflight", "register");

    public void handlePacket(ResourceLocation channel, PacketBuffer packetBuffer) {
        if (FLY.equals(channel)) {
            DaFlight.instance().movementHandler().maxFlySpeed = packetBuffer.readFloat();
        } else if (SPRINT.equals(channel)) {
            DaFlight.instance().movementHandler().maxWalkSpeed = packetBuffer.readFloat();
        }
    }

    public void registerChannels() {
        MCHooks.Network.sendChannels(FLY, SPRINT, CONNECT);
    }

    public void connect() {
        MCHooks.Network.sendMessageBytes(CONNECT, new byte[0]);
    }

    void sendPlayerAbilities() {
        MCHooks.Player.sendPlayerAbilities();
    }

    void sendState(ResourceLocation channel, boolean value) {
        byte[] data = {value ? (byte) 1 : (byte) 0};
        MCHooks.Network.sendMessageBytes(channel, data);
    }
}
