package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
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

    protected static final double JOYSTICK_DEADZONE = 0.15;
    protected static final double JOYSTICK_EXPONENT = 2;

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
        return Math.pow(deadzoned, JOYSTICK_EXPONENT) * Math.signum(deadzoned);
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
        oi.getButton(OI.Joysticks.LEFT, 1).whenPressed(
                new InstantCommand(() -> collector.setFolded(!collector.isFolded())));

        oi.getButton(OI.Joysticks.LEFT, 4).whenPressed(
                new InstantCommand(() -> collector.setRollerThrottle(1)));
        oi.getButton(OI.Joysticks.LEFT, 5).whenPressed(
                new InstantCommand(() -> collector.setRollerThrottle(-1)));
        oi.getButton(OI.Joysticks.LEFT, 2).whenPressed(
                new InstantCommand(() -> collector.setRollerThrottle(0)));


        oi.getButton(OI.Joysticks.RIGHT, 2).whenPressed(
                new InstantCommand(() -> manipulator.setHooks(!manipulator.isHooksOpen())));
        oi.getButton(OI.Joysticks.RIGHT, 3).whenPressed(
                new InstantCommand(() -> manipulator.setExtender(!manipulator.isExtenderExtended())));

        oi.getButton(OI.Joysticks.PANEL, 11).whenPressed(
                new InstantCommand(() -> manipulator.setGrippers(1)));
        oi.getButton(OI.Joysticks.PANEL, 12).whenPressed(
                new InstantCommand(() -> manipulator.setGrippers(0)));
        oi.getButton(OI.Joysticks.PANEL, 13).whenPressed(
                new InstantCommand(() -> manipulator.setGrippers(-1)));

        oi.getButton(OI.Joysticks.PANEL, 14).whenPressed(
                new InstantCommand(() -> elevator.set(ControlMode.Position, 70))
        );
        oi.getButton(OI.Joysticks.PANEL, 20).whenPressed(
                new InstantCommand(() -> elevator.set(ControlMode.Position, 20))
        );
        oi.getButton(OI.Joysticks.PANEL, 18).whenPressed(
                new InstantCommand(() -> elevator.set(ControlMode.Position, 0))
        );
        */
    }
}