package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;

public class OI
{
    public static final double JOY_DEADZONE = 0;
    public static final double JOY_SCALAR = 0;
    public static final double JOY_EXPONENT = 0;
    private Joystick left, right, panel;

    public OI()
    {
        left = new Joystick(Map.LEFT);
        right = new Joystick(Map.RIGHT);
        panel = new Joystick(Map.PANEL);
    }

    public double getLeftY()
    {
        return left.getY();
    }

    public double getRightY()
    {
        return right.getY();
    }

    public static class Map
    {
        public static final int LEFT = 0;
        public static final int RIGHT = 0;
        public static final int PANEL = 0;
    }
}
