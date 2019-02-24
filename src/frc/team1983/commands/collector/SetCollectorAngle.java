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
    }

    public SetCollectorAngle(double angle)
    {
        this(Robot.getInstance().getCollector(), angle);
    }
}
