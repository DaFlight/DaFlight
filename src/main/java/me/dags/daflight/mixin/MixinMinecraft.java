package me.dags.daflight.mixin;

import me.dags.daflight.DaFlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.main.GameConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraft
{
    @Final
    @Shadow
    public File mcDataDir;
    @Shadow
    public EntityPlayerSP thePlayer;
    @Shadow
    public boolean inGameHasFocus;

    @Inject(method = "startGame()V", at = @At("RETURN"))
    public void endStartGame(CallbackInfo callbackInfo)
    {
        DaFlight.init(mcDataDir);
    }

    @Inject(method = "runTick()V", at = @At("RETURN"))
    public void endRunTick(CallbackInfo callbackInfo)
    {
        Minecraft.getMinecraft().mcProfiler.startSection("daFlightTick");
        DaFlight.instance().tick(thePlayer != null, inGameHasFocus);
        Minecraft.getMinecraft().mcProfiler.endSection();
    }
}
