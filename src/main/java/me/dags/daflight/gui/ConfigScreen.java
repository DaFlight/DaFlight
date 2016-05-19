package me.dags.daflight.gui;

import me.dags.daflight.DaFlight;
import me.dags.daflight.util.Config;
import me.dags.daflight.util.ConfigGlobal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dags_ <dags@dags.me>
 */

public class ConfigScreen extends GuiScreen
{
    private List<UIElement> elements = new ArrayList<UIElement>();

    private Map<String, UISlider> sliders = new LinkedHashMap<String, UISlider>();
    private Map<String, UIBind> binds = new LinkedHashMap<String, UIBind>();

    private UIToggle disable;
    private UIToggle hud;
    private UIToggle flight3d;
    private UIToggle disableFov;
    private UIToggle serverConfigs;

    private ConfigGlobal configGlobal;
    private Config config;

    public ConfigScreen(ConfigGlobal configGlobal)
    {
        this.configGlobal = configGlobal;
        this.config = configGlobal.getActiveConfig();
    }

    private void register(UIElement element)
    {
        if (element instanceof UISlider)
        {
            sliders.put(element.getDisplayString(), (UISlider) element);
        }
        if (element instanceof UIBind)
        {
            binds.put(element.getDisplayString(), (UIBind) element);
        }
        elements.add(element);
    }

    @Override
    public void initGui()
    {
        if (!elements.isEmpty())
        {
            updateConfig();
        }

        elements.clear();
        sliders.clear();
        binds.clear();

        int displayWidth = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
        int w = 300;
        int l = (displayWidth / 2) - (w / 2);

        elements.add(new UILabel(8, 0x00b3b3).left(l).setDisplay("Options"));
        elements.add(disable = new UIToggle(w, 10, "Mod: Disabled", "Mod: Enabled").set(config.disabled).left(l));
        elements.add(hud = new UIToggle(w, 10, "Hud: Enabled", "Hud: Disabled").set(config.hud).left(l));
        elements.add(flight3d = new UIToggle(w, 10, "Flight Mode: 3D", "Flight Mode: Normal").set(config.flight3D).left(l));
        elements.add(disableFov = new UIToggle(w, 10, "FOV Effect: Disabled", "FOV Effect: Enabled").set(config.disableFov).left(l));
        elements.add(serverConfigs = new UIToggle(w, 10, "Server Configs: Enabled", "Server Configs: Disabled").set(configGlobal.serverConfigs).left(l));

        elements.add(new UILabel(8, 0x00b3b3).left(l).setDisplay("Tuning"));
        register(new UISlider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Fly Speed").setValue(config.flySpeed).setDefault(1F));
        register(new UISlider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Fly Boost").setValue(config.flyBoost).setDefault(2F));
        register(new UISlider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Sprint Speed").setValue(config.sprintSpeed).setDefault(1F));
        register(new UISlider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Sprint Boost").setValue(config.sprintBoost).setDefault(2F));
        register(new UISlider(w, 10, 10F, 0.0F, 4, 1).left(l).setDisplay("Jump Modifier").setValue(config.jumpModifier).setDefault(2F));
        register(new UISlider(w, 10, 2F, 0.0F, 4, 1).left(l).setDisplay("Strafe Modifier").setValue(config.strafeModifier).setDefault(1F));
        register(new UISlider(w, 10, 2F, 0.0F, 4, 1).left(l).setDisplay("Ascend Modifier").setValue(config.verticalModifier).setDefault(1F));

        int w1 = (w / 3) * 2 - 1;
        int w2 = w - w1 - 2;
        elements.add(new UILabel(8, 0x00b3b3).left(l).setDisplay("Binds"));
        register(new UIBind(w1, 10).left(l).setDisplay("Fly").setValue(config.fly).attach(new UIToggle(w2, 10, "Type: Toggle", "Type: Hold").set(config.flyToggle)).setDefault("F"));
        register(new UIBind(w1, 10).left(l).setDisplay("Sprint").setValue(config.sprint).attach(new UIToggle(w2, 10, "Type: Toggle", "Type: Hold").set(config.sprintToggle)).setDefault("R"));
        register(new UIBind(w1, 10).left(l).setDisplay("Boost").setValue(config.boost).attach(new UIToggle(w2, 10, "Type: Toggle", "Type: Hold").set(config.boostToggle)).setDefault("X"));
        register(new UIBind(w1, 10).left(l).setDisplay("Fly Up").setValue(config.up).setDefault("SPACE"));
        register(new UIBind(w1, 10).left(l).setDisplay("Fly Down").setValue(config.down).setDefault("LSHIFT"));
        register(new UIBind(w1, 10).left(l).setDisplay("Menu").setValue(config.menu).setDefault("F9"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        int top = 5;
        for (UIElement s : elements)
        {
            s.top(top).draw(mouseX, mouseY);
            top += s.getHeight() + 2;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        for (UIElement s : elements)
        {
            s.mouseClick(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        for (UIElement s : elements)
        {
            s.mouseRelease();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException
    {
        boolean activeAndEsc = false;
        for (UIElement e : elements)
        {
            activeAndEsc = activeAndEsc || e.active() && keyCode == Keyboard.KEY_ESCAPE;
            e.keyType(typedChar, keyCode);
        }
        if (keyCode == Keyboard.KEY_ESCAPE && !activeAndEsc)
        {
            updateConfig();
            configGlobal.save();
            DaFlight.instance().updateConfig();
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    private void updateConfig()
    {
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
        config.flight3D = flight3d.boolValue();
        config.disableFov = disableFov.boolValue();

        configGlobal.serverConfigs = serverConfigs.boolValue();
    }
}
