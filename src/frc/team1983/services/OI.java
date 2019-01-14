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
        left = new Joystick(Map.LEFT.PORT);
        right = new Joystick(Map.RIGHT.PORT);
        panel = new Joystick(Map.PANEL.PORT);
    }

    public double getLeftY()
    {
        return left.getY();
    }

    public double getRightY()
    {
        return right.getY();
    }

    public enum Map
    {
        LEFT(0),
        RIGHT(0),
        PANEL(0)
        ;

        final int PORT;
        private Map(int port)
        {
            this.PORT = port;
        }
    }
}
