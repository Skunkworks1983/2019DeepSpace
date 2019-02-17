package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.subsystems.Collector;

/**
 * Sets the angle of the collector
 */
public class SetCollectorAngle extends Command
{
    private Collector collector;
    private double angle;

    public SetCollectorAngle(Collector collector, double angle)
    {
        this.collector = collector;
        this.angle = angle;
    }

    @Override
    protected void initialize()
    {
        collector.setAngle(angle);
    }

    @Override
    protected boolean isFinished()
    {
        return true;
    }
}
