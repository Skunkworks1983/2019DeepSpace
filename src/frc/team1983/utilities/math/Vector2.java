package frc.team1983.utilities.math;

import frc.team1983.constants.Constants;

public class Vector2
{
    public static final Vector2 ZERO = new Vector2(0, 0);

    private double x, y;

    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString(boolean csv)
    {
        // Multiplying and dividing by 100 to round to two decimal places
        String xStr = Double.toString(Math.round(x * 100.0) / 100.0);
        String yStr = Double.toString(Math.round(y * 100.0) / 100.0);

        // Print with or without angle brackets for csv debugging
        if (csv)
            return xStr + ", " + yStr;
        else
            return "<" + xStr + ", " + yStr + ">";
    }

    public String toString()
    {
        return toString(false);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void set(double x, double y)
    {
        setX(x);
        setY(y);
    }

    public void set(Vector2 v)
    {
        set(v.x, v.y);
    }

    public static Vector2 copy(Vector2 vector)
    {
        return new Vector2(vector.x, vector.y);
    }

    public Vector2 copy()
    {
        return copy(this);
    }

    public double getMagnitude()
    {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 getNormalized()
    {
        return Vector2.scale(this, 1.0 / getMagnitude());
    }

    public void normalize()
    {
        set(getNormalized());
    }

    public Vector2 getNegative()
    {
        return new Vector2(-x, -y);
    }

    public static double getDistance(Vector2 left, Vector2 right)
    {
        return Math.sqrt(Math.pow(left.x - right.x, 2) + Math.pow(left.y - right.y, 2));
    }

    public double getDistanceTo(Vector2 other)
    {
        return Vector2.getDistance(this, other);
    }

    public static boolean equals(Vector2 left, Vector2 right)
    {
        return Math.abs(left.x - right.x) < Constants.EPSILON && Math.abs(left.y - right.y) < Constants.EPSILON;
    }

    public boolean equals(Vector2 other)
    {
        return Vector2.equals(this, other);
    }

    public static Vector2 add(Vector2 left, Vector2 right)
    {
        return new Vector2(left.x + right.x, left.y + right.y);
    }

    public void add(Vector2 right)
    {
        set(Vector2.add(this, right));
    }

    public static Vector2 sub(Vector2 left, Vector2 right)
    {
        return new Vector2(left.x - right.x, left.y - right.y);
    }

    public void sub(Vector2 right)
    {
        set(Vector2.sub(this, right));
    }

    public static Vector2 scale(Vector2 left, double right)
    {
        return new Vector2(left.x * right, left.y * right);
    }

    public void scale(double scalar)
    {
        set(Vector2.scale(this, scalar));
    }

    public static double dot(Vector2 left, Vector2 right)
    {
        return left.x * right.x + left.y * right.y;
    }

    public static Vector2 twist(Vector2 point, Vector2 center, double degrees)
    {
        degrees *= Math.PI / 180;
        double r = Vector2.getDistance(point, center);
        double theta = Math.atan2(point.y - center.y, point.x - center.x);
        return Vector2.add(center, Vector2.scale(new Vector2(Math.cos(theta + degrees), Math.sin(theta + degrees)), r));
    }

    public static Vector2 rotate(Vector2 point, double degrees)
    {
        return twist(point, new Vector2(0, 0), degrees);
    }

    public void twist(Vector2 center, double degrees)
    {
        set(Vector2.twist(this, center, degrees));
    }

    public static Vector2 findCenter(Vector2... points)
    {
        Vector2 center = points[0].copy();
        for(int i = 1; i < points.length; i++)
            center.add(points[i]);
        center.scale(1.0 / points.length);
        return center;
    }
}