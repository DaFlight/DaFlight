package me.dags.daflight.launch;

import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.mixin.Mixins;

public class DaFlightRiftLoader implements InitializationListener {
    @Override
    public void onInitialization() {
        Mixins.addConfiguration("mixin.daflight.json");
    }
}
