package frc.team1983;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.drivebase.SmellyDashListener;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.services.logging.Level;
import frc.team1983.services.logging.Logger;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;
import frc.team1983.utilities.sensors.Gyro;
import frc.team1983.utilities.sensors.NavX;
import frc.team1983.utilities.sensors.Pigeon;

public class Robot extends TimedRobot
{
    private static Robot instance;
    private Drivebase drivebase;
    private Pigeon pigeon;
    private NavX navx;
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
        navx = new NavX();
        estimator = new StateEstimator();

        oi = new OI();
        oi.initializeBindings();
    }


    @Override
    public void robotInit()
    {
        navx.reset();
        pigeon.reset();
    }
    @Override
    public void teleopInit()
    {
    }
    @Override
    public void teleopPeriodic()
    {
        Scheduler.getInstance().run();
    }
    @Override
    public void robotPeriodic()
    {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("robotX", estimator.getPosition().getX());
        SmartDashboard.putNumber("robotY", estimator.getPosition().getY());
        SmartDashboard.putNumber("robotAngle", Math.toDegrees(getGyro().getHeading()));
    }

    @Override
    public void disabledInit()
    {
        drivebase.setBrake(false);
    }

    @Override
    public void autonomousInit()
    {
        drivebase.setBrake(true);
        Scheduler.getInstance().add(new SmellyDashListener());
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

    public Gyro getGyro()
    {
        return navx;
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
