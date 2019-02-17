package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.subsystems.Collector;

/**
 * Sets if the collector pistons are extended or not
 */
public class SetCollectorPistons extends Command
{
    private Collector collector;
    private boolean shouldExtend;

    /**
     * @param collector The collector
     * @param shouldExtend If the collector pistons should be extended or not
     */
    public SetCollectorPistons(Collector collector, boolean shouldExtend)
    {
        this.collector = collector;
        this.shouldExtend = shouldExtend;
    }

    /**
     * This constructor toggles the current state of the pistons
     */
    public SetCollectorPistons(Collector collector)
    {
        this(collector, !collector.isPistonExtended());
    }

    protected void initialize()
    {
        collector.setPiston(shouldExtend);
    }

    @Override
    protected boolean isFinished()
    {
        return true;
    }
}
