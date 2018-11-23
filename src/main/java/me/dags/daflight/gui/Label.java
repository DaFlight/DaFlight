package me.dags.daflight.gui;

import me.dags.daflight.MCHooks;

/**
 * @author dags_ <dags@dags.me>
 */

public class Label implements Element {

    private final int height;
    private final int color;

    private String label = "";
    private int left = 0;
    private int top = 0;

    public Label(int height, int color) {
        this.height = height;
        this.color = color;
    }

    @Override
    public Element top(int y) {
        top = y;
        return this;
    }

    @Override
    public Element left(int x) {
        left = x;
        return this;
    }

    @Override
    public Element setValue(Object value) {
        return this;
    }

    @Override
    public Element setDisplay(String display) {
        label = display;
        return this;
    }

    @Override
    public Element setDefault(Object value) {
        return this;
    }

    @Override
    public String getDisplayString() {
        return label;
    }

    @Override
    public boolean active() {
        return false;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(double mouseX, double mouseY) {
        MCHooks.GUI.drawString(label, left, top, color);
    }

    @Override
    public boolean mouseClick(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseRelease() {
        return false;
    }

    @Override
    public boolean keyType(char character, int id) {
        return false;
    }

    @Override
    public Object get() {
        return null;
    }
}
