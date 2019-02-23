package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Collector;

/**
 * Sets the throttle of the wrist. Also resets the throttle to 0 when the command is canceled (for instance when
 * using whileHeld on a button).
 */
public class SetCollectorWristThrottle extends Command
{
    private Collector collector;
    private double throttle;

    public SetCollectorWristThrottle(Collector collector, double throttle)
    {
        this.collector = collector;
        this.throttle = throttle;
    }

    public SetCollectorWristThrottle(double throttle)
    {
        this(Robot.getInstance().getCollector(), throttle);
    }

    @Override
    protected void initialize()
    {
        collector.setWristThrottle(throttle);
    }

    @Override
    protected void end()
    {
        collector.setWristThrottle(0);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
