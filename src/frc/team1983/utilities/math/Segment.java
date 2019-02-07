package frc.team1983.utilities.math;

public class Segment extends Bezier
{
    public Segment(Vector2 point0, Vector2 point1)
    {
        super(point0, point1);
    }

    public Vector2 getPoint0()
    {
        return points[0];
    }

    public Vector2 getPoint1()
    {
        return points[1];
    }
}
