package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import frc.team1983.constants.OIConstants;

public class OI
{
    private Joystick left, right, panel;

    public OI()
    {
        left = new Joystick(OIConstants.Map.LEFT);
        right = new Joystick(OIConstants.Map.LEFT);
        panel = new Joystick(OIConstants.Map.LEFT);
    }

    public double getLeftY()
    {
        return left.getY();
    }

    public double getRightY()
    {
        return right.getY();
    }
}
