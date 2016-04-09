package me.dags.daflight.util;

import com.google.common.base.Optional;
import net.minecraft.client.Minecraft;

import java.io.File;

/**
 * @author dags_ <dags@dags.me>
 */

public class ConfigGlobal
{
    private transient File saveFile;
    private transient File daflightDir;
    public transient Config activeConfig;

    public boolean serverConfigs = false;
    public String flyDisplay = "fly";
    public String sprintDisplay = "sprint";
    public String boostDisplay = "+";

    private Config globalConfig = new Config();

    public void save()
    {
        if (serverConfigs && !Minecraft.getMinecraft().isSingleplayer())
        {
            if (activeConfig == null || activeConfig == globalConfig)
            {
                String loc = Minecraft.getMinecraft().getCurrentServerData().serverIP.replace(":", "-").replace("-25565", "");
                activeConfig = Config.getOrCreate(new File(daflightDir, loc + ".json"));
            }
            activeConfig.save();
        }
        else
        {
            activeConfig = globalConfig;
        }
        FileUtil.serialize(this, saveFile);
    }

    public static ConfigGlobal getOrCreate(File folder)
    {
        File globalFile = new File(folder, "daflight_global.json");
        Optional<ConfigGlobal> optional = FileUtil.deserialize(globalFile, ConfigGlobal.class);
        ConfigGlobal configGlobal = optional.isPresent() ? optional.get() : new ConfigGlobal();
        configGlobal.saveFile = globalFile;
        configGlobal.daflightDir = FileUtil.createFolder(folder, "mods", "daflight");
        configGlobal.save();
        return configGlobal;
    }
}
