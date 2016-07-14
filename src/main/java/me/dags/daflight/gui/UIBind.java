package me.dags.daflight.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @author dags_ <dags@dags.me>
 */

public class UIBind implements UIElement<String>
{
    protected int left = 0;
    protected int top = 0;
    protected int width = 0;
    protected int height = 0;

    protected boolean mouseOver = false;
    protected boolean active = false;
    private String display = "";
    protected String value = "";
    protected String defaultVal = "";

    protected UIToggle toggle;

    public UIBind(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public UIBind attach(UIToggle toggle)
    {
        this.toggle = toggle;
        return this;
    }

    @Override
    public UIBind top(int y)
    {
        top = y;
        return this;
    }

    @Override
    public UIBind left(int x)
    {
        left = x;
        return this;
    }

    @Override
    public UIBind setValue(String value)
    {
        this.value = value;
        return this;
    }

    @Override
    public UIBind setDisplay(String display)
    {
        this.display = display;
        return this;
    }

    @Override
    public UIElement setDefault(String value)
    {
        defaultVal = value;
        return this;
    }

    public boolean getToggleValue()
    {
        return toggle.boolValue();
    }

    @Override
    public String getDisplayString()
    {
        return display;
    }

    public String getText()
    {
        return active ? display + ":" : display + ": " + value;
    }

    @Override
    public boolean active()
    {
        return active;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public void draw(int mouseX, int mouseY)
    {
        mouseOver = mouseX >= left && mouseX <= left + width && mouseY >= top && mouseY <= top + height;
        Gui.drawRect(left, top, left + width, top + height, BACKGROUND_COLOR);
        String str = getText();
        int textLeft = left + (width / 2) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(str) / 2);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(str, textLeft, top + 1, active ? ACTIVE_COLOR : TEXT_COLOR);

        if (toggle != null)
        {
            toggle.left(left + width + 2);
            toggle.top(top);
            toggle.draw(mouseX, mouseY);
        }
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int button)
    {
        if (active)
        {
            if (mouseOver)
            {
                value = Mouse.getButtonName(button);
            }
            active = false;
        }
        else if (mouseOver)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && defaultVal.length() > 0)
            {
                setValue(defaultVal);
            }
            else
            {
                active = true;
            }
        }
        if (toggle != null)
        {
            toggle.mouseClick(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseRelease()
    {
        if (toggle != null)
        {
            toggle.mouseRelease();
        }
    }

    @Override
    public void keyType(char character, int id)
    {
        if (active)
        {
            if (id == Keyboard.KEY_BACK)
            {
                value = "";
            }
            else if (id != Keyboard.KEY_ESCAPE)
            {
                value = Keyboard.getKeyName(id);
            }
            active = false;
        }
    }

    @Override
    public String get()
    {
        return value;
    }
}
