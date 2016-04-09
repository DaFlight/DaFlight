package me.dags.daflight.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.dags.daflight.DaFlight;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {

    @Inject(method = "renderGameOverlay(F)V", at = @At("RETURN"))
    public void onRenderGameOverlay(float ticks, CallbackInfo ci) {
        DaFlight.INSTANCE.hudOverlay.renderGameOverlay(ticks);
    }
}
