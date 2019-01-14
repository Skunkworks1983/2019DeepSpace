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

    public Vector2 getOrigin()
    {
        return origin;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    public static Vector2 cast(Line left, Line right)
    {
        if(Math.abs(Vector2.dot(left.direction, right.direction) - 1) < Constants.EPSILON)
            return null;

        double t1 = (right.origin.getY() - left.origin.getY()) * right.direction.getX() - (right.origin.getX() - left.origin.getX()) * right.direction.getY();
        t1 /= right.direction.getX() * left.direction.getY() - right.direction.getY() * left.direction.getX();

        return Vector2.add(left.origin, Vector2.scale(left.direction, t1));
    }

    public Vector2 cast(Line other)
    {
        return cast(this, other);
    }
}
