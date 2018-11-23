package me.dags.daflight.gui;

/**
 * @author dags_ <dags@dags.me>
 */

interface Element<T> {

    int BACKGROUND_COLOR = 0x99999999;
    int TEXT_COLOR = 0xFFFFFFFF;
    int ACTIVE_COLOR = 0xFFFFFFB3;

    Element top(int y);

    Element left(int x);

    Element setValue(T value);

    Element setDisplay(String display);

    Element setDefault(T value);

    String getDisplayString();

    boolean active();

    int getHeight();

    void draw(double mouseX, double mouseY);

    boolean mouseClick(double mouseX, double mouseY, int button);

    boolean mouseRelease();

    boolean keyType(char character, int id);

    T get();
}
