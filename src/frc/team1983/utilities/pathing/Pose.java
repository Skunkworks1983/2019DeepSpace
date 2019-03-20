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
public final class Pose
{
    public static final Pose ORIGIN = new Pose(0, 0, 90);
    public static final Pose DEFAULT = new Pose(Constants.ROBOT_WIDTH / 2, Constants.ROBOT_LENGTH / 2, 90);

    static double L = Constants.ROBOT_LENGTH;

    protected static final double ROCKET_LINE_UP_DISTANCE = 3.75; // feet
    protected static final double ROCKET_DRIVER_SWITCH_DISTANCE = 2.0; // feet
    protected static final double LOADING_STATION_DRIVER_SWITCH_DISTANCE = 3.0; // feet

    // Rockets
    static double dx = Math.cos(Math.toRadians(61.25));
    static double dy = Math.sin(Math.toRadians(61.25));

    static double dxRocket = L / 2.0 * dx;
    static double dyRocket = L / 2.0 * dy;
    public static final Pose LEFT_ROCKET_CLOSE = new Pose(1 + (6.5 / 12.0) + dxRocket, 17 + (8.5 / 12.0) - dyRocket, 118.75);
    public static final Pose LEFT_ROCKET_MIDDLE = new Pose(2 + (3.5 / 12.0) + L / 2.0, 19, 180);
    public static final Pose LEFT_ROCKET_FAR = new Pose(1 + (6.5 / 12.0) + dxRocket, 20 + (3.5 / 12.0) + dyRocket, -118.75);

    public static final Pose RIGHT_ROCKET_CLOSE = new Pose(25 + (5.5 / 12.0) - dxRocket, 17 + (8.5 / 12.0) - dyRocket, 61.25);
    public static final Pose RIGHT_ROCKET_MIDDLE = new Pose(24 + (8.5 / 12.0) - L / 2.0, 19, 0);
    public static final Pose RIGHT_ROCKET_FAR = new Pose(25 + (5.5 / 12.0) - dxRocket, 20 + (3.5 / 12.0) + dyRocket, -61.25);

    static double dxLineUp = ROCKET_LINE_UP_DISTANCE * dx;
    static double dyLineUp = ROCKET_LINE_UP_DISTANCE * dy;
    public static final Pose LEFT_ROCKET_FAR_LINE_UP = new Pose(Vector2.add(LEFT_ROCKET_FAR.getPosition(), new Vector2(dxLineUp, dyLineUp)), -118.75);
    public static final Pose RIGHT_ROCKET_FAR_LINE_UP = new Pose(Vector2.add(RIGHT_ROCKET_FAR.getPosition(), new Vector2(-dxLineUp, dyLineUp)), -61.25);

    static double dxDriverSwitch = ROCKET_DRIVER_SWITCH_DISTANCE * dx;
    static double dyDriverSwitch = ROCKET_DRIVER_SWITCH_DISTANCE * dy;
    public static final Pose LEFT_ROCKET_FAR_DRIVER_SWITCH = new Pose(Vector2.add(LEFT_ROCKET_FAR.getPosition(), new Vector2(dxDriverSwitch, dyDriverSwitch)), -118.75);
    public static final Pose RIGHT_ROCKET_FAR_DRIVER_SWITCH = new Pose(Vector2.add(RIGHT_ROCKET_FAR.getPosition(), new Vector2(-dxDriverSwitch, dyDriverSwitch)), -61.25);

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
    public static final Pose LEFT_LOADING_STATION = new Pose(2 + (2.75 / 12.0), L / 2.0, -90);
    public static final Pose RIGHT_LOADING_STATION = new Pose(24 + (9.25 / 12.0), L / 2.0, -90);

    public static final Pose LEFT_LOADING_STATION_LINE_UP = new Pose(Pose.LEFT_ROCKET_FAR_LINE_UP.getPosition().getX() + 3, Pose.LEFT_ROCKET_FAR_LINE_UP.getPosition().getY(), -90);
    public static final Pose RIGHT_LOADING_STATION_LINE_UP = new Pose(Pose.RIGHT_ROCKET_FAR_LINE_UP.getPosition().getX() - 3, Pose.RIGHT_ROCKET_FAR_LINE_UP.getPosition().getY(), -90);

    public static final Pose LEFT_LOADING_STATION_DRIVER_SWITCH = new Pose(Vector2.add(LEFT_LOADING_STATION.getPosition(), new Vector2(0, LOADING_STATION_DRIVER_SWITCH_DISTANCE)), -90);
    public static final Pose RIGHT_LOADING_STATION_DRIVER_SWITCH = new Pose(Vector2.add(RIGHT_LOADING_STATION.getPosition(), new Vector2(0, LOADING_STATION_DRIVER_SWITCH_DISTANCE)), -90);

    // HAB
    public static final Pose LEVEL_1_LEFT = new Pose(9 + (8 / 12.0), 4 + L / 2.0, 90);
    public static final Pose LEVEL_1_MIDDLE = new Pose(13 + (6 / 12.0), 4 + L / 2.0, 90);
    public static final Pose LEVEL_1_RIGHT = new Pose(17 + (4 / 12.0), 4 + L / 2.0, 90);
    public static final Pose LEVEL_2_LEFT = new Pose(9 + (8 / 12.0), L / 2.0, 90);
    public static final Pose LEVEL_2_RIGHT = new Pose(17 + (4 / 12.0), L / 2.0, 90);

    public static final Pose LEVEL_1_LEFT_REVERSED = LEVEL_1_LEFT.getReversed();
    public static final Pose LEVEL_1_MIDDLE_REVERSED = LEVEL_1_MIDDLE.getReversed();
    public static final Pose LEVEL_1_RIGHT_REVERSED = LEVEL_1_RIGHT.getReversed();
    public static final Pose LEVEL_2_LEFT_REVERSED = LEVEL_2_LEFT.getReversed();
    public static final Pose LEVEL_2_RIGHT_REVERSED = LEVEL_2_RIGHT.getReversed();

    private final Vector2 position, direction;
    private final double heading;

    /**
     * Create a pose with position and direction
     * Direction is always normalized
     * Heading is created given the direction
     *
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
        return position.copy();
    }

    public Vector2 getDirection()
    {
        return direction.copy();
    }

    public double getHeading()
    {
        return heading;
    }

    public Pose copy()
    {
        return new Pose(getPosition(), getDirection());
    }

    public Pose getReversed()
    {
        return new Pose(getPosition(), getDirection().getNegative());
    }

    @Override
    public String toString()
    {
        return getPosition() + ", " + getDirection() + ", " + getHeading();
    }

    public static boolean equals(Pose pose1, Pose pose2)
    {
        return Vector2.equals(pose1.getPosition(), pose2.getPosition()) && Vector2.equals(pose1.getDirection(), pose2.getDirection());
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Pose)
            return Pose.equals(this, (Pose) other);
        return false;
    }
}
