package me.dags.daflight.util;

/**
 * @author dags <dags@dags.me>
 */
public class Rotation
{
    private float pitch, yaw;

    public Rotation update(float pitch, float yaw)
    {
        this.pitch = pitch;
        this.yaw = yaw;
        return this;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getYaw()
    {
        return yaw;
    }
}
