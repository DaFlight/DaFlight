package me.dags.daflight.handler;

import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import net.minecraft.network.PacketBuffer;

/**
 * @author dags <dags@dags.me>
 */
public class MessageHandler {

    static final String CHANNEL_FLY = "DAFLIGHT-FLY";
    static final String CHANNEL_SPRINT = "DAFLIGHT-SPRINT";
    static final String CHANNEL_CONNECT = "DAFLIGHT-CONNECT";

    public void handlePacket(String channel, PacketBuffer packetBuffer) {
        if (CHANNEL_FLY.equals(channel)) {
            float speed = packetBuffer.readFloat();
            DaFlight.instance().movementHandler().setMaxFlySpeed(speed);
        } else if (CHANNEL_SPRINT.equals(channel)) {
            float speed = packetBuffer.readFloat();
            DaFlight.instance().movementHandler().setMaxWalkSpeed(speed);
        }
    }

    public void registerChannels() {
        String channels = CHANNEL_FLY + "\u0000" + CHANNEL_SPRINT + "\u0000" + CHANNEL_CONNECT;
        MCHooks.Network.sendChannels(channels);
    }

    public void connect() {
        MCHooks.Network.sendMessageBytes(CHANNEL_CONNECT, new byte[0]);
    }

    void sendPlayerAbilities() {
        MCHooks.Player.sendPlayerAbilities();
    }

    void sendState(String channel, boolean value) {
        byte[] data = {value ? (byte) 1 : (byte) 0};
        MCHooks.Network.sendMessageBytes(channel, data);
    }
}
