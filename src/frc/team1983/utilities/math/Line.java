package frc.team1983.utilities.math;

import frc.team1983.constants.Constants;

public class Line
{
    private Vector2 origin;
    private Vector2 direction;

    public Line(Vector2 origin, Vector2 direction)
    {
        this.origin = origin;
        this.direction = direction.getNormalized();
    }

    public Line(double originX, double originY, double directionX, double directionY)
    {
        this(new Vector2(originX, originY), new Vector2(directionX, directionY));
    }

    public Vector2 getOrigin()
    {
        return origin;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    /**
     * Finds the intersection point of two lines
     * @param left first line
     * @param right second line
     * @return intersection
     */
    public static Vector2 cast(Line left, Line right)
    {
        if(Math.abs(Vector2.dot(left.direction, right.direction) - 1) < Constants.EPSILON)
            return null;

        double t1 = (right.origin.getY() - left.origin.getY()) * right.direction.getX() - (right.origin.getX() - left.origin.getX()) * right.direction.getY();
        t1 /= right.direction.getX() * left.direction.getY() - right.direction.getY() * left.direction.getX();

        return Vector2.add(left.origin, Vector2.scale(left.direction, t1));
    }

    /**
     * Finds the intersection point of this line and another
     * @param other other line
     * @return intersection
     */
    public Vector2 cast(Line other)
    {
        return cast(this, other);
    }

    /**
     * Find the closest point on a line that another point is closest to
     * @param line
     * @param point other point
     * @return closest point
     */
    public static Vector2 closest(Line line, Vector2 point)
    {
        return cast(new Line(point, line.getDirection().getRight()), line);
    }

    /**
     * Find the closest point on this line that another point is closest to
     * @param point other point
     * @return closest point
     */
    public Vector2 closest(Vector2 point)
    {
        return Line.closest(this, point);
    }
}
