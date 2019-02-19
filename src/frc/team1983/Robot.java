package frc.team1983;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.commands.LimeLight;
import frc.team1983.commands.drivebase.RunTankDrive;
import frc.team1983.constants.RobotMap;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.services.logging.Level;
import frc.team1983.services.logging.Logger;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.subsystems.*;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.sensors.Gyro;
import frc.team1983.utilities.sensors.NavX;

import static java.lang.Math.abs;

public class Robot extends TimedRobot
{
    private static Robot instance;

    private Drivebase drivebase;
    private Elevator elevator;
    private Climber climber;
    private Collector collector;
    private Manipulator manipulator;

    private Compressor compressor;
    private NavX navx;
    private StateEstimator estimator;
    private OI oi;
    private Logger logger;

    Robot()
    {
        instance = this;

        logger = Logger.getInstance();
        logger.setGlobalLevel(Level.INFO);

        compressor = new Compressor(RobotMap.COMPRESSOR);

        drivebase = new Drivebase();
        drivebase.zero();

        elevator = new Elevator();
        elevator.zero();

        climber = new Climber();

        collector = new Collector();
        collector.zero();

        manipulator = new Manipulator();

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
    public void disabledInit()
    {
        Scheduler.getInstance().removeAll();
        for(MotorGroup motorGroup : MotorGroup.motorGroups)
            motorGroup.disableController();
        drivebase.setBrake(false);
        compressor.stop();
    }

    @Override
    public void autonomousInit()
    {
        drivebase.setBrake(true);
        compressor.start();
    }

    @Override
    public void teleopInit()
    {
        //Scheduler.getInstance().add(new RunTankDrive());
        compressor.start();
    }

    @Override
    public void teleopPeriodic()
    {

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

    public Elevator getElevator()
    {
        return elevator;
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

    public Manipulator getManipulator()
    {
        return manipulator;
    }

    public Collector getCollector()
    {
        return collector;
    }
}
