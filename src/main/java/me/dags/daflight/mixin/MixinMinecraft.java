package me.dags.daflight.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.dags.daflight.DaFlight;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft
{

    @Inject(method = "runTick()V", at = @At("RETURN"))
    public void onTick(CallbackInfo ci)
    {
        DaFlight.INSTANCE.handleInput();
    }
}
