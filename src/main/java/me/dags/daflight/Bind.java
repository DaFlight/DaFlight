package me.dags.daflight;

/**
 * @author dags_ <dags@dags.me>
 */

public class Bind {

    private final String bindName;
    private int id;
    private boolean toggle = true;
    private boolean tapped = false;

    public Bind(String name) {
        bindName = name;
    }

    public Bind setId(int i) {
        id = i;
        return this;
    }

    public Bind setToggle(boolean b) {
        toggle = b;
        return this;
    }

    public String getBindName() {
        return bindName;
    }

    public boolean isToggle() {
        return toggle;
    }

    public int getId() {
        return id;
    }

    public boolean active() {
        return toggle && keyPress() || !toggle && keyHeld();
    }

    public boolean keyPress() {
        if (keyHeld()) {
            return !tapped && (tapped = true);
        }
        return tapped = false;
    }

    public boolean keyHeld() {
        return MCHooks.Input.isDown(getId());
    }

    public Bind setBind(String button) {
        setId(MCHooks.Input.inputId(button));
        return this;
    }

    public static Bind from(String name, String button, boolean toggle) {
        return new Bind(name).setBind(button).setToggle(toggle);
    }
}
