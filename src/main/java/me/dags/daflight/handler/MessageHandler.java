package me.dags.daflight.handler;

import com.google.common.base.Charsets;
import io.netty.buffer.Unpooled;
import me.dags.daflight.DaFlight;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

/**
 * @author dags <dags@dags.me>
 */
public class MessageHandler
{
    static final String CHANNEL_FLY = "DAFLIGHT-FLY";
    static final String CHANNEL_SPRINT = "DAFLIGHT-SPRINT";
    static final String CHANNEL_CONNECT = "DAFLIGHT-CONNECT";

    public void handlePacket(String channel, PacketBuffer packetBuffer)
    {
        if (CHANNEL_FLY.equals(channel))
        {
            float speed = packetBuffer.readFloat();
            DaFlight.instance().movementHandler().setMaxFlySpeed(speed);
        }
        else if (CHANNEL_SPRINT.equals(channel))
        {
            float speed = packetBuffer.readFloat();
            DaFlight.instance().movementHandler().setMaxWalkSpeed(speed);
        }
    }

    public void registerChannels()
    {
        String channels = CHANNEL_FLY + "\u0000" + CHANNEL_SPRINT + "\u0000" + CHANNEL_CONNECT;
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        buffer.writeBytes(channels.getBytes(Charsets.UTF_8));
        send(new CPacketCustomPayload("REGISTER", buffer));
    }

    public void connect()
    {
        send(new CPacketCustomPayload(CHANNEL_CONNECT, new PacketBuffer(Unpooled.wrappedBuffer(new byte[0]))));
    }

    void sendPlayerAbilities()
    {
        Minecraft.getMinecraft().thePlayer.sendPlayerAbilities();
    }

    void sendState(String channel, boolean value)
    {
        byte[] data = {value ? (byte) 1 : (byte) 0};
        send(new CPacketCustomPayload(channel, new PacketBuffer(Unpooled.wrappedBuffer(data))));
    }

    private static void send(CPacketCustomPayload payload)
    {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(payload);
    }
}
