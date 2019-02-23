package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Climber;

public class LiftEngage extends Command
{
    private Climber climber;
    private double throttle;

    public LiftEngage(Climber climber, double throttle)
    {
        this.climber = climber;
        this.throttle = throttle;
    }
    
    public LiftEngage(double throttle)
    {
        this(Robot.getInstance().getClimber(), throttle);
    }

    @Override
    protected void initialize()
    {
        climber.setThrottle(throttle);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        climber.setThrottle(0);
    }
}
