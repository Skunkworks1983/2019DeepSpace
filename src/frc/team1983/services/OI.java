package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import java.util.HashMap;

import static java.lang.Math.abs;

public class OI
{
    public enum Joysticks //Ordinal used, so order is important
    {
        LEFT,
        RIGHT,
        PANEL
    }

    private static final double JOYSTICK_DEADZONE = 0.15;

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
        this(new Joystick(Joysticks.LEFT.ordinal()),
                new Joystick(Joysticks.RIGHT.ordinal()),
                new Joystick(Joysticks.PANEL.ordinal()),
                new HashMap<>()
        );
    }

    public double getLeftY()
    {
        double raw = -left.getY();
        return abs(raw) * raw;
    }

    public double getRightY()
    {
        double raw = -right.getY();
        return abs(raw) * raw;
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
            default: //If it wasn't the other two it must be panel. Java doesn't like it if we just do case PANEL.
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

    }
}