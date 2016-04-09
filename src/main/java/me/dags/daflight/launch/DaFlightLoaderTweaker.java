package me.dags.daflight.launch;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.List;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightLoaderTweaker implements ITweaker
{
    @Override
    public void acceptOptions(List<String> list, File gameDir, File assetsDir, String profile)
    {
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader)
    {
        MixinBootstrap.init();
        MixinEnvironment.getDefaultEnvironment().addConfiguration("mixin.daflight.json");
    }

    @Override
    public String getLaunchTarget()
    {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments()
    {
        return new String[0];
    }
}
