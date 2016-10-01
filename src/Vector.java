

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class Vector {

    private float x, y;

    public static final Vector zero = new Vector(0, 0);
    public static final Vector up = new Vector(0, -1);
    public static final Vector down = new Vector(0, 1);
    public static final Vector left = new Vector(-1, 0);
    public static final Vector right = new Vector(1, 0);

    public Vector clone() {
        return new Vector(x, y);
    }

    public Vector() {
        x = 0;
        y = 0;
    }

    public Vector(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Component c) {
        this.x = c.getWidth();
        this.y = c.getHeight();
    }

    public Vector(Dimension d) {
        this.x = (float) d.getWidth();
        this.y = (float) d.getHeight();
    }

    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void add(Vector a) {
        x += a.x;
        y += a.y;
    }

    public void sub(Vector a) {
        x -= a.x;
        y -= a.y;
    }

    public void scale(float a) {
        x *= a;
        y *= a;
    }

    public void scale(Vector a) {
        x *= a.x;
        y *= a.y;
    }

    public void addScaled(Vector v, float a) {
        add(scale(v, a));
    }

    public void normalize() {
        float a = mag();
        if (a != 0) {
            scale(1 / mag());
        }
    }

    public void round() {
        x = getXi();
        y = getYi();
    }

    public void invert() {
        if (x != 0) {
            x = 1 / x;
        }
        if (y != 0) {
            y = 1 / y;
        }
    }

    public Vector roundI() {
        return new Vector(getXi(), getYi());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getXi2() {
        return (int) Math.round(x);
    }

    public int getXi() {
        return (int) Math.floor(x + 0.5f);
    }

    public int getXi3() {
        return (int) (x + 0.5);
    }

    public void smooth() {
        x = getXi();
        y = getYi();
    }

    public int getYi() {
        return (int) Math.floor(y + 0.5f);
    }

    public int getYi2() {
        return (int) Math.round(y);
    }

    public int getYi3() {
        return (int) (y + 0.5);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void negate() {
        x = -x;
        y = -y;
    }

    public Vector addi(Vector a) {
        return Vector.add(this, a);
    }

    public Vector subi(Vector a) {
        return Vector.sub(this, a);
    }

    public Vector scalei(Vector a) {
        return Vector.scale(this, a);
    }

    public Vector scalei(float a) {
        return Vector.scale(this, a);
    }

    public Vector addScaledi(Vector v, float a) {
        return Vector.addScaled(this, v, a);
    }

    public Vector inverti() {
        return Vector.invert(this);
    }

    public Vector normalizei() {
        return Vector.normal(this);
    }

    public Vector negatei() {
        return Vector.negate(this);
    }

    public float magSqr() {
        return x * x + y * y;
    }

    public boolean equals(Vector a) {
        return (x == a.x) && (y == a.y);
    }

    public boolean roughEquals(Vector a) {
        return (((x - a.x) * (x - a.x)) + ((y - a.y) * (y - a.y))) < 0.01;
    }

    public float dot(Vector a) {
        return x * a.x + y * a.y;
    }

    public float mag() {
        return (float) Math.sqrt(magSqr());
    }

    public String toString() {
        return "Vector[x = " + x + ", y = " + y + "]";
    }

    public static Vector invert(Vector a) {
        if (a.x == 0 && a.y == 0) {
            return new Vector(0, 0);
        }
        if (a.x == 0) {
            return new Vector(1 / a.x, 0);
        }
        if (a.y == 0) {
            return new Vector(0, 1 / a.y);
        }
        return new Vector(1 / a.x, 1 / a.y);
    }

    public static Vector cross(Vector a, Vector b) {
        return new Vector(-(a.y - b.y), a.x - b.x);
    }

    public Vector crossi(Vector v) {
        return new Vector(-(y - v.y), x - v.x);
    }

    public void cross(Vector v) {
        float xx = -(y - v.y);
        float yy = (x - v.x);

        x = xx;
        y = yy;
    }

    public static boolean equals(Vector a, Vector b) {
        return (a.x == b.x) && (a.y == b.y);
    }

    public static boolean roughEquals(Vector a, Vector b) {
        return ((a.x - b.x) + (a.y - b.y)) * ((a.x - b.x) + (a.y - b.y)) < 0.01;
    }

    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y);
    }

    public static Vector sub(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y);
    }

    public static Vector scale(Vector a, float b) {
        return new Vector(a.x * b, a.y * b);
    }

    public static Vector scale(Vector a, Vector b) {
        return new Vector(a.x * b.x, a.y * b.y);
    }

    public static Vector addScaled(Vector a, Vector b, float c) {
        return add(a, scale(b, c));
    }

    public static Vector negate(Vector a) {
        return new Vector(-a.x, -a.y);
    }

    public static float magSqr(Vector a) {
        return a.x * a.x + a.y * a.y;
    }

    public static float dot(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y;
    }

    public static float mag(Vector a) {
        return (float) Math.sqrt(magSqr(a));
    }

    public static Vector normal(Vector a) {
        float m = mag(a);
        if (m == 0) {
            return new Vector(a);
        }
        return new Vector(scale(a, 1 / m));
    }

    public static Vector getDirectionVector(Vector v) {
        if (v.equals(new Vector(0.0f, 0.0f))) {
            return null;
        }

        float up = v.dot(Vector.up);
        float down = v.dot(Vector.down);
        float left = v.dot(Vector.left);
        float right = v.dot(Vector.right);
        if (up > down && up > left && up > right) {
            return Vector.up;
        } else if (down > left && down > right) {
            return Vector.down;
        } else if (left > right) {
            return Vector.left;
        } else {
            return Vector.right;
        }
    }

    public Vector(BufferedImage i) {
        x = i.getWidth();
        y = i.getHeight();
    }
}
