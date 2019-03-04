package frc.team1983.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class SetElevatorThrottle extends Command
{
    private Elevator elevator;
    private double throttle;

    public SetElevatorThrottle(Elevator elevator, double throttle)
    {
        this.elevator = elevator;
        this.throttle = throttle;
    }

    public SetElevatorThrottle(double throttle)
    {
        this(Robot.getInstance().getElevator(), throttle);
    }

    @Override
    protected void initialize()
    {
        elevator.set(ControlMode.Throttle, throttle);

    }

    @Override
    protected void end()
    {
        elevator.set(ControlMode.Throttle, 0);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
