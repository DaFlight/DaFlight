package me.dags.daflight.gui;

/**
 * @author dags_ <dags@dags.me>
 */

public class Toggle extends Binding {

    private boolean boolValue = false;
    private String on;
    private String off;

    public Toggle(int width, int height, String on, String off) {
        super(width, height);
        this.on = on;
        this.off = off;
    }

    public Toggle left(int left) {
        super.left(left);
        return this;
    }

    @Override
    public String getText() {
        return boolValue ? on : off;
    }

    public Toggle set(boolean value) {
        this.boolValue = value;
        return this;
    }

    public Toggle attach(Toggle toggle) {
        super.attach(toggle);
        return this;
    }

    @Override
    public boolean mouseClick(double mouseX, double mouseY, int button) {
        if (mouseOver) {
            boolValue = !boolValue;
            super.value = boolValue + "";
            active = true;
            return true;
        }
        return super.toggle != null && super.toggle.mouseClick(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseRelease() {
        active = false;
        return super.mouseRelease();
    }

    @Override
    public boolean keyType(char character, int id) {
        return false;
    }

    public boolean boolValue() {
        return boolValue;
    }
}