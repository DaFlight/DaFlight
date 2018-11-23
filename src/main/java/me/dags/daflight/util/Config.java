package me.dags.daflight.util;

import me.dags.daflight.MCHooks;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author dags_ <dags@dags.me>
 */

public class Config {

    private transient File saveFile;

    public float flySpeed = 1F;
    public float sprintSpeed = 1F;
    public float strafeModifier = 1.0F;
    public float verticalModifier = 1.0F;
    public float jumpModifier = 2.0F;
    public float flyBoost = 2.0F;
    public float sprintBoost = 2.0F;

    public String fly = "key.keyboard.f";
    public String sprint = "key.keyboard.r";
    public String boost = "key.keyboard.x";
    public String menu = "key.keyboard.f9";
    public String up = "key.keyboard.space";
    public String down = "key.keyboard.left.shift";

    public boolean flyToggle = true;
    public boolean sprintToggle = true;
    public boolean boostToggle = true;

    public boolean disabled = false;
    public boolean hud = true;
    public boolean speedometer = false;
    public boolean flight3D = false;
    public boolean disableFov = true;

    public void save() {
        if (saveFile != null) FileUtil.serialize(this, saveFile);
    }

    public void checkInputs() {
        boolean valid;
        valid = check(fly, "key.keyboard.f", k -> fly = k);
        valid = check(sprint, "key.keyboard.r", k -> sprint = k) & valid;
        valid = check(boost, "key.keyboard.x", k -> boost = k) & valid;
        valid = check(menu, "key.keyboard.f9", k -> menu = k) & valid;
        valid = check(up, "key.keyboard.space", k -> up = k) & valid;
        valid = check(down, "key.keyboard.left.shift", k -> down = k) & valid;
        if (!valid) {
            save();
        }
    }

    private static boolean check(String value, String def, Consumer<String> setter) {
        try {
            if (MCHooks.Input.id(value) != -1) {
                return true;
            }
        } catch (Throwable ignore) {

        }
        setter.accept(def);
        return false;
    }

    public static Config getOrCreate(File file) {
        Optional<Config> optional = FileUtil.deserialize(file, Config.class);
        if (optional.isPresent()) {
            optional.get().saveFile = file;
            optional.get().checkInputs();
            return optional.get();
        }

        Config config = new Config();
        config.saveFile = file;
        config.save();
        return config;
    }
}