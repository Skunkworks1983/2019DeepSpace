package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Manipulator;

public class ToggleHooks extends InstantCommand
{
    public ToggleHooks(Manipulator manipulator)
    {
        super(manipulator, () -> manipulator.setHooks(!manipulator.getHooks()));
    }

    public ToggleHooks()
    {
        this(Robot.getInstance().getManipulator());
    }
}
