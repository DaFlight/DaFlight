package me.dags.daflight.launch;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightTweaker implements ITweaker {

    private final List<String> args = new ArrayList<String>();

    @Override
    public void acceptOptions(List<String> list, File gameDir, File assetsDir, String profile) {
        args.addAll(list);
        if (!args.contains("--version") && profile != null) {
            this.args.add("--version");
            this.args.add(profile);
        }
        if (!args.contains("--assetsDir") && assetsDir != null) {
            this.args.add("--assetsDir");
            this.args.add(assetsDir.getPath());
        }
        if (!args.contains("--gameDir") && gameDir != null) {
            this.args.add("--gameDir");
            this.args.add(gameDir.getPath());
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixin.daflight.json");
        if (fmlIsPresent()) {
            MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        }
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[args.size()]);
    }

    private static boolean fmlIsPresent() {
        for (IClassTransformer transformer : Launch.classLoader.getTransformers()) {
            if (transformer.getClass().getName().contains("fml")) {
                return true;
            }
        }
        return false;
    }
}
