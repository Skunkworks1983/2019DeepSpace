package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Manipulator;

public class ToggleManipulatorExtended extends InstantCommand
{
    public ToggleManipulatorExtended(Manipulator manipulator)
    {
        super(manipulator, () -> manipulator.setExtended(!manipulator.getExtended()));
    }

    public ToggleManipulatorExtended()
    {
        this(Robot.getInstance().getManipulator());
    }
}
