package frc.team1983;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.services.logging.Level;
import frc.team1983.services.logging.Logger;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;
import frc.team1983.utilities.sensors.Pigeon;

public class Robot extends TimedRobot
{
    private static Robot instance;
    private Drivebase drivebase;
    private Pigeon pigeon;
    private StateEstimator estimator;
    private OI oi;
    private Logger logger;

    Robot()
    {
        instance = this;

        logger = Logger.getInstance();
        logger.setGlobalLevel(Level.INFO);

        drivebase = new Drivebase();
        pigeon = new Pigeon(drivebase.getPigeonTalon());
        estimator = new StateEstimator();
        oi = new OI();

        oi.initializeBindings();
    }

    @Override
    public void robotPeriodic()
    {
        Scheduler.getInstance().run();

        System.out.println(pigeon.getHeading());
    }

    @Override
    public void autonomousInit()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(-10, 10, 180)
        );

        Scheduler.getInstance().add(new DrivePath(path));
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

    public Pigeon getPigeon()
    {
        return pigeon;
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
