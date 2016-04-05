package me.dags.daflight.util;

import com.google.common.base.Optional;

import java.io.File;

/**
 * @author dags_ <dags@dags.me>
 */

public class Config
{
    protected transient File saveFile;

    public float flySpeed = 1F;
    public float sprintSpeed = 1F;
    public float strafeModifier = 1.0F;
    public float verticalModifier = 1.0F;
    public float jumpModifier = 2.0F;
    public float flyBoost = 2.0F;
    public float sprintBoost = 2.0F;

    public String fly = "F";
    public String sprint = "R";
    public String boost = "X";
    public String menu = "F9";
    public String up = "SPACE";
    public String down = "LSHIFT";

    public boolean flyToggle = true;
    public boolean sprintToggle = true;
    public boolean boostToggle = true;

    public boolean disabled = false;
    public boolean hud = true;
    public boolean flight3D = false;
    public boolean disableFov = true;

    public void save()
    {
        if (saveFile != null) me.dags.daflight.util.FileUtil.serialize(this, saveFile);
    }

    public static Config getOrCreate(File file)
    {
        Optional<Config> optional = FileUtil.deserialize(file, Config.class);
        if (optional.isPresent())
        {
            optional.get().saveFile = file;
            return optional.get();
        }

        Config config = new Config();
        config.saveFile = file;
        config.save();
        return config;
    }
}
