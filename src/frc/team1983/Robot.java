package frc.team1983;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.commands.drivebase.RunTankDrive;
import frc.team1983.constants.RobotMap;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.services.logging.Level;
import frc.team1983.services.logging.Logger;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.subsystems.*;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.pathing.Pose;
import frc.team1983.utilities.sensors.Gyro;
import frc.team1983.utilities.sensors.Limelight;
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
    private Limelight limelight;
    private OI oi;
    private Logger logger;

    private DigitalInput dio = new DigitalInput(7);

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

        limelight = new Limelight();

        oi = new OI();
        oi.initializeBindings();
    }


    @Override
    public void robotInit()
    {
        compressor.start();
        navx.reset();
        estimator.setPose(Pose.DEFAULT);
    }


    @Override
    public void robotPeriodic()
    {
        Scheduler.getInstance().run();

        SmartDashboard.putNumber("robotX", estimator.getPosition().getX());
        SmartDashboard.putNumber("robotY", estimator.getPosition().getY());
        SmartDashboard.putNumber("robotAngle", getGyro().getHeading());

        //        System.out.println("DIO: " + dio.get());
        //        System.out.println("Wrist: " + collector.getTicks());
        //                System.out.println("Elevator: " + elevator.getPosition());
    }

    @Override
    public void autonomousInit()
    {
        Scheduler.getInstance().removeAll();
        for (MotorGroup motorGroup : MotorGroup.motorGroups)
            motorGroup.disableController();
        drivebase.setBrake(false);
        compressor.stop();
    }

    @Override
    public void autonomousPeriodic()
    {

    }

    @Override
    public void teleopInit()
    {

    }

    @Override
    public void teleopPeriodic()
    {
        Scheduler.getInstance().add(new RunTankDrive());
//                drivebase.setLeft(ControlMode.Throttle, oi.getLeftY() * abs(oi.getLeftY()));
//                drivebase.setRight(ControlMode.Throttle, oi.getRightY() * abs(oi.getRightY()));

//        climber.setThrottle(oi.getRightY() * abs(oi.getRightY()));

//        collector.setWristThrottle(oi.getLeftY() * abs(oi.getLeftY()));

//                if (!oi.getButton(OI.Joysticks.RIGHT, 1).get()) manipulator.setGrippers(oi.getRightY() * abs(oi.getRightY()));
//                else elevator.set(ControlMode.Throttle, oi.getRightY() * abs(oi.getRightY()));
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

    public Limelight getLimelight()
    {
        return limelight;
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
