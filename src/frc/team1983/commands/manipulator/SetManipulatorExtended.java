package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Manipulator;

public class SetManipulatorExtended extends InstantCommand
{
    public SetManipulatorExtended(Manipulator manipulator, boolean extended)
    {
        super(manipulator, () -> manipulator.setExtender(true));
    }

    public SetManipulatorExtended(boolean extended)
    {
        this(Robot.getInstance().getManipulator(), extended);
    }
}
