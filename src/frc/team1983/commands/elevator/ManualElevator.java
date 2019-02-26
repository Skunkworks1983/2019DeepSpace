package frc.team1983.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class ManualElevator extends Command
{
    private Elevator elevator;
    private double throttle;

    public ManualElevator(Elevator elevator, double throttle)
    {
        this.elevator = elevator;
        this.throttle = throttle;
    }

    public ManualElevator(double throttle)
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
