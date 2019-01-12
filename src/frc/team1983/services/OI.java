package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import frc.team1983.Constants;

public class OI
{
    private Joystick left, right, panel;

    public OI()
    {
        left = new Joystick(Constants.OI.Map.LEFT);
        right = new Joystick(Constants.OI.Map.LEFT);
        panel = new Joystick(Constants.OI.Map.LEFT);
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
