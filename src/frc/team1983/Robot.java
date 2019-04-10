package frc.team1983;

import edu.wpi.cscore.VideoCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.autonomous.routines.LeftRocketFarHatch;
import frc.team1983.autonomous.routines.RightRocketFarHatch;
import frc.team1983.autonomous.paths.LeftLoadingToRocketFar;
import frc.team1983.autonomous.paths.RightLoadingToRocketFar;
import frc.team1983.autonomous.routines.DoNothing;
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

import static frc.team1983.services.OI.JOYSTICK_BOTTOM_BUTTON;

public class Robot extends TimedRobot
{
    private static Robot instance;

    private Logger logger;

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

    private SendableChooser<Pose> startingPoseChooser;
    private SendableChooser<Command> autoChooser;

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
        navx.reset();

        estimator = new StateEstimator();

        limelight = new Limelight();

        oi = new OI();
        oi.initializeBindings();
    }

    @Override
    public void robotInit()
    {
        CameraServer.getInstance().startAutomaticCapture(0);
        CameraServer.getInstance().startAutomaticCapture(1);

        startingPoseChooser = new SendableChooser<>();
        startingPoseChooser.setDefaultOption("DO NOT RUN AUTO", Pose.LEVEL_1_MIDDLE);
        startingPoseChooser.addOption("Level 1 middle forward", Pose.LEVEL_1_MIDDLE);
        startingPoseChooser.addOption("Level 1 left reversed", Pose.LEVEL_1_LEFT_REVERSED);
        startingPoseChooser.addOption("Level 1 right reversed", Pose.LEVEL_1_RIGHT_REVERSED);
        startingPoseChooser.addOption("Level 2 left reversed", Pose.LEVEL_2_LEFT_REVERSED);
        startingPoseChooser.addOption("Level 2 right reversed", Pose.LEVEL_2_RIGHT_REVERSED);
        SmartDashboard.putData("Starting pose chooser", startingPoseChooser);

        autoChooser = new SendableChooser<>();
        autoChooser.setDefaultOption("Driver controlled", new DoNothing());
        autoChooser.addOption("Right rocket reversed", new RightRocketFarHatch());
        autoChooser.addOption("Left rocket reversed", new LeftRocketFarHatch());
        SmartDashboard.putData("Auto chooser", autoChooser);
    }

    @Override
    public void robotPeriodic()
    {
        Scheduler.getInstance().run();

        SmartDashboard.putNumber("robotX", estimator.getPosition().getX());
        SmartDashboard.putNumber("robotY", estimator.getPosition().getY());
        SmartDashboard.putNumber("robotAngle", getGyro().getHeading());

        SmartDashboard.putNumber("Time left in match", DriverStation.getInstance().getMatchTime());
    }

    @Override
    public void autonomousInit()
    {
        Scheduler.getInstance().removeAll();
        estimator.setPose(startingPoseChooser.getSelected());
        Scheduler.getInstance().add(autoChooser.getSelected());

        compressor.start();
        manipulator.setOpen(true);

        elevator.setPosition(Elevator.Setpoints.Panel.ROCKET_BOTTOM);
    }

    @Override
    public void autonomousPeriodic()
    {
        if(oi.isInManualMode())
            Scheduler.getInstance().removeAll();
    }

    @Override
    public void teleopInit()
    {
        compressor.start();

        if(startingPoseChooser.getSelected().equals(Pose.LEVEL_1_LEFT_REVERSED) ||
            startingPoseChooser.getSelected().equals(Pose.LEVEL_1_LEFT) ||
            startingPoseChooser.getSelected().equals(Pose.LEVEL_2_LEFT_REVERSED) ||
            startingPoseChooser.getSelected().equals(Pose.LEVEL_2_LEFT))
        {
            oi.getButton(OI.Joysticks.RIGHT, JOYSTICK_BOTTOM_BUTTON).whenPressed(new LeftLoadingToRocketFar());
        }
        else if(startingPoseChooser.getSelected().equals(Pose.LEVEL_1_RIGHT_REVERSED) ||
                startingPoseChooser.getSelected().equals(Pose.LEVEL_1_RIGHT) ||
                startingPoseChooser.getSelected().equals(Pose.LEVEL_2_RIGHT_REVERSED) ||
                startingPoseChooser.getSelected().equals(Pose.LEVEL_2_RIGHT))
        {
            oi.getButton(OI.Joysticks.RIGHT, JOYSTICK_BOTTOM_BUTTON).whenPressed(new RightLoadingToRocketFar());
        }
    }

    @Override
    public void disabledInit()
    {
        Scheduler.getInstance().removeAll();
        for(MotorGroup motorGroup : MotorGroup.motorGroups)
            motorGroup.disableController();
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
