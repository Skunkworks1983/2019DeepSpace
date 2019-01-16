package frc.team1983.services;

import com.ctre.phoenix.sensors.PigeonIMU;
import frc.team1983.Robot;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Pose;

public class StateEstimator implements Runnable
{
    public static final int UPDATE_RATE = 20;

    private Drivebase drivebase;
    private PigeonIMU pigeon;

    private double lastLeftPosition = 0, lastRightPosition = 0;
    private Vector2 position = new Vector2(0, 0);

    public StateEstimator(Drivebase drivebase, PigeonIMU pigeon)
    {
        this.drivebase = drivebase;
        this.pigeon = pigeon;

        lastLeftPosition = drivebase.getLeftPosition();
        lastRightPosition = drivebase.getRightPosition();

        new Thread(this).start();
    }

    public StateEstimator()
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getPigeon());
    }

    public Pose getCurrentPose()
    {
        return new Pose(position, pigeon.getFusedHeading());
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
    }

    private synchronized void execute()
    {
        double leftPosition = drivebase.getLeftPosition();
        double rightPosition = drivebase.getRightPosition();
        double angle = pigeon.getFusedHeading() * Math.PI / 180.0;

        double displacement = ((leftPosition - lastLeftPosition) + (rightPosition - lastRightPosition)) / 2;
        position.add(Vector2.scale(new Vector2(Math.sin(angle), Math.cos(angle)), displacement));

        lastLeftPosition = leftPosition;
        lastRightPosition = rightPosition;
    }

    @Override
    public void run()
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
