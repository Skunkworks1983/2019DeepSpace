package frc.team1983;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.commands.collector.CollectionManager;
import frc.team1983.commands.drivebase.RunTankDrive;
import frc.team1983.constants.RobotMap;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.services.logging.Level;
import frc.team1983.services.logging.Logger;
import frc.team1983.subsystems.*;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.pathing.Pose;
import frc.team1983.utilities.sensors.Gyro;
import frc.team1983.utilities.sensors.NavX;

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

    private CollectionManager collectionManager;

    Robot()
    {
        instance = this;

        collectionManager = new CollectionManager();

        logger = Logger.getInstance();
        logger.setGlobalLevel(Level.INFO);

        compressor = new Compressor(RobotMap.COMPRESSOR);

        drivebase = new Drivebase();
        drivebase.zero();

        elevator = new Elevator();
        elevator.zero();

        climber = new Climber();
        climber.zero();

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
        estimator.setPose(Pose.DEFAULT);
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
        for (MotorGroup motorGroup : MotorGroup.motorGroups)
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
        compressor.start();
        Scheduler.getInstance().add(new RunTankDrive());
        Scheduler.getInstance().add(collectionManager);
    }

    @Override
    public void teleopPeriodic()
    {
        if(oi.getButton(OI.Joysticks.LEFT, 2).get())
            elevator.set(ControlMode.Position, 0);
        if(oi.getButton(OI.Joysticks.LEFT, 3).get())
            elevator.set(ControlMode.Position, 30);

        if(oi.getButton(OI.Joysticks.LEFT, 4).get())
            collector.setAngle(0);
        if(oi.getButton(OI.Joysticks.LEFT, 5).get())
            collector.setAngle(130);

        if(oi.getButton(OI.Joysticks.RIGHT, 2).get())
            collector.setFolded(false);
        if(oi.getButton(OI.Joysticks.RIGHT, 3).get())
            collector.setFolded(true);


        //System.out.println("COLLECTOR STATE :    " + collector.currentState);

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

    public Climber getClimber()
    {
        return climber;
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

    public CollectionManager getCollectionManager(){
        return collectionManager;}

}
