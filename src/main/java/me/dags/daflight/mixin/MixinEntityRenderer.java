package me.dags.daflight.mixin;

import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer
{
    @Inject(method = "setupViewBobbing(F)V", at = @At("HEAD"), cancellable = true)
    private void setupViewBobbing(float partialTicks, CallbackInfo callbackInfo)
    {
        if (DaFlight.instance().movementHandler().disableViewBob())
        {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "updateCameraAndRender", at = @At("RETURN"))
    public void onUpdateCameraAndRender(float partialTicks, long nanoTime, CallbackInfo callbackInfo)
    {
        MCHooks.Profiler.startSection("daflightOverlay");
        DaFlight.instance().overlayHandler().renderGameOverlay();
        MCHooks.Profiler.endSection();
    }
}
