package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Collector;

public class ToggleCollectorFolded extends InstantCommand
{
    public ToggleCollectorFolded(Collector collector)
    {
        super(collector, () -> collector.setFolded(!collector.isFolded()));
    }

    public ToggleCollectorFolded()
    {
        this(Robot.getInstance().getCollector());
    }
}