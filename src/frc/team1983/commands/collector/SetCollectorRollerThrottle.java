package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.subsystems.Collector;

public class SetCollectorRollerThrottle extends Command
{
    private Collector collector;
    private double throttle;

    public SetCollectorRollerThrottle(Collector collector, double throttle)
    {
        this.collector = collector;
        this.throttle = throttle;
    }

    @Override
    protected void initialize()
    {
        collector.setRollerThrottle(throttle);
    }

    @Override
    protected void interrupted()
    {
        collector.setRollerThrottle(0);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
