package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Manipulator;

public class SetHooksOpen extends InstantCommand
{
    /**
     * This constructor allows you to set if the manipulator is opened or not
     *
     * @param manipulator The manipulator
     * @param open  If the manipulator should open or close
     */
    public SetHooksOpen(Manipulator manipulator, boolean open)
    {
        super(manipulator, () -> manipulator.setHooks(open));
    }

    public SetHooksOpen(boolean open)
    {
        this(Robot.getInstance().getManipulator(), open);
    }
}
