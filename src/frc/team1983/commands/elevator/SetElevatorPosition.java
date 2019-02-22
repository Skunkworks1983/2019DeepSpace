package frc.team1983.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class SetElevatorPosition extends InstantCommand
{
    /**
     *
     * @param elevator The elevator object
     * @param setpoint The height of the carriage in inches from the bottom stage
     */
    public SetElevatorPosition(Elevator elevator, double setpoint)
    {
        super(elevator, () -> elevator.set(ControlMode.Position, setpoint));
    }

    public SetElevatorPosition(double setpoint)
    {
        this(Robot.getInstance().getElevator(), setpoint);
    }
}
