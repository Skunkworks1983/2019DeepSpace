package frc.team1983.commands.Climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Climber;

/**
 * Sets the throttle of the climbing elevator. Also resets the throttle to 0 when the command is canceled (for instance when
 * using whileHeld on a button).
 */
public class ManualClimber extends Command
{
    private Climber climber;
    private double throttle;

    public ManualClimber(Climber climber, double throttle)
    {
        this.climber = climber;
        this.throttle = throttle;
    }

    public ManualClimber(double throttle)
    {
        this(Robot.getInstance().getClimber(), throttle);
    }

    @Override
    protected void initialize()
    {
        climber.setThrottle(throttle);

    }

    @Override
    protected void end()
    {
        climber.setThrottle(0);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
