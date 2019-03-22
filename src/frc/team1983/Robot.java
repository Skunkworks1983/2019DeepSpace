package frc.team1983;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.autonomous.routines.LeftRocketFarHatch;
import frc.team1983.autonomous.routines.RightRocketFarHatch;
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
    Logger logger;

    private SendableChooser startingPoseChooser;
    private SendableChooser autoChooser;

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
        climber.zero();

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
        getGyro().reset();
        estimator.setPose(Pose.LEVEL_1_LEFT_REVERSED);
        compressor.start();
        CameraServer.getInstance().startAutomaticCapture();

        startingPoseChooser = new SendableChooser();
        startingPoseChooser.addDefault("Level 1 Middle", Pose.LEVEL_1_MIDDLE);
        startingPoseChooser.addOption("Level 1 Left", Pose.LEVEL_1_LEFT);
        startingPoseChooser.addOption("Level 1 Right", Pose.LEVEL_1_RIGHT);
        startingPoseChooser.addOption("Level 1 Middle Reversed", Pose.LEVEL_1_MIDDLE_REVERSED);
        startingPoseChooser.addOption("Level 1 Left Reversed", Pose.LEVEL_1_LEFT_REVERSED);
        startingPoseChooser.addOption("Level 1 Right Reversed", Pose.LEVEL_1_RIGHT_REVERSED);
        startingPoseChooser.addOption("Level 2 Left", Pose.LEVEL_2_LEFT);
        startingPoseChooser.addOption("Level 2 Right", Pose.LEVEL_2_RIGHT);
        startingPoseChooser.addOption("Level 2 Left Reversed", Pose.LEVEL_2_LEFT_REVERSED);
        startingPoseChooser.addOption("Level 2 Right Reversed", Pose.LEVEL_2_RIGHT_REVERSED);

        SmartDashboard.putData("Starting Pose", startingPoseChooser);

        autoChooser = new SendableChooser();
        autoChooser.addOption("Right Rocket Hatch", new RightRocketFarHatch());
        autoChooser.addOption("Left Rocket Hatch", new LeftRocketFarHatch());

        SmartDashboard.putData("Auto Selected", autoChooser);
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
    public void autonomousInit()
    {
        estimator.setPose((Pose) startingPoseChooser.getSelected());
        compressor.start();
        manipulator.setOpen(true);
        Scheduler.getInstance().add((Command) autoChooser.getSelected());
    }

    @Override
    public void autonomousPeriodic()
    {

    }

    @Override
    public void teleopInit()
    {
        estimator.setPose((Pose) startingPoseChooser.getSelected());
        compressor.start();
    }

    @Override
    public void teleopPeriodic()
    {

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

    public Climber getClimber()
    {
        return climber;
    }

    public Manipulator getManipulator()
    {
        return manipulator;
    }

    public Collector getCollector()
    {
        return collector;
    }

    public Compressor getCompressor()
    {
        return compressor;
    }

    public Limelight getLimelight()
    {
        return limelight;
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
