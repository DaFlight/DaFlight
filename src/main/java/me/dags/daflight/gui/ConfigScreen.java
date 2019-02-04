package me.dags.daflight.gui;

import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dags_ <dags@dags.me>
 */

public class ConfigScreen extends GuiScreen {

    private List<Element> elements = new ArrayList<Element>();

    private Map<String, Slider> sliders = new LinkedHashMap<String, Slider>();
    private Map<String, Binding> binds = new LinkedHashMap<String, Binding>();

    private Toggle disable;
    private Toggle hud;
    private Toggle speedometer;
    private Toggle flight3d;
    private Toggle disableFov;
    private Toggle serverConfigs;

    private ConfigGlobal configGlobal;
    private Config config;

    public ConfigScreen(ConfigGlobal configGlobal) {
        this.configGlobal = configGlobal;
        this.config = configGlobal.getActiveConfig();
    }

    private void register(Element element) {
        if (element instanceof Slider) {
            sliders.put(element.getDisplayString(), (Slider) element);
        }
        if (element instanceof Binding) {
            binds.put(element.getDisplayString(), (Binding) element);
        }
        elements.add(element);
    }

    @Override
    public void initGui() {
        if (!elements.isEmpty()) {
            updateConfig();
        }

        elements.clear();
        sliders.clear();
        binds.clear();

        int displayWidth = MCHooks.GUI.displayWidth();
        int w = 300;
        int l = (displayWidth / 2) - (w / 2);
        int w0 = (w / 2) - 1;

        elements.add(new Label(8, 0x00b3b3).left(l).setDisplay("Options"));
        elements.add(disable = new Toggle(w0, 10, "Mod: Off", "Mod: On").set(config.disabled).left(l).attach(hud = new Toggle(w0, 10, "Hud: On", "Hud: Off").set(config.hud).left(l)));
        elements.add(speedometer = new Toggle(w0, 10, "Speedometer: On", "Speedometer: Off").set(config.speedometer).left(l).attach(disableFov = new Toggle(w0, 10, "FOV Effect: Off", "FOV Effect: On").set(config.disableFov).left(l)));
        elements.add(flight3d = new Toggle(w0, 10, "Flight Mode: 3D", "Flight Mode: Normal").set(config.flight3D).left(l).attach(serverConfigs = new Toggle(w0, 10, "Server Configs: On", "Server Configs: Off").set(configGlobal.serverConfigs).left(l)));

        elements.add(new Label(8, 0x00b3b3).left(l).setDisplay("Tuning"));
        register(new Slider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Fly Speed").setValue(config.flySpeed).setDefault(1F));
        register(new Slider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Fly Boost").setValue(config.flyBoost).setDefault(2F));
        register(new Slider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Sprint Speed").setValue(config.sprintSpeed).setDefault(1F));
        register(new Slider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Sprint Boost").setValue(config.sprintBoost).setDefault(2F));
        register(new Slider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Jump Modifier").setValue(config.jumpModifier).setDefault(2F));
        register(new Slider(w, 10, 2F, 0.0F, 4, 1).left(l).setDisplay("Strafe Modifier").setValue(config.strafeModifier).setDefault(1F));
        register(new Slider(w, 10, 2F, 0.0F, 4, 1).left(l).setDisplay("Ascend Modifier").setValue(config.verticalModifier).setDefault(1F));

        int w1 = (w / 3) * 2 - 1;
        int w2 = w - w1 - 2;
        elements.add(new Label(8, 0x00b3b3).left(l).setDisplay("Binds"));
        register(new Binding(w1, 10).left(l).setDisplay("Fly").setValue(config.fly).attach(new Toggle(w2, 10, "Type: Toggle", "Type: Hold").set(config.flyToggle)).setDefault("key.keyboard.f"));
        register(new Binding(w1, 10).left(l).setDisplay("Sprint").setValue(config.sprint).attach(new Toggle(w2, 10, "Type: Toggle", "Type: Hold").set(config.sprintToggle)).setDefault("key.keyboard.r"));
        register(new Binding(w1, 10).left(l).setDisplay("Boost").setValue(config.boost).attach(new Toggle(w2, 10, "Type: Toggle", "Type: Hold").set(config.boostToggle)).setDefault("key.keyboard.x"));
        register(new Binding(w1, 10).left(l).setDisplay("Fly Up").setValue(config.up).setDefault("key.keyboard.space"));
        register(new Binding(w1, 10).left(l).setDisplay("Fly Down").setValue(config.down).setDefault("key.keyboard.left.shift"));
        register(new Binding(w1, 10).left(l).setDisplay("Menu").setValue(config.menu).setDefault("key.keyboard.f9"));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int top = 5;
        for (Element s : elements) {
            s.top(top).draw(mouseX, mouseY);
            top += s.getHeight() + 2;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = false;
        for (Element s : elements) {
            result |= s.mouseClick(mouseX, mouseY, button);
        }
        return result || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean result = false;
        for (Element s : elements) {
            result |= s.mouseRelease();
        }
        return result || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int id, int b, int type) {
        boolean activeAndEsc = false;
        boolean result = false;
        for (Element e : elements) {
            activeAndEsc |= e.active() && id == MCHooks.Input.escape();
            result |= e.keyType('.', id);
        }
        return activeAndEsc || result || super.keyPressed(id, b, type);
    }

    @Override
    public void close() {
        updateConfig();
        configGlobal.save();
        DaFlight.instance().updateConfig();
        super.close();
    }

    private void updateConfig() {
        config.flySpeed = sliders.get("Fly Speed").get();
        config.flyBoost = sliders.get("Fly Boost").get();
        config.sprintSpeed = sliders.get("Sprint Speed").get();
        config.sprintBoost = sliders.get("Sprint Boost").get();
        config.jumpModifier = sliders.get("Jump Modifier").get();
        config.strafeModifier = sliders.get("Strafe Modifier").get();
        config.verticalModifier = sliders.get("Ascend Modifier").get();

        config.fly = binds.get("Fly").get();
        config.flyToggle = binds.get("Fly").getToggleValue();
        config.sprint = binds.get("Sprint").get();
        config.sprintToggle = binds.get("Sprint").getToggleValue();
        config.boost = binds.get("Boost").get();
        config.boostToggle = binds.get("Boost").getToggleValue();

        config.menu = binds.get("Menu").get();
        config.menu = config.menu.equals("") ? "F9" : config.menu;
        config.up = binds.get("Fly Up").get();
        config.down = binds.get("Fly Down").get();

        config.disabled = disable.boolValue();
        config.hud = hud.boolValue();
        config.speedometer = speedometer.boolValue();
        config.flight3D = flight3d.boolValue();
        config.disableFov = disableFov.boolValue();

        configGlobal.serverConfigs = serverConfigs.boolValue();
    }
}