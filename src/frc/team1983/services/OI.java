package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import java.util.HashMap;

public class OI
{
    public static class Map
    {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int PANEL = 2;
    }

    public static final double JOYSTICK_DEADZONE = 0.1;
    public static final double JOYSTICK_EXPONENT = 2;

    private Joystick left, right, panel;
    private HashMap<Integer, HashMap<Integer, JoystickButton>> buttons;

    public OI()
    {
        left = new Joystick(Map.LEFT);
        right = new Joystick(Map.RIGHT);
        panel = new Joystick(Map.PANEL);

        buttons = new HashMap<>();
    }

    public double getLeftY()
    {
        return left.getY();
    }

    public double getRightY()
    {
        return right.getY();
    }

    public JoystickButton getButton(int joystickPort, int button)
    {
        Joystick joystick = null;
        switch(joystickPort)
        {
            case Map.LEFT:
                joystick = left;
                break;
            case Map.RIGHT:
                joystick = right;
                break;
            case Map.PANEL:
                joystick = panel;
                break;
        }

        if(joystick == null)
            throw new IllegalArgumentException("Joystick on port " + joystickPort + " does not exist");

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
