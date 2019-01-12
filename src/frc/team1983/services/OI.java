package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import frc.team1983.Constants;

public class OI
{
    private JoystickWrapper left, right, panel;

    public OI()
    {
        left = new JoystickWrapper(Constants.OI.Map.LEFT);
        right = new JoystickWrapper(Constants.OI.Map.RIGHT);
        panel = new JoystickWrapper(Constants.OI.Map.PANEL);
    }
}
