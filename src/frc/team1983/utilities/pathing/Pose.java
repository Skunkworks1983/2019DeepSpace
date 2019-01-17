package frc.team1983.utilities.pathing;

import frc.team1983.utilities.math.Vector2;

public class Pose
{
    private final Vector2 position, direction;
    private final double heading;

    public Pose(Vector2 position, Vector2 direction)
    {
        this.position = position;
        this.direction = direction;
        this.heading = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
    }

    public Pose(Vector2 position, double degrees)
    {
        this(position, new Vector2(Math.cos(Math.toRadians(degrees)), Math.sin(Math.toRadians(degrees))));
    }

    public Pose(double x, double y, double degrees)
    {
        this(new Vector2(x, y), degrees);
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    public double getHeading()
    {
        return heading;
    }
}
