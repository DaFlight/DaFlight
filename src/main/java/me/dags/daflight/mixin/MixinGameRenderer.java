package me.dags.daflight.mixin;

import me.dags.daflight.DaFlight;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(method = "applyBobbing(F)V", at = @At("HEAD"), cancellable = true)
    private void onApplyBobbing(float partialTicks, CallbackInfo callbackInfo) {
        if (DaFlight.instance().movementHandler().disableViewBob()) {
            callbackInfo.cancel();
        }
    }
}
