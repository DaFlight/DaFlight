package me.dags.daflight.mixin;

import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import net.minecraft.client.gui.IngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(IngameGui.class)
public class MixinGuiIngame {

    @Inject(method = "renderGameOverlay(F)V", at = @At("RETURN"))
    public void onRenderGameOverlay(float partialTicks, CallbackInfo callbackInfo) {
        MCHooks.Profiler.startSection("daflightOverlay");
        DaFlight.instance().overlayHandler().renderGameOverlay();
        MCHooks.Profiler.endSection();
    }
}
