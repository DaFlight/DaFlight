package me.dags.daflight.gui;

import me.dags.daflight.MCHooks;

/**
 * @author dags_ <dags@dags.me>
 */

public class UILabel implements UIElement {

    private final int height;
    private final int color;

    private String label = "";
    private int left = 0;
    private int top = 0;

    public UILabel(int height, int color) {
        this.height = height;
        this.color = color;
    }

    @Override
    public UIElement top(int y) {
        top = y;
        return this;
    }

    @Override
    public UIElement left(int x) {
        left = x;
        return this;
    }

    @Override
    public UIElement setValue(Object value) {
        return this;
    }

    @Override
    public UIElement setDisplay(String display) {
        label = display;
        return this;
    }

    @Override
    public UIElement setDefault(Object value) {
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
    public void draw(int mouseX, int mouseY) {
        MCHooks.GUI.drawString(label, left, top, color);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int button) {

    }

    @Override
    public void mouseRelease() {

    }

    @Override
    public void keyType(char character, int id) {

    }

    @Override
    public Object get() {
        return null;
    }
}
