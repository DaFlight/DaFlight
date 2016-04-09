package me.dags.daflight.util;

/**
 * @author dags <dags@dags.me>
 */
public class Vector3d
{
    private double x, y, z;

    public Vector3d update(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public double getX()
    {
       return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public Vector3d add(double x, double y, double z)
    {
        x += x;
        y += y;
        z += z;
        return this;
    }

    public Vector3d mult(double x, double y, double z)
    {
        x *= x;
        y *= y;
        z *= z;
        return this;
    }
}
