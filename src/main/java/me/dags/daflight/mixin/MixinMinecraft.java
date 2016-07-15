package me.dags.daflight.mixin;

import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraft
{
    @Inject(method = "startGame()V", at = @At("RETURN"))
    public void endStartGame(CallbackInfo callbackInfo)
    {
        DaFlight.init(MCHooks.Game.gameDir());
    }

    @Inject(method = "runTick()V", at = @At("RETURN"))
    public void endRunTick(CallbackInfo callbackInfo)
    {
        MCHooks.Profiler.startSection("daFlightTick");
        DaFlight.instance().tick(MCHooks.Game.inGame(), MCHooks.Game.inGameHasFocus());
        MCHooks.Profiler.endSection();
    }
}
