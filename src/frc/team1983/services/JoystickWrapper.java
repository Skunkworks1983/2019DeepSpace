package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import java.util.ArrayList;

public class JoystickWrapper extends Joystick {
    private ArrayList<Button> buttons;

    public JoystickWrapper(final int port)
    {
        super(port);

        buttons = new ArrayList<>();

        int i = 0;
        while(true)
        {
            try
            {
                buttons.add(new JoystickButton(this, i++));
            }
            catch(Exception e)
            {
                break;
            }
        }
    }

    public Button getButton(int index)
    {
        return buttons.get(index);
    }
}
