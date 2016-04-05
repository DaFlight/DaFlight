package me.dags.daflight.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;

/**
 * @author dags_ <dags@dags.me>
 */

public class UISlider implements UIElement<Float>
{
    private static final DecimalFormat decimal2dp = new DecimalFormat("0.00");

    private String displayString = "";

    protected int left = 0;
    protected int top = 0;
    protected int width = 0;
    protected int height = 0;
    protected int middle = 0;
    protected int topMargin = 0;

    private float max = 0F;
    private float min = 0F;
    private float defaultVal = 0F;

    private int sliderPos = 0;
    private int sliderHalfWidth = 0;

    private boolean active = false;
    private boolean hovered = false;

    public UISlider(int width, int height, float max, float min, int sliderWidth, int topMargin)
    {
        this.width = width;
        this.height = height;
        this.max = max;
        this.min = min;
        this.sliderHalfWidth = sliderWidth / 2;
        this.topMargin = topMargin;
    }

    @Override
    public me.dags.daflight.gui.UISlider left(int left)
    {
        this.left = left;
        this.middle = left + (width / 2);
        return this;
    }

    @Override
    public me.dags.daflight.gui.UISlider top(int top)
    {
        this.top = top;
        return this;
    }

    @Override
    public me.dags.daflight.gui.UISlider setValue(Float n)
    {
        float perc = percFromVal(n);
        int relPos = (int) (width * perc);
        sliderPos = left + relPos;
        return this;
    }

    @Override
    public me.dags.daflight.gui.UISlider setDisplay(String s)
    {
        displayString = s;
        return this;
    }

    @Override
    public UIElement setDefault(Float value)
    {
        defaultVal = value;
        return this;
    }

    public final float percFromVal(float val)
    {
        val = (val < min ? min : val > max ? max : val);
        return (val - min) / (max - min);
    }

    @Override
    public Float get()
    {
        return min + ((max - min) * ((float) (sliderPos - left) / width));
    }

    @Override
    public final String getDisplayString()
    {
        return displayString;
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
        hovered = mouseX >= left && mouseX <= left + width && mouseY >= top && mouseY <= top + height;

        Gui.drawRect(left, top, left + width, top + height, BACKGROUND_COLOR);

        if (active)
        {
            sliderPos = mouseX > left + width ? left + width : mouseX < left ? left : mouseX;
        }
        Gui.drawRect(sliderPos - sliderHalfWidth, top, sliderPos + sliderHalfWidth, top + height, TEXT_COLOR);

        String val = displayString + ": " + decimal2dp.format(round2dp(this.get()));
        int valLeft = left + (width / 2) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(val) / 2);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(val, valLeft, top + topMargin, hovered ? ACTIVE_COLOR : TEXT_COLOR);
    }

    @Override
    public void mouseClick(int x, int y, int button)
    {
        if (hovered && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            setValue(defaultVal);
        }
        else
        {
            active = x >= sliderPos - sliderHalfWidth && x <= sliderPos + sliderHalfWidth && y >= top && y <= top + height;
            if (!active && hovered)
            {
                sliderPos = x > left + width ? left + width : x < left ? left : x;
            }
        }
    }

    @Override
    public void mouseRelease()
    {
        active = false;
    }

    @Override
    public void keyType(char character, int id)
    {

    }

    public static float round2dp(float f)
    {
        f *= 10000F;
        return (f > 0F ? (long) (f + 0.5F) : (long) (f - 0.5F)) / 10000F;
    }
}
