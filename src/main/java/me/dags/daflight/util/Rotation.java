package me.dags.daflight.util;

/**
 * @author dags <dags@dags.me>
 */
public class Rotation {

    private float pitch, yaw;

    public void set(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
