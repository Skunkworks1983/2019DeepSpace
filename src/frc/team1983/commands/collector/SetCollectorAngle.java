package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Collector;

/**
 * Sets the angle of the collector
 */
public class SetCollectorAngle extends InstantCommand
{
    public SetCollectorAngle(Collector collector, double angle)
    {
        super(collector, () -> collector.setAngle(angle));
        if (collector.getAngle() > 80)
        {
            collector.setFolded(false);
        }
        if (collector.getAngle() < 90)
        {
            collector.setFolded(true);
        }
    }

    public SetCollectorAngle(double angle)
    {
        this(Robot.getInstance().getCollector(), angle);
    }
}
