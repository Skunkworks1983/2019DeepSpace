package frc.team1983.services;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.Robot;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Pose;
import frc.team1983.utilities.sensors.Gyro;
import frc.team1983.utilities.sensors.Pigeon;

// This class estimates the position of the robot. Coordinate system conventions are
// that the origin is in the bottom left corner from the perspective of standing at
// the driver station, and theta = 0 towards the x-axis.
public class StateEstimator implements Runnable
{
    public static final int UPDATE_RATE = 20;

    private Drivebase drivebase;
    private Gyro gyro;

    private double lastLeftPosition, lastRightPosition;
    // TODO: find real
//    private Vector2 position = new Vector2(0, 0);
//    private Vector2 position = new Vector2(32.0 / 24.0, 36.0 / 24.0);
    private Vector2 position = Pose.LEVEL_1_MIDDLE.getPosition();

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

    public Pose getCurrentPose()
    {
        return new Pose(position, gyro.getHeading());
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
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
            catch(InterruptedException exception)
            {
                exception.printStackTrace();
            }
        }
    }
}
