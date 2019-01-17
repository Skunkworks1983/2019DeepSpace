package frc.team1983.services;

import frc.team1983.Robot;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Pose;
import frc.team1983.utilities.sensors.Pigeon;

public class StateEstimator implements Runnable
{
    public static final int UPDATE_RATE = 30;

    private Drivebase drivebase;
    private Pigeon pigeon;

    private double lastLeftPosition, lastRightPosition;
    private Vector2 position = new Vector2(0, 0);

    public StateEstimator(Drivebase drivebase, Pigeon pigeon)
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
        return new Pose(position, pigeon.getHeading());
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
        double angle = Math.toRadians(pigeon.getHeading());

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
