package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team1983.Robot;
import frc.team1983.commands.ConditionalCommand;
import frc.team1983.commands.climber.Climb;
import frc.team1983.commands.collector.*;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.manipulator.*;
import frc.team1983.subsystems.*;
import frc.team1983.commands.climber.ManualClimber;
import frc.team1983.commands.climber.ManualClimber;
import frc.team1983.commands.collector.SetCollectorAngle;
import frc.team1983.commands.collector.SetCollectorRollerThrottle;
import frc.team1983.commands.collector.SetCollectorWristThrottle;
import frc.team1983.commands.collector.ToggleCollector;
import frc.team1983.commands.drivebase.RunArcadeDrive;
import frc.team1983.commands.drivebase.RunTankDrive;
import frc.team1983.commands.elevator.ManualElevator;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetManipulatorExtended;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.commands.manipulator.ToggleHooks;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.pathing.Path;

import java.util.HashMap;
public class OI
{
    public enum Joysticks
    {
        LEFT(0),
        RIGHT(1),
        PANEL(2);

        private int port;
        Joysticks(int port)
        {
            this.port = port;
        }

        public int getPort()
        {
            return port;
        }
    }
    private static boolean useDriveMode1 = false;

    public static final int JOYSTICK_TRIGGER = 1;
    public static final int JOYSTICK_BOTTOM_BUTTON = 2;
    public static final int JOYSTICK_TOP_BUTTON = 3;
    public static final int JOYSTICK_LEFT_BUTTON = 4;
    public static final int JOYSTICK_RIGHT_BUTTON = 5;

    public static final int HATCH_MODE_ENABLED = 14;

    public static final int ELEVATOR_1 = 16;
    public static final int ELEVATOR_2 = 15;
    public static final int ELEVATOR_3 = 12;
    public static final int ELEVATOR_4 = 11;
    public static final int ELEVATOR_5 = 10;
    public static final int ELEVATOR_6 = 9;

    public static final int TOGGLE_COLLECTOR_CLOSED = 8;
    public static final int TOGGLE_COLLECTOR_FOLDED = 7;

    public static final int EXTEND_MANIPULATOR = 23;
    public static final int RETRACT_MANIPULATOR = 20;

    public static final int EXPEL_PANEL = 21;
    public static final int INTAKE_PANEL = 22;

    public static final int EXPEL_BALL = 22;
    public static final int INTAKE_BALL = 21;

    public static final int MANUAL_ENABLED = 24;
    public static final int MANUAL_ELEVATOR_UP = 2;
    public static final int MANUAL_ELEVATOR_DOWN = 1;
    public static final int MANUAL_CLIMB_ELEVATOR_UP = 4;
    public static final int MANUAL_CLIMB_ELEVATOR_DOWN = 3;
    public static final int MANUAL_COLLECTOR_OUT = 5;
    public static final int MANUAL_COLLECTOR_IN = 6;

    public static final int LEVEL_TWO_CLIMB_ENABLED = 25;
    public static final int CLIMB = 13;

    protected static final double JOYSTICK_DEADZONE = 0.15;
    protected static final double JOYSTICK_EXPONENT = 1.7;
//    protected static final double JOYSTICK_EXPONENT = 3;
    protected static final double LINEAR_ZONE = 0.4;
    protected static final double LINEAR_SLOPE = Math.abs(Math.pow(LINEAR_ZONE, JOYSTICK_EXPONENT) / (LINEAR_ZONE - JOYSTICK_DEADZONE));

    private Joystick left, right, panel;
    private HashMap<Joysticks, HashMap<Integer, JoystickButton>> buttons;

    public OI(Joystick left, Joystick right, Joystick panel, HashMap<Joysticks, HashMap<Integer, JoystickButton>> buttons)
    {
        this.left = left;
        this.right = right;
        this.panel = panel;
        this.buttons = buttons;
    }

    public OI()
    {
        this(new Joystick(Joysticks.LEFT.getPort()),
                new Joystick(Joysticks.RIGHT.getPort()),
                new Joystick(Joysticks.PANEL.getPort()),
                new HashMap<>()
        );
    }

    // delete me after glacier peak
    protected static double scaleOld(double raw)
    {
        if(Math.abs(raw) < JOYSTICK_DEADZONE) return 0;
//        if(Math.abs(raw) < LINEAR_ZONE) return LINEAR_SLOPE * raw;
        if(Math.abs(raw) < LINEAR_ZONE) return LINEAR_SLOPE * raw - Math.signum(raw) * JOYSTICK_DEADZONE;
        else return Math.pow(Math.abs(raw), JOYSTICK_EXPONENT) * Math.signum(raw);
    }

    protected static double scale(double raw)
    {
        if(Math.abs(raw) < JOYSTICK_DEADZONE) return 0;
//        if(Math.abs(raw) < LINEAR_ZONE) return LINEAR_SLOPE * raw;
        if(Math.abs(raw) < LINEAR_ZONE) return (LINEAR_SLOPE * raw) - (Math.signum(raw) * JOYSTICK_DEADZONE);
        else return Math.pow(Math.abs(raw), JOYSTICK_EXPONENT) * Math.signum(raw);
    }

    public double getLeftY()
    {
        return scale(-left.getY());
    }

    // delete me after glacier peak
    public double getLeftYOld()
    {
        return scaleOld(-left.getY());
    }


    public double getRightY()
    {
        return scale(-right.getY());
    }

    public double getRightX()
    {
        return scale(right.getX());
    }

    public JoystickButton getButton(Joysticks joystickPort, int button)
    {
        Joystick joystick;
        switch(joystickPort)
        {
            case LEFT:
                joystick = left;
                break;
            case RIGHT:
                joystick = right;
                break;
            default: // If it wasn't the other two it must be panel. Java doesn't like it if we just do case PANEL.
                joystick = panel;
                break;
        }

        if(!buttons.containsKey(joystickPort))
            buttons.put(joystickPort, new HashMap<>());
        if(!buttons.get(joystickPort).containsKey(button))
            buttons.get(joystickPort).put(button, new JoystickButton(joystick, button));

        return buttons.get(joystickPort).get(button);
    }

    public boolean isInHatchMode()
    {
        return getButton(Joysticks.PANEL, HATCH_MODE_ENABLED).get();
    }

    public boolean isInLevelTwoClimbMode()
    {
        return getButton(Joysticks.PANEL, LEVEL_TWO_CLIMB_ENABLED).get();
    }

    public boolean isInManualMode()
    {
        return getButton(Joysticks.PANEL, MANUAL_ENABLED).get();
    }

    public void initializeBindings()
    {
        getButton(Joysticks.RIGHT, JOYSTICK_TRIGGER).whileHeld(new RunArcadeDrive());
        getButton(Joysticks.RIGHT, JOYSTICK_TRIGGER).whenReleased(new RunTankDrive());

//        // Quick paths
//        getButton(Joysticks.LEFT, JOYSTICK_BOTTOM_BUTTON).whenPressed(new LeftLoading());
//        getButton(Joysticks.RIGHT, JOYSTICK_BOTTOM_BUTTON).whenPressed(new RightLoading());
//
//        getButton(Joysticks.LEFT, JOYSTICK_LEFT_BUTTON).whenPressed(new LeftLoadingToRocketClose());
//        getButton(Joysticks.RIGHT, JOYSTICK_LEFT_BUTTON).whenPressed(new RightLoadingToRocketClose());
//
//        getButton(Joysticks.LEFT, JOYSTICK_TOP_BUTTON).whenPressed(new LeftLoadingToRocketMiddle());
//        getButton(Joysticks.RIGHT, JOYSTICK_TOP_BUTTON).whenPressed(new RightLoadingToRocketMiddle());
//
//        getButton(Joysticks.LEFT, JOYSTICK_RIGHT_BUTTON).whenPressed(new LeftLoadingToRocketFar());
//        getButton(Joysticks.RIGHT, JOYSTICK_RIGHT_BUTTON).whenPressed(new RightLoadingToRocketFar());


        getButton(Joysticks.PANEL, HATCH_MODE_ENABLED).whenPressed(new SetCollectorAngle(Collector.Setpoints.STOW));
        getButton(Joysticks.PANEL, HATCH_MODE_ENABLED).whenReleased(new ConditionalCommand(
                new SetCollectorAngle(Collector.Setpoints.STOW_UPPER),
                (args) -> Robot.getInstance().getElevator().isInDangerZone()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_1).whileHeld(new SetElevatorPosition(0));
        getButton(Joysticks.PANEL, ELEVATOR_1).whenReleased(new ConditionalCommand(
                new SetElevatorPosition(Elevator.Setpoints.Panel.GROUND_COLLECT),
                new SetElevatorPosition(Elevator.Setpoints.BOTTOM),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_2).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(15.0),
                new SetElevatorPosition(Elevator.Setpoints.Ball.LOADING_STATION),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_2).whenPressed(new ConditionalCommand(
                new SetCollectorAngle(Collector.Setpoints.STOW_UPPER),
                (args) -> !isInHatchMode()
        ));
        getButton(Joysticks.PANEL, ELEVATOR_2).whileHeld(new ConditionalCommand(
                new SetCollectorFolded(false),
                (args) -> !isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_3).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.Setpoints.Ball.CARGOSHIP),
                new SetElevatorPosition(Elevator.Setpoints.Ball.CARGOSHIP),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_3).whenPressed(new ConditionalCommand(
                new SetCollectorAngle(Collector.Setpoints.STOW_UPPER),
                (args) -> !isInHatchMode()
        ));
        getButton(Joysticks.PANEL, ELEVATOR_3).whileHeld(new ConditionalCommand(
                new SetCollectorFolded(false),
                (args) -> !isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_4).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.Setpoints.Ball.CARGOSHIP),
                new SetElevatorPosition(Elevator.Setpoints.Ball.ROCKET_BOTTOM),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_4).whenPressed(new ConditionalCommand(
                new SetCollectorAngle(Collector.Setpoints.STOW_UPPER),
                (args) -> !isInHatchMode()
        ));
        getButton(Joysticks.PANEL, ELEVATOR_4).whileHeld(new ConditionalCommand(
                new SetCollectorFolded(false),
                (args) -> !isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_5).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.Setpoints.Panel.ROCKET_MIDDLE),
                new SetElevatorPosition(Elevator.Setpoints.Ball.ROCKET_MIDDLE),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_5).whenPressed(new ConditionalCommand(
                new SetCollectorAngle(Collector.Setpoints.STOW_UPPER),
                (args) -> !isInHatchMode()
        ));
        getButton(Joysticks.PANEL, ELEVATOR_5).whileHeld(new ConditionalCommand(
                new SetCollectorFolded(false),
                (args) -> !isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_6).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.Setpoints.Panel.ROCKET_TOP),
                new SetElevatorPosition(Elevator.Setpoints.Ball.ROCKET_TOP),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_6).whenPressed(new ConditionalCommand(
                new SetCollectorAngle(Collector.Setpoints.STOW_UPPER),
                (args) -> !isInHatchMode()
        ));
        getButton(Joysticks.PANEL, ELEVATOR_6).whileHeld(new ConditionalCommand(
                new SetCollectorFolded(false),
                (args) -> !isInHatchMode()
        ));

        // -1 intakes panel, 1 expels panel
        // 1 intakes ball, -1 expels ball
        getButton(Joysticks.PANEL, INTAKE_PANEL).whileHeld(new ConditionalCommand(
                new SetManipulatorRollerSpeed(1),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, INTAKE_BALL).whileHeld(new ConditionalCommand(
                new SetManipulatorRollerSpeed(1),
                (args) -> !isInHatchMode()
        ));
        getButton(Joysticks.PANEL, INTAKE_BALL).whileHeld(new ConditionalCommand(
                new SetCollectorRollerThrottle(-0.8),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().isInDangerZone()
        ));
        getButton(Joysticks.PANEL, INTAKE_BALL).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.Setpoints.BOTTOM),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().getPosition() < 24.0
        ));
        getButton(Joysticks.PANEL, INTAKE_BALL).whenPressed(new ConditionalCommand(
                new SetCollectorAngle(Collector.Setpoints.COLLECT),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().isInDangerZone()
        ));
        getButton(Joysticks.PANEL, INTAKE_BALL).whileHeld(new ConditionalCommand(
                new SetCollectorFolded(true),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().isInDangerZone()
        ));
        getButton(Joysticks.PANEL, INTAKE_BALL).whenReleased(new ConditionalCommand(
                new SetCollectorAngle(Collector.Setpoints.STOW_UPPER),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().isInDangerZone()
        ));
        getButton(Joysticks.PANEL, INTAKE_BALL).whenPressed(new ConditionalCommand(
                new SetManipulatorExtended(true),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().isInDangerZone()
        ));
        getButton(Joysticks.PANEL, INTAKE_BALL).whenReleased(new ConditionalCommand(
                new SetManipulatorExtended(true),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().isInDangerZone()
        ));


        getButton(Joysticks.PANEL, EXPEL_PANEL).whileHeld(new ConditionalCommand(
                new SetManipulatorRollerSpeed(-1),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, EXPEL_BALL).whileHeld(new ConditionalCommand(
                new SetManipulatorRollerSpeed(-1),
                (args) -> !isInHatchMode()
        ));
        getButton(Joysticks.PANEL, EXPEL_BALL).whileHeld(new ConditionalCommand(
                new SetCollectorFolded(true),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().getPosition() < 12.0
        ));

        getButton(Joysticks.PANEL, EXPEL_BALL).whileHeld(new ConditionalCommand(
                new SetCollectorRollerThrottle(1),
                (args) -> !isInHatchMode() && Robot.getInstance().getElevator().isInDangerZone()
        ));

        getButton(Joysticks.PANEL,EXTEND_MANIPULATOR).whenPressed(new SetManipulatorExtended(false));
        getButton(Joysticks.PANEL,RETRACT_MANIPULATOR).whenPressed(new SetManipulatorExtended(true));

        getButton(Joysticks.PANEL, 19).whenPressed(new SetCollectorAngle(109));
        getButton(Joysticks.PANEL, 19).whileHeld(new SetCollectorFolded(true));
        getButton(Joysticks.PANEL, 18).whenPressed(new SetCollectorAngle(150));
        getButton(Joysticks.PANEL, 18).whileHeld(new SetCollectorFolded(true));
        getButton(Joysticks.PANEL, 17).whenPressed(new SetCollectorAngle(0));
        getButton(Joysticks.PANEL, 17).whileHeld(new SetCollectorFolded(true));

        getButton(Joysticks.PANEL, CLIMB).whenPressed(new ConditionalCommand(
                new Climb(-12, -8),
                new Climb(-24, -16),
                (args) -> isInLevelTwoClimbMode()
        ));

        getButton(Joysticks.PANEL,TOGGLE_COLLECTOR_FOLDED).whenPressed(new ToggleCollector());
        getButton(Joysticks.PANEL,TOGGLE_COLLECTOR_CLOSED).whenPressed(new ToggleHooks());

        // Manual collector wrist control
        getButton(Joysticks.PANEL, MANUAL_COLLECTOR_OUT).whileHeld(new ConditionalCommand(
                new SetCollectorWristThrottle(0.5),
                (args) -> getButton(Joysticks.PANEL, MANUAL_ENABLED).get()
        ));

        getButton(Joysticks.PANEL, MANUAL_COLLECTOR_IN).whileHeld(new ConditionalCommand(
                new SetCollectorWristThrottle(-0.25),
                (args) -> getButton(Joysticks.PANEL, MANUAL_ENABLED).get()
        ));

        // manual climb elevator up
        getButton(Joysticks.PANEL, MANUAL_CLIMB_ELEVATOR_UP).whileHeld(new ConditionalCommand(
                new ManualClimber(0.5),
                (args) -> getButton(Joysticks.PANEL, MANUAL_ENABLED).get()
        ));

        // manual climb elevator DOWN
        getButton(Joysticks.PANEL, MANUAL_CLIMB_ELEVATOR_DOWN).whileHeld(new ConditionalCommand(
                new ManualClimber(-0.5),
                (args) -> getButton(Joysticks.PANEL, MANUAL_ENABLED).get()
        ));

        // manual elevator up
        getButton(Joysticks.PANEL, MANUAL_ELEVATOR_UP).whileHeld(new ConditionalCommand(
                new ManualElevator(0.5),
                (args) -> getButton(Joysticks.PANEL, MANUAL_ENABLED).get()
        ));

        // manual elevator down
        getButton(Joysticks.PANEL, MANUAL_ELEVATOR_DOWN).whileHeld(new ConditionalCommand(
                new ManualElevator(-0.5),
                (args) -> getButton(Joysticks.PANEL, MANUAL_ENABLED).get()
        ));

                /*
        // Button to switch to manual mode is 24
        // Button to switch between balls and hatches is 14
        // Extra buttons are 17, 18, 19
        getButton(Joysticks.PANEL, 17).whileHeld(new SetCollectorRollerThrottle(.5));
        // Controls for pneumatics
        getButton(Joysticks.PANEL,TOGGLE_COLLECTOR).whenPressed(new ToggleCollector());
        getButton(Joysticks.PANEL,TOGGLE_HOOKS).whenPressed(new ToggleHooks());

        // Swapping modes
        getButton(Joysticks.PANEL, HATCH_BALL_TOGGLE).whenReleased(new SetCollectorAngle(0));
        getButton(Joysticks.PANEL, HATCH_BALL_TOGGLE).whenPressed(new SetCollectorAngle(140));

        // Expel
        getButton(Joysticks.PANEL, EXPEL_PANEL).whileHeld(new ConditionalCommand(
                new SetManipulatorRollerSpeed(Robot.getInstance().getManipulator(),1,1,true),
                new SetManipulatorRollerSpeed(Robot.getInstance().getManipulator(),-1,-1,true),
                (args) -> isInHatchMode()
        ));

        // Intake
        getButton(Joysticks.PANEL, INTAKE_PANEL).whileHeld(new SetManipulatorRollerSpeed(1));


        // Bottom
        getButton(Joysticks.PANEL, ELEVATOR_1).whenReleased(new ConditionalCommand(
                new SetElevatorPosition(0),
                new SetElevatorPosition(Elevator.BOTTOM_HATCH),
                (args) -> isInHatchMode()
        ));

        getButton(Joysticks.PANEL, ELEVATOR_BOTTOM).whileHeld(new Command()
        {
            @Override
            public void initialize()
            {
                Robot.getInstance().getElevator().set(ControlMode.MotionMagic, 0);
            }

            @Override
            protected boolean isFinished()
            {
                return false;
            }
        });

        // Loading station ball
        getButton(Joysticks.PANEL,ELEVATOR_LOADING_STATION_BALL).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.FEEDER_BALL),
                new SetElevatorPosition(15)
        )
        {
            @Override
            protected boolean condition()
            {
                return getButton(Joysticks.PANEL,HATCH_BALL_TOGGLE).get();
            }
        });

        // Ball cargo ship
        getButton(Joysticks.PANEL, ELEVATOR_CARGOSHIP_BALL).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.CARGOSHIP_BALL),
                new SetElevatorPosition(Elevator.BOTTOM_HATCH)
        )
        {
            @Override
            protected boolean condition()
            {
                return getButton(Joysticks.PANEL,HATCH_BALL_TOGGLE).get();
            }
        });

        // low hatch/ball rocket
        getButton(Joysticks.PANEL, ELEVATOR_LOW_HATCH_BALL).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.ROCKET_LOW_BALL),
                new SetElevatorPosition(Elevator.BOTTOM_HATCH)
        )
        {
            @Override
            protected boolean condition()
            {
                return getButton(Joysticks.PANEL,HATCH_BALL_TOGGLE).get();
            }
        });

        // middle hatch/ball
        getButton(Joysticks.PANEL, ELEVATOR_MIDDLE_HATCH_BALL).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.ROCKET_MIDDLE_BALL),
                new SetElevatorPosition(Elevator.MIDDLE_HATCH)
        )
        {
            @Override
            protected boolean condition()
            {
                return getButton(Joysticks.PANEL,HATCH_BALL_TOGGLE).get();
            }
        });

        // Top hatch/ball
        getButton(Joysticks.PANEL, ELEVATOR_TOP_HATCH_BALL).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.ROCKET_TOP_BALL),
                new SetElevatorPosition(Elevator.TOP_HATCH)
        )
        {
            @Override
            protected boolean condition()
            {
                return getButton(Joysticks.PANEL,HATCH_BALL_TOGGLE).get();
            }
        });

        // Climb
        getButton(Joysticks.PANEL, CLIMB).whileHeld(new ConditionalCommand(
                new ClimbLevelTwo(),
                new ClimbLevelThree()
        )
        {
            @Override
            protected boolean condition()
            {
                return getButton(Joysticks.PANEL,CLIMB_SELECTION).get();
            }
        });
        */

//        getButton(Joysticks.LEFT, 1).whenPressed(new IncrementElevatorPosition(3));
//        getButton(Joysticks.LEFT, 2).whenPressed(new IncrementElevatorPosition(-3));
    }
}
