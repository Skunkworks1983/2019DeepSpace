package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Manipulator;

public class SetManipulatorOpen extends InstantCommand
{
    public SetManipulatorOpen(Manipulator manipulator, boolean open)
    {
        super(manipulator, () -> manipulator.setOpen(open));
    }

    public SetManipulatorOpen(boolean open)
    {
        this(Robot.getInstance().getManipulator(), open);
    }
}
