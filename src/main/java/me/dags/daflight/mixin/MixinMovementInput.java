package me.dags.daflight.mixin;

import me.dags.daflight.DaFlight;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(MovementInputFromOptions.class)
public abstract class MixinMovementInput extends MovementInput
{
    @Inject(method = "updatePlayerMoveState()V", at = @At("RETURN"))
    public void updatePlayerMoveState(CallbackInfo callbackInfo)
    {
        if (this.sneak && (DaFlight.instance().movementHandler().flying() || DaFlight.instance().movementHandler().sprinting()))
        {
            this.moveForward /= 0.3D;
            this.moveStrafe /= 0.3D;
        }
    }
}
