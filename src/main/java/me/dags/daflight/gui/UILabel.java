package me.dags.daflight.gui;

import net.minecraft.client.Minecraft;

/**
 * @author dags_ <dags@dags.me>
 */

public class UILabel implements me.dags.daflight.gui.UIElement
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
    public me.dags.daflight.gui.UIElement top(int y)
    {
        top = y;
        return this;
    }

    @Override
    public me.dags.daflight.gui.UIElement left(int x)
    {
        left = x;
        return this;
    }

    @Override
    public me.dags.daflight.gui.UIElement setValue(Object value)
    {
        return this;
    }

    @Override
    public me.dags.daflight.gui.UIElement setDisplay(String display)
    {
        labal = display;
        return this;
    }

    @Override
    public UIElement setDefault(Object value)
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
    public Object get()
    {
        return null;
    }
}
