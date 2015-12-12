package me.dags.daflight;

import org.lwjgl.input.Keyboard;

/**
 * @author dags_ <dags@dags.me>
 */

public abstract class Bind
{
    private final String bindName;
    private int id;
    private boolean toggle = true;
    private boolean tapped = false;

    public Bind(String name)
    {
        bindName = name;
    }

    public Bind setId(int i)
    {
        id = i;
        return this;
    }

    public Bind setToggle(boolean b)
    {
        toggle = b;
        return this;
    }

    public String getBindName()
    {
        return bindName;
    }

    public boolean isToggle()
    {
        return toggle;
    }

    public int getId()
    {
        return id;
    }

    public boolean active()
    {
        return toggle && keyPress() || !toggle && keyHeld();
    }

    public boolean keyPress()
    {
        if (keyHeld())
        {
            return !tapped && (tapped = true);
        }
        return tapped = false;
    }

    public abstract boolean keyHeld();

    public abstract Bind setBind(String button);

    public static class Key extends Bind
    {
        public Key(String name, String key)
        {
            super(name);
            setBind(key);
        }

        @Override
        public boolean keyHeld()
        {
            return Keyboard.isKeyDown(getId());
        }

        @Override
        public Bind setBind(String button)
        {
            setId(Keyboard.getKeyIndex(button));
            return this;
        }
    }

    public static class Mouse extends Bind
    {
        public Mouse(String name, String button)
        {
            super(name);
            setBind(button);
        }

        @Override
        public boolean keyHeld()
        {
            return org.lwjgl.input.Mouse.isButtonDown(getId());
        }

        @Override
        public Bind setBind(String button)
        {
            setId(org.lwjgl.input.Mouse.getButtonIndex(button));
            return this;
        }
    }

    public static Bind from(String name, String button, boolean toggle)
    {
        if (button.startsWith("BUTTON"))
        {
            return new Mouse(name, button).setToggle(toggle);
        }
        return new Key(name, button).setToggle(toggle);
    }
}
