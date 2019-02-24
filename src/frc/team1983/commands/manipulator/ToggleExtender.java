package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Manipulator;

public class ToggleExtender extends InstantCommand
{
    public ToggleExtender(Manipulator manipulator)
    {
        super(manipulator, () -> manipulator.setExtender(!manipulator.getExtender()));
    }

    public ToggleExtender()
    {
        this(Robot.getInstance().getManipulator());
    }
}

