package frc.team1983.services;

import frc.team1983.Robot;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Pose;
import frc.team1983.utilities.sensors.Gyro;
import frc.team1983.utilities.sensors.Limelight;

/**
 * This class estimates the position of the robot. Coordinate system conventions are
 * that the origin is in the bottom left corner from the perspective of standing at
 * the driver station, and theta = 0 towards the x-axis.
 */
public class StateEstimator implements Runnable
{
    public static final int UPDATE_RATE = 20;

    private Drivebase drivebase;
    private Gyro gyro;

    private double lastLeftPosition, lastRightPosition;
    private Vector2 position = Vector2.ZERO;

    public StateEstimator(Drivebase drivebase, Gyro gyro)
    {
        this.drivebase = drivebase;
        this.gyro = gyro;

        lastLeftPosition = drivebase.getLeftPosition();
        lastRightPosition = drivebase.getRightPosition();

        new Thread(this).start();
}

    public StateEstimator()
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getGyro());
    }

    protected synchronized void execute()
    {
        double leftPosition = drivebase.getLeftPosition();
        double rightPosition = drivebase.getRightPosition();
        double angle = Math.toRadians(gyro.getHeading());

        double displacement = ((leftPosition - lastLeftPosition) + (rightPosition - lastRightPosition)) / 2;
        position.add(Vector2.scale(new Vector2(Math.cos(angle), Math.sin(angle)), displacement));

        lastLeftPosition = leftPosition;
        lastRightPosition = rightPosition;
    }

    public synchronized void setPosition(Limelight limelight, Pose target)
    {
        position = Vector2.add(target.getPosition(), Vector2.twist(new Vector2(limelight.getXOffset(), limelight.getYOffset()), target.getPosition(), target.getHeading()));
    }

    public synchronized void setPose(Pose pose)
    {
        position = pose.getPosition();
        gyro.setHeading(pose.getHeading());
    }

    public synchronized Pose getCurrentPose()
    {
        return new Pose(position, gyro.getHeading());
    }

    public synchronized Vector2 getPosition()
    {
        return position;
    }

    @Override
    public void run()
    {
        while(true)
        {
            execute();

            try
            {
                Thread.sleep((long) 1000.0 / UPDATE_RATE);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
