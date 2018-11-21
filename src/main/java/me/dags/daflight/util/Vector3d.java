package me.dags.daflight.util;

/**
 * @author dags <dags@dags.me>
 */
public class Vector3d {

    private double x, y, z;

    public Vector3d update(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector3d add(double x, double y, double z) {
        x += x;
        y += y;
        z += z;
        return this;
    }

    public Vector3d mult(double x, double y, double z) {
        x *= x;
        y *= y;
        z *= z;
        return this;
    }

	public Vector3d mult(double s) {
		x *= s;
		y *= s;
		z *= s;
		return this;
	}

	public Vector3d normalize() {
		if (this.length() == 0) {
			return this;
		}
		x /= this.length();
		y /= this.length();
		z /= this.length();
		return this;
	}

	public double length() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
}
