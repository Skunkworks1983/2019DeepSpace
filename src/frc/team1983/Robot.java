package frc.team1983;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;

public class Robot extends TimedRobot
{
    private static Robot instance;
    private Drivebase drivebase;
    private StateEstimator estimator;
    private OI oi;

    public Robot()
    {
        drivebase = new Drivebase();
        estimator = new StateEstimator();
        oi = new OI();
    }

    @Override
    public void robotPeriodic()
    {
        Scheduler.getInstance().run();
    }

    public static Robot getInstance()
    {
        if(instance == null)
            instance = new Robot();
        return instance;
    }

    public Drivebase getDrivebase()
    {
        return drivebase;
    }

    public StateEstimator getEstimator()
    {
        return estimator;
    }

    public OI getOI()
    {
        return oi;
    }
}
