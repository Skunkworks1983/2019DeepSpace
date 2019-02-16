package frc.team1983;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.commands.drivebase.RunTankDrive;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.services.logging.Level;
import frc.team1983.services.logging.Logger;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.Transmission;
import frc.team1983.utilities.sensors.Gyro;
import frc.team1983.utilities.sensors.NavX;

public class Robot extends TimedRobot
{
    private static Robot instance;
    private Drivebase drivebase;
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
        drivebase.zero();

        navx = new NavX();
        estimator = new StateEstimator();

        oi = new OI();
        oi.initializeBindings();
    }


    @Override
    public void robotInit()
    {
        navx.reset();
    }

    @Override
    public void robotPeriodic()
    {
        Scheduler.getInstance().run();

        SmartDashboard.putNumber("robotX", estimator.getPosition().getX());
        SmartDashboard.putNumber("robotY", estimator.getPosition().getY());
        SmartDashboard.putNumber("robotAngle", getGyro().getHeading());
    }

    @Override
    public void disabledInit()
    {
        Scheduler.getInstance().removeAll();
        for(Transmission transmission : Transmission.transmissions)
            transmission.disableController();
        drivebase.setBrake(false);
    }

    @Override
    public void autonomousInit()
    {
        drivebase.setBrake(true);
    }

    @Override
    public void teleopInit()
    {
        Scheduler.getInstance().add(new RunTankDrive());
    }

    public static Robot getInstance()
    {
        if (instance == null)
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
