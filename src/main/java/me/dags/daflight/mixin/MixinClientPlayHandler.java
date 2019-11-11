package me.dags.daflight.mixin;

import me.dags.daflight.DaFlight;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(ClientPlayNetHandler.class)
public abstract class MixinClientPlayHandler {

    @Inject(method = "handleCustomPayload(Lnet/minecraft/network/play/server/SCustomPayloadPlayPacket;)V", at = @At("RETURN"))
    public void handleCustomPayload(SCustomPayloadPlayPacket packetIn, CallbackInfo callbackInfo) {
        ResourceLocation channel = packetIn.getChannelName();
        DaFlight.instance().messageHandler().handlePacket(channel, packetIn.getBufferData());
    }
}
