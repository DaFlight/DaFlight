package me.dags.daflight.util;

/**
 * @author dags <dags@dags.me>
 */
public class Vector3d {

    private double x, y, z;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void mult(double factor) {
        mult(factor, factor, factor);
    }

    public void mult(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    public void norm() {
        double length = length();
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
    }
}
