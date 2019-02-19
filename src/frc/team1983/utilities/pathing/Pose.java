package frc.team1983.utilities.pathing;

import frc.team1983.constants.Constants;
import frc.team1983.utilities.math.Vector2;

/**
 * Pose is a position-heading pair that represents the robot on the field.
 * The coordinate plane's origin is at the far left of whichever alliance wall the robot is at.
 * X values increase moving to the right, and Y values increase moving away.
 * All heading measurements are in degrees.
 * Zero degrees starts in the X coordinate direction and increases counter-clockwise,
 * 90 degrees facing the opposite alliance wall.
 */
public class Pose
{
    public static final Pose ORIGIN = new Pose(0, 0, 90);

    static double L = Constants.ROBOT_LENGTH;

    // Rockets
    static double dx = L / 2.0 * Math.cos(Math.toRadians(61.25));
    static double dy = L / 2.0 * Math.sin(Math.toRadians(61.25));

    public static final Pose LEFT_ROCKET_CLOSE = new Pose(1 + (6.5 / 12.0) + dx, 17 + (8.5 / 12.0) - dy, 118.75);
    public static final Pose LEFT_ROCKET_MIDDLE = new Pose(2 + (3.5 / 12.0) + L / 2.0, 19, 180);
    public static final Pose LEFT_ROCKET_FAR = new Pose(1 + (6.5 / 12.0) + dx, 20 + (3.5 / 12.0) + dy, -118.75);

    public static final Pose RIGHT_ROCKET_CLOSE = new Pose(25 + (5.5 / 12.0) - dx, 17 + (8.5 / 12.0) - dy, 61.25);
    public static final Pose RIGHT_ROCKET_MIDDLE = new Pose(24 + (8.5 / 12.0) - L / 2.0, 19, 0);
    public static final Pose RIGHT_ROCKET_FAR = new Pose(25 + (5.5 / 12.0) - dx, 20 + (3.5 / 12.0) + dy, -61.25);

    // Cargo ship
    public static final Pose CARGO_SHIP_LEFT_CLOSE = new Pose(11 + (7.25 / 12.0) - (5.125 / 12.0) - L / 2.0, 25 + (7.5 / 12.0), 0);
    public static final Pose CARGO_SHIP_LEFT_MIDDLE = new Pose(11 + (7.25 / 12.0) - (5.125 / 12.0) - L / 2.0, 23 + (7.375 / 12.0), 0);
    public static final Pose CARGO_SHIP_LEFT_FAR = new Pose(11 + (7.25 / 12.0) - (5.125 / 12.0) - L / 2.0, 21 + (11.25 / 12.0), 0);
    public static final Pose CARGO_SHIP_RIGHT_CLOSE = new Pose(15 + (4.75 / 12.0) + (5.125 / 12.0) + L / 2.0, 25 + (7.5 / 12.0), 180);
    public static final Pose CARGO_SHIP_RIGHT_MIDDLE = new Pose(15 + (4.75 / 12.0) + (5.125 / 12.0) + L / 2.0, 23 + (7.375 / 12.0), 180);
    public static final Pose CARGO_SHIP_RIGHT_FAR = new Pose(15 + (4.75 / 12.0) + (5.125 / 12.0) + L / 2.0, 21 + (11.25 / 12.0), 180);
    public static final Pose CARGO_SHIP_MIDDLE_LEFT = new Pose(12 + (7 / 12.0), 18 + (10.875 / 12.0) - (7.5 / 12.0) - L / 2.0, 90);
    public static final Pose CARGO_SHIP_MIDDLE_RIGHT = new Pose(14 + (5 / 12.0), 18 + (10.875 / 12.0) - (7.5 / 12.0) - L / 2.0, 90);

    // Loading stations
    public static final Pose LEFT_LOADING_STATION = new Pose(1 + (10.75 / 12.0), L / 2.0, -90);
    public static final Pose RIGHT_LOADING_STATION = new Pose(25 + (1.25 / 12.0), L / 2.0, -90);

    // HAB
    public static final Pose LEVEL_1 = new Pose(9 + (8 / 12.0), L / 2.0, 90);
    public static final Pose LEFT_LEVEL_2 = new Pose(17 + (4 / 12.0), L / 2.0, 90);
    public static final Pose RIGHT_LEVEL_2 = new Pose(13 + (6 / 12.0), 4 + L / 2.0, 90);

    private final Vector2 position, direction;
    private final double heading;

    /**
     * Create a pose with position and direction
     * Direction is always normalized
     * Heading is created given the direction
     * @param position
     * @param direction
     */
    public Pose(Vector2 position, Vector2 direction)
    {
        this.position = position;
        this.direction = direction.getNormalized();
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
