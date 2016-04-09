package me.dags.daflight.gui;

import net.minecraft.client.Minecraft;

/**
 * @author dags_ <dags@dags.me>
 */

public class UILabel implements UIElement<String>
{
    private final int height;
    private final int color;

    private String labal = "";
    private int left = 0;
    private int top = 0;

    public UILabel(int height, int color)
    {
        this.height = height;
        this.color = color;
    }

    @Override
    public UIElement<String> top(int y)
    {
        top = y;
        return this;
    }

    @Override
    public UIElement<String> left(int x)
    {
        left = x;
        return this;
    }

    @Override
    public UIElement<String> setValue(String value)
    {
        return this;
    }

    @Override
    public UIElement<String> setDisplay(String display)
    {
        labal = display;
        return this;
    }

    @Override
    public UIElement<String> setDefault(String value)
    {
        return this;
    }

    @Override
    public String getDisplayString()
    {
        return labal;
    }

    @Override
    public boolean active()
    {
        return false;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public void draw(int mouseX, int mouseY)
    {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(labal, left, top, color);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int button)
    {

    }

    @Override
    public void mouseRelease()
    {

    }

    @Override
    public void keyType(char character, int id)
    {

    }

    @Override
    public String get()
    {
        return null;
    }
}
