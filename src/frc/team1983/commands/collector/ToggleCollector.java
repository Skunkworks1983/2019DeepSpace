package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Collector;

public class ToggleCollector extends InstantCommand
{
    public ToggleCollector(Collector collector)
    {
        super(collector, () -> collector.setFolded(!collector.isFolded()));
    }

    public ToggleCollector()
    {
        this(Robot.getInstance().getCollector());
    }
}