package me.dags.daflight.gui;

/**
 * @author dags_ <dags@dags.me>
 */

interface UIElement<T> {

    int BACKGROUND_COLOR = 0x99999999;
    int TEXT_COLOR = 0xFFFFFFFF;
    int ACTIVE_COLOR = 0xFFFFFFB3;

    UIElement top(int y);

    UIElement left(int x);

    UIElement setValue(T value);

    UIElement setDisplay(String display);

    UIElement setDefault(T value);

    String getDisplayString();

    boolean active();

    int getHeight();

    void draw(int mouseX, int mouseY);

    void mouseClick(int mouseX, int mouseY, int button);

    void mouseRelease();

    void keyType(char character, int id);

    T get();
}
