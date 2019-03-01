package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.*;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.Robot;
import frc.team1983.commands.climber.ClimbLevelTwo;
import frc.team1983.commands.climber.ManualClimber;
import frc.team1983.commands.collector.SetCollectorAngle;
import frc.team1983.commands.collector.SetCollectorRollerThrottle;
import frc.team1983.commands.collector.SetCollectorWristThrottle;
import frc.team1983.commands.collector.ToggleCollector;
import frc.team1983.commands.elevator.IncrementElevatorPosition;
import frc.team1983.commands.elevator.ManualElevator;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetManipulatorExtended;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.commands.manipulator.ToggleExtender;
import frc.team1983.commands.manipulator.ToggleHooks;
import frc.team1983.commands.climber.ClimbLevelThree;
import frc.team1983.subsystems.Climber;
import frc.team1983.subsystems.Elevator;

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

    protected static final double JOYSTICK_DEADZONE = 0.15;
    protected static final double JOYSTICK_EXPONENT = 3;

    public static final int TOGGLE_COLLECTOR = 7;
    public static final int MANIPULATOR_EXTENDED = 23;
    public static final int MANIPULATOR_RETRACTED = 20;
    public static final int TOGGLE_HOOKS = 8;
    public static final int EXPEL = 22;
    public static final int INTAKE = 21;
    public static final int COLLECTOR_WRIST_OUT = 5;
    public static final int COLLECTOR_WRIST_IN = 6;
    public static final int MANUAL_CLIMB_ELEVATOR_UP = 4;
    public static final int MANUAL_CLIMB_ELEVATOR_DOWN = 3;
    public static final int MANUAL_ELEVATOR_UP = 2;
    public static final int MANUAL_ELEVATOR_DOWN = 1;
    public static final int ELEVATOR_BOTTOM = 16;
    public static final int ELEVATOR_LOADING_STATION_BALL = 15;
    public static final int ELEVATOR_CARGOSHIP_BALL = 12;
    public static final int ELEVATOR_LOW_HATCH_BALL = 11;
    public static final int ELEVATOR_MIDDLE_HATCH_BALL = 10;
    public static final int ELEVATOR_TOP_HATCH_BALL = 9;
    public static final int CLIMB = 13;
    public static final int CLIMB_SELECTION = 25;
    public static final int MAUAL_MODE = 24;
    public static final int HATCH_BALL_TOGGLE = 14;

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

    protected static double scale(double raw)
    {
        double deadzoned = Math.abs(raw) > JOYSTICK_DEADZONE ? raw : 0;
        return Math.pow(Math.abs(deadzoned), JOYSTICK_EXPONENT) * Math.signum(deadzoned);
    }

    public double getLeftY()
    {
        return scale(-left.getY());
    }

    public double getRightY()
    {
        return scale(-right.getY());
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

    public void initializeBindings()
    {
        /*
        // Button to switch to manual mode is 24
        // Button to switch between balls and hatches is 14
        // Extra buttons are 17, 18, 19
      
        // Controls for pneumatics
        getButton(Joysticks.PANEL,TOGGLE_COLLECTOR).whenPressed(new ToggleCollector());
        getButton(Joysticks.PANEL,MANIPULATOR_EXTENDED).whenPressed(new SetManipulatorExtended(true));
        getButton(Joysticks.PANEL,MANIPULATOR_RETRACTED).whenPressed(new SetManipulatorExtended(false));
        getButton(Joysticks.PANEL,TOGGLE_HOOKS).whenPressed(new ToggleHooks());

        // Expel
        getButton(Joysticks.PANEL,EXPEL).whileHeld(new SetManipulatorRollerSpeed(Robot.getInstance().getManipulator(),1,1,true));

        // Intake
        getButton(Joysticks.PANEL,INTAKE).whileHeld(new SetManipulatorRollerSpeed(Robot.getInstance().getManipulator(),-0.8,-0.8,true));
        // getButton(Joysticks.PANEL,INTAKE).whileHeld(new SetCollectorRollerThrottle(1));

        // Manual collector wrist control
        getButton(Joysticks.PANEL,COLLECTOR_WRIST_OUT).whileHeld(new SetCollectorWristThrottle(0.5));
        getButton(Joysticks.PANEL,COLLECTOR_WRIST_IN).whileHeld(new SetCollectorWristThrottle(-0.25));

        // manual climb elevator up
        getButton(Joysticks.PANEL,MANUAL_CLIMB_ELEVATOR_UP).whileHeld(new ManualClimber(0.5));

        // manual climb elevator DOWN
        getButton(Joysticks.PANEL,MANUAL_CLIMB_ELEVATOR_DOWN).whileHeld(new ManualClimber(-0.5));

        // manual elevator up
        getButton(Joysticks.PANEL,MANUAL_ELEVATOR_UP).whileHeld(new ManualElevator(0.5));

        // manual elevator down
        getButton(Joysticks.PANEL,MANUAL_ELEVATOR_DOWN).whileHeld(new ManualElevator(-0.5));

        // Bottom
        getButton(Joysticks.PANEL,ELEVATOR_BOTTOM).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.BOTTOM),
                new SetElevatorPosition(ELEVATOR_LOW_HATCH_BALL)
        )
        {
            @Override
            protected boolean condition()
            {
                return getButton(Joysticks.PANEL,HATCH_BALL_TOGGLE).get();
            }
        });

        // Loading station ball
        getButton(Joysticks.PANEL,ELEVATOR_LOADING_STATION_BALL).whenPressed(new ConditionalCommand(
                new SetElevatorPosition(Elevator.FEEDER_BALL),
                new SetElevatorPosition(Elevator.BOTTOM_HATCH)
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


//        getButton(Joysticks.LEFT, 1).whenPressed(new IncrementElevatorPosition(3));
//        getButton(Joysticks.LEFT, 2).whenPressed(new IncrementElevatorPosition(-3));
        */
    }
}
