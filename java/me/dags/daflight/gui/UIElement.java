package me.dags.daflight.gui;

/**
 * @author dags_ <dags@dags.me>
 */

public interface UIElement<T>
{
    public static final int BACKGROUND_COLOR = 0x99999999;
    public static final int TEXT_COLOR = 0xFFFFFFFF;
    public static final int ACTIVE_COLOR = 0xFFFFFFB3;

    public me.dags.daflight.gui.UIElement top(int y);

    public me.dags.daflight.gui.UIElement left(int x);

    public me.dags.daflight.gui.UIElement setValue(T value);

    public me.dags.daflight.gui.UIElement setDisplay(String display);

    public me.dags.daflight.gui.UIElement setDefault(T value);

    public String getDisplayString();

    public boolean active();

    public int getHeight();

    public void draw(int mouseX, int mouseY);

    public void mouseClick(int mouseX, int mouseY, int button);

    public void mouseRelease();

    public void keyType(char character, int id);

    public T get();
}
