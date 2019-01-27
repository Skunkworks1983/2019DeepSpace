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

    /**
     * Returns a copied instance of a vector
     * @return a copied instance of the vector
     */
    public static Vector2 copy(Vector2 vector)
    {
        return new Vector2(vector.x, vector.y);
    }

    /**
     * Returns a copied instance of this vector
     * @return a copied instance of this vector
     */
    public Vector2 copy()
    {
        return copy(this);
    }

    /**
     * Evaluates the magnitude (or length) of this vector
     * @return the magnitude
     */
    public double getMagnitude()
    {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Evaluates the normalized version of this vector
     * @return the normalized vector
     */
    public Vector2 getNormalized()
    {
        return Vector2.scale(this, 1.0 / getMagnitude());
    }

    /**
     * Scales the components of this vector such that the magnitude is equal to 1
     */
    public void normalize()
    {
        set(getNormalized());
    }

    /**
     * Swaps the signs of each component of this vector
     * @return the negative vector
     */
    public Vector2 getNegative()
    {
        return new Vector2(-x, -y);
    }

    /**
     * Evaluates the distance between two vectors
     * @param vector1
     * @param vector2
     * @return the distance
     */
    public static double getDistance(Vector2 vector1, Vector2 vector2)
    {
        return Math.sqrt(Math.pow(vector1.x - vector2.x, 2) + Math.pow(vector1.y - vector2.y, 2));
    }

    /**
     * Evaluates the distance between this vector and another
     * @param other
     * @return the distance
     */
    public double getDistanceTo(Vector2 other)
    {
        return Vector2.getDistance(this, other);
    }

    /**
     * Checks equality between two vectors and another by checking if the difference between each component is less than Constants.EPSILON
     * @param vector1
     * @param vector2
     * @return whether or not the vectors are equal
     */
    public static boolean equals(Vector2 vector1, Vector2 vector2)
    {
        return Math.abs(vector1.x - vector2.x) < Constants.EPSILON && Math.abs(vector1.y - vector2.y) < Constants.EPSILON;
    }

    /**
     * Checks equality between this vector and another by checking if the difference between each component is less than Constants.EPSILON
     * @param other
     * @return whether or not the vectors are equal
     */
    public boolean equals(Vector2 other)
    {
        return Vector2.equals(this, other);
    }

    /**
     * Adds two vectors
     * @param left
     * @param right
     * @returns the sum of the two vectors
     */
    public static Vector2 add(Vector2 left, Vector2 right)
    {
        return new Vector2(left.x + right.x, left.y + right.y);
    }

    /**
     * Adds another vector to this vector
     * @param other
     */
    public void add(Vector2 other)
    {
        set(Vector2.add(this, other));
    }

    /**
     * Subtract two vectors
     * @param left
     * @param right
     * @return the difference between the two vectors
     */
    public static Vector2 sub(Vector2 left, Vector2 right)
    {
        return new Vector2(left.x - right.x, left.y - right.y);
    }

    /**
     * Subtract this vector by another
     * @param other the vector to subtract by
     */
    public void sub(Vector2 other)
    {
        set(Vector2.sub(this, other));
    }

    /**
     * Multiplies each component of a vector by a scalar
     * @param scalar
     */
    public static Vector2 scale(Vector2 vector, double scalar)
    {
        return new Vector2(vector.x * scalar, vector.y * scalar);
    }

    /**
     * Multiplies each component of this vector by a scalar
     * @param scalar
     */
    public void scale(double scalar)
    {
        set(Vector2.scale(this, scalar));
    }

    /**
     * @param vector1
     * @param vector2
     * @return the dot product of the two vectors
     */
    public static double dot(Vector2 vector1, Vector2 vector2)
    {
        return vector1.x * vector2.x + vector1.y * vector2.y;
    }

    /**
     * Rotates a point around a given center point by a number of degrees
     * @param point the point to rotate
     * @param center the point to rotate around
     * @param degrees the number of degrees to rotate. Positive values are counter-clockwise, and negative values are clockwise
     * @return the twisted point
     */
    public static Vector2 twist(Vector2 point, Vector2 center, double degrees)
    {
        degrees = Math.toRadians(degrees);
        double r = Vector2.getDistance(point, center);
        double theta = Math.atan2(point.y - center.y, point.x - center.x);
        return Vector2.add(center, Vector2.scale(new Vector2(Math.cos(theta + degrees), Math.sin(theta + degrees)), r));
    }

    /**
     * Rotates a point around the origin by a number of degrees
     * @param point the point to rotate
     * @param degrees the number of degrees to rotate. Positive values are counter-clockwise, and negative values are clockwise
     * @return the rotated point
     */
    public static Vector2 rotate(Vector2 point, double degrees)
    {
        return twist(point, ZERO, degrees);
    }

    /**
     * Rotates this point around a center point by a number of degrees
     * @param center the point to orbit around
     * @param degrees the difference in rotation. Positive values are counter-clockwise, and negative values are clockwise
     */
    public void twist(Vector2 center, double degrees)
    {
        set(Vector2.twist(this, center, degrees));
    }

    /**
     * Finds the average position of a list of Vectors
     * @param points a list of points
     * @return the average position
     */
    public static Vector2 findCenter(Vector2... points)
    {
        Vector2 center = points[0].copy();
        for(int i = 1; i < points.length; i++)
            center.add(points[i]);
        center.scale(1.0 / points.length);
        return center;
    }
}