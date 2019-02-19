package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Collector;

/**
 * Sets the throttle of the rollers. Also resets the throttle to 0 when the command is canceled (for instance when
 * using whileHeld on a button).
 */
public class SetCollectorRollerThrottle extends Command
{
    private Collector collector;
    private double throttle;

    public SetCollectorRollerThrottle(Collector collector, double throttle)
    {
        this.collector = collector;
        this.throttle = throttle;
    }

    public SetCollectorRollerThrottle(double throttle)
    {
        this(Robot.getInstance().getCollector(), throttle);
    }

    @Override
    protected void initialize()
    {
        collector.setRollerThrottle(throttle);
    }

    @Override
    protected void end()
    {
        collector.setRollerThrottle(0);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
