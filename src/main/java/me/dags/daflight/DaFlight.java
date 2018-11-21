package me.dags.daflight;

import java.io.File;
import me.dags.daflight.handler.InputHandler;
import me.dags.daflight.handler.MessageHandler;
import me.dags.daflight.handler.MovementHandler;
import me.dags.daflight.handler.OverlayHandler;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import me.dags.daflight.util.FileUtil;

/**
 * @author dags <dags@dags.me>
 */
public class DaFlight {

    private static DaFlight instance;

    private final MovementHandler movementHandler;
    private final MessageHandler messageHandler;
    private final OverlayHandler overlayHandler;
    private final ConfigGlobal configGlobal;

    private InputHandler inputHandler;
    private Config config = new Config();

    private boolean singlePlayer = false;
    private String serverName = "";

    private DaFlight(File mcDir) {
        File configDir = FileUtil.createFolder(mcDir, "config");
        movementHandler = new MovementHandler(this);
        messageHandler = new MessageHandler();
        overlayHandler = new OverlayHandler();
        configGlobal = ConfigGlobal.getOrCreate(configDir);
        inputHandler = new InputHandler(configGlobal.getGlobalConfig(), movementHandler);
    }

    public ConfigGlobal globalConfig() {
        return configGlobal;
    }

    public Config config() {
        return config;
    }

    public InputHandler inputHandler() {
        return inputHandler;
    }

    public MessageHandler messageHandler() {
        return messageHandler;
    }

    public OverlayHandler overlayHandler() {
        return overlayHandler;
    }

    public MovementHandler movementHandler() {
        return movementHandler;
    }

    public boolean isSinglePlayer() {
        return singlePlayer;
    }

    public String getServerName() {
        return serverName;
    }

    public void tick(boolean inGame, boolean inGameFocus) {
        inputHandler().handleMenuInput();
        if (inGame && inGameFocus) {
            inputHandler().handleInput();
        }
    }

    public void setSinglePlayer(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void updateConfig() {
        config = configGlobal.getActiveConfig();
        inputHandler = new InputHandler(config, movementHandler);
    }

    public void displayConfig() {
//        MCHooks.GUI.displayScreen(new ConfigScreen(configGlobal));
    }

    public static DaFlight instance() {
        return instance;
    }

    public static void init(File mcDataDir) {
        if (instance == null) {
            instance = new DaFlight(mcDataDir);
        }
    }
}
