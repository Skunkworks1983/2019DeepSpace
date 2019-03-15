package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Manipulator;

public class SetManipulatorOpen extends InstantCommand
{
    /**
     * This constructor allows you to set if the manipulator is opened or not
     *
     * @param manipulator The manipulator
     * @param open  If the manipulator should open or close
     */
    public SetManipulatorOpen(Manipulator manipulator, boolean open)
    {
        super(manipulator, () -> manipulator.setOpen(open));
    }

    public SetManipulatorOpen(boolean open)
    {
        this(Robot.getInstance().getManipulator(), open);
    }
}
