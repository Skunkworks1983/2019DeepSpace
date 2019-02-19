package frc.team1983.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class SetElevatorPosition extends Command
{
    private Elevator elevator;
    public double setpoint;

    public SetElevatorPosition(Elevator elevator, double setpoint)
    {
        requires(elevator);

        this.elevator = elevator;
        this.setpoint = setpoint;
    }

    public SetElevatorPosition(double setpoint)
    {
        this(Robot.getInstance().getElevator(), setpoint);
    }

    @Override
    public void initialize()
    {
        elevator.set(ControlMode.Position, setpoint);
    }

    @Override
    protected boolean isFinished()
    {
        return true;
    }
}
