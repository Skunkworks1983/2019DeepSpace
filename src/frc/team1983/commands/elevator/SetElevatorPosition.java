package frc.team1983.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

import static java.lang.Math.abs;

public class SetElevatorPosition extends Command
{
    private Elevator elevator;
    private double height;

    public SetElevatorPosition(Elevator elevator, double setpoint)
    {
        this.elevator = elevator;
        this.height = setpoint;
    }

    public SetElevatorPosition(double setpoint)
    {
        this(Robot.getInstance().getElevator(), setpoint);
    }

    @Override
    public void initialize()
    {
        elevator.setHeight(height);
    }

    @Override
    protected boolean isFinished()
    {
        return elevator.isAtSetpoint();
    }
}
