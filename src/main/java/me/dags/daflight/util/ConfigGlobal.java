package me.dags.daflight.util;

import com.google.common.base.Optional;
import me.dags.daflight.DaFlight;

import java.io.File;

/**
 * @author dags_ <dags@dags.me>
 */

public class ConfigGlobal {

    private transient File saveFile;
    private transient File daflightDir;
    private transient Config activeConfig;

    public boolean serverConfigs = false;
    public String flyDisplay = "fly";
    public String sprintDisplay = "sprint";
    public String boostDisplay = "+";

    private Config globalConfig = new Config();

    public Config getGlobalConfig() {
        return globalConfig;
    }

    public Config getActiveConfig() {
        if (serverConfigs && !DaFlight.instance().isSinglePlayer()) {
            if (activeConfig == null || activeConfig == globalConfig) {
                String loc = DaFlight.instance().getServerName();
                activeConfig = Config.getOrCreate(new File(daflightDir, loc + ".json"));
            }
            activeConfig.save();
        } else {
            activeConfig = globalConfig;
        }
        return activeConfig;
    }

    public void save() {
        if (serverConfigs && !DaFlight.instance().isSinglePlayer()) {
            if (activeConfig == null || activeConfig == globalConfig) {
                String loc = DaFlight.instance().getServerName();
                activeConfig = Config.getOrCreate(new File(daflightDir, loc + ".json"));
            }
            activeConfig.save();
        } else {
            activeConfig = globalConfig;
        }
        FileUtil.serialize(this, saveFile);
    }

    public static ConfigGlobal getOrCreate(File configDir) {
        File globalFile = new File(configDir, "daflight-global.json");
        Optional<ConfigGlobal> optional = FileUtil.deserialize(globalFile, ConfigGlobal.class);
        ConfigGlobal configGlobal = optional.or(new ConfigGlobal());
        configGlobal.saveFile = globalFile;
        configGlobal.daflightDir = FileUtil.createFolder(configDir, "daflight-servers");
        if (!optional.isPresent()) {
            FileUtil.serialize(configGlobal, configGlobal.saveFile);
        }
        return configGlobal;
    }
}
