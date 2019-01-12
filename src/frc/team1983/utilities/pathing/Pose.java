package frc.team1983.utilities.pathing;

import frc.team1983.utilities.math.Vector2;

public class Pose
{
    private Vector2 position, direction;

    public Pose(Vector2 position, Vector2 direction)
    {
        this.position = position;
        this.direction = direction;
    }

    public Pose(Vector2 position, double degrees)
    {
        this(position, new Vector2(Math.cos(Math.toRadians(degrees)), Math.sin(Math.toRadians(degrees))));
    }
}
