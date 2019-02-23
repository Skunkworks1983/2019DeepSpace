package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Collector;

/**
 * Sets if the collector pistons are extended or not
 */
public class SetCollectorFolded extends InstantCommand
{
    /**
     * @param collector The collector
     * @param folded If the collector pistons should be extended or not
     */
    public SetCollectorFolded(Collector collector, boolean folded)
    {
        super(collector, () -> collector.setFolded(folded));
    }

    /**
     * This constructor toggles the current state of the pistons
     */
    public SetCollectorFolded(boolean folded)
    {
        this(Robot.getInstance().getCollector(), folded);
    }
}
