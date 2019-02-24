package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorAngle;
import frc.team1983.commands.collector.SetCollectorRollerThrottle;
import frc.team1983.commands.collector.SetCollectorWristThrottle;
import frc.team1983.commands.collector.ToggleCollector;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.commands.manipulator.ToggleExtender;
import frc.team1983.commands.manipulator.ToggleHooks;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.commands.climber.Climb;
import frc.team1983.utilities.motors.ControlMode;

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
    ///COLLECTION BUTTONS///
    public static final int SWITCH = 12; //magic
    //public static final int INTAKE = 13; //magic
    public static final int FLOOR_COLLECT = 13;
    public static final int STATION_COLLECT = 20;

    ///PLACEMENT BUTTONS///
    public static final int HIGH = 9;
    public static final int MID = 10;
    public static final int LOW =11;
    public static final int CARGO_SHIP = 12;
    public static final int RELEASE = 20; //expel & place

    public static final int COL_FOLD = 17;
    public static final int COL_UNFOLD = 19;

    protected static final double JOYSTICK_DEADZONE = 0.15;
    protected static final double JOYSTICK_EXPONENT = 3;

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
    public boolean isPressed(int button)
    {

        return panel.getRawButtonPressed(button + 1);
    }
    public void initializeBindings()
    {
        //Controls for pneumatics
        getButton(Joysticks.PANEL,14).whenPressed(new ToggleCollector());
        getButton(Joysticks.PANEL,20).whenPressed(new ToggleExtender());
        getButton(Joysticks.PANEL,18).whenPressed(new ToggleHooks());

        //TODO Find actual collector angle for collection
        //Controls for collector angle
        getButton(Joysticks.PANEL,COL_UNFOLD).whenPressed(new SetCollectorAngle(0));
        getButton(Joysticks.PANEL,COL_FOLD).whenPressed(new SetCollectorAngle(130));

        //controls for throttle on collector roller and side roller
        getButton(Joysticks.PANEL,7).whileHeld(new SetManipulatorRollerSpeed(Robot.getInstance().getManipulator(),1,-1,true));
        getButton(Joysticks.PANEL,6).whileHeld(new SetManipulatorRollerSpeed(Robot.getInstance().getManipulator(),-0.8,0.8,true));
        getButton(Joysticks.PANEL,19).whileHeld(new SetCollectorRollerThrottle(1));
        //getButton(Joysticks.PANEL,17).whileHeld(new SetCollectorRollerThrottle(1));

        //Manual collector wrist control
        getButton(Joysticks.PANEL,3).whileHeld(new SetCollectorWristThrottle(0.5));
        getButton(Joysticks.PANEL,4).whileHeld(new SetCollectorWristThrottle(-0.25));

        //TODO Add actual elevator set points
        //Controls for elevator set points
        getButton(Joysticks.PANEL,FLOOR_COLLECT).whenPressed(new SetElevatorPosition(0));
        getButton(Joysticks.PANEL,CARGO_SHIP).whenPressed(new SetElevatorPosition(12.5));
        getButton(Joysticks.PANEL,LOW).whenPressed(new SetElevatorPosition(25));
        getButton(Joysticks.PANEL,MID).whenPressed(new SetElevatorPosition(35));
        getButton(Joysticks.PANEL,HIGH).whenPressed(new SetElevatorPosition(60));
    }
}