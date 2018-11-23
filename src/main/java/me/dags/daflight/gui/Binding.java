package me.dags.daflight.gui;

import me.dags.daflight.MCHooks;
import org.apache.commons.lang3.StringUtils;

/**
 * @author dags_ <dags@dags.me>
 */

public class Binding implements Element<String> {

    protected int left = 0;
    protected int top = 0;
    protected int width = 0;
    protected int height = 0;

    protected boolean mouseOver = false;
    protected boolean active = false;
    private String display = "";
    protected String value = "";
    protected String defaultVal = "";

    protected Toggle toggle;

    public Binding(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Binding attach(Toggle toggle) {
        this.toggle = toggle;
        return this;
    }

    @Override
    public Binding top(int y) {
        top = y;
        return this;
    }

    @Override
    public Binding left(int x) {
        left = x;
        return this;
    }

    @Override
    public Binding setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public Binding setDisplay(String display) {
        this.display = display;
        return this;
    }

    @Override
    public Element setDefault(String value) {
        defaultVal = value;
        return this;
    }

    public boolean getToggleValue() {
        return toggle.boolValue();
    }

    @Override
    public String getDisplayString() {
        return display;
    }

    public String getText() {
        if (active) {
            return display + ":";
        }
        return display + ": " + StringUtils.capitalize(MCHooks.Input.display(value));
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(double mouseX, double mouseY) {
        mouseOver = mouseX >= left && mouseX <= left + width && mouseY >= top && mouseY <= top + height;
        MCHooks.GUI.drawRectangle(left, top, left + width, top + height, BACKGROUND_COLOR);
        String str = getText();
        int textLeft = left + (width / 2) - (MCHooks.GUI.stringWidth(str) / 2);
        MCHooks.GUI.drawString(str, textLeft, top + 1, active ? ACTIVE_COLOR : TEXT_COLOR, true);

        if (toggle != null) {
            toggle.left(left + width + 2);
            toggle.top(top);
            toggle.draw(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClick(double mouseX, double mouseY, int button) {
        if (active) {
            if (mouseOver) {
                value = MCHooks.Input.mouseName(button);
            }
            active = false;
            return true;
        } else if (mouseOver) {
            if (MCHooks.Input.isShiftDown() && defaultVal.length() > 0) {
                setValue(defaultVal);
            } else {
                active = true;
            }
            return true;
        }
        return toggle != null && toggle.mouseClick(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseRelease() {
        return toggle != null && toggle.mouseRelease();
    }

    @Override
    public boolean keyType(char character, int id) {
        if (active) {
            active = false;

            if (id == MCHooks.Input.escape()) {
                return true;
            } else if (id == MCHooks.Input.backspace()) {
                value = "";
            } else {
                value = MCHooks.Input.keyboardName(id);
            }
            return true;
        }
        return false;
    }

    @Override
    public String get() {
        return value;
    }
}