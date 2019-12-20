package me.dags.daflight;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class Launch implements IMixinConnector {
    @Override
    public void connect() {
        Mixins.addConfiguration("mixin.daflight.json");
    }
}
