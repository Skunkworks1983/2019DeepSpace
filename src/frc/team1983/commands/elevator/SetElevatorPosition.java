package frc.team1983.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

import static java.lang.Math.abs;

public class SetElevatorPosition extends Command
{
    private Elevator elevator;
    private double setpoint;

    /**
     *
     * @param setpoint The height of the carriage in inches from the bottom stage
     */
    public SetElevatorPosition(double setpoint)
    {
        elevator = Robot.getInstance().getElevator();
        this.setpoint = setpoint;
    }

    @Override
    public void initialize()
    {
        elevator.set(ControlMode.Position, setpoint);
    }

    @Override
    protected boolean isFinished()
    {
        System.out.println("ELEVATOR AT SETPOINT: " + elevator.isAtSetpoint());
        return elevator.isAtSetpoint();
    }
}
