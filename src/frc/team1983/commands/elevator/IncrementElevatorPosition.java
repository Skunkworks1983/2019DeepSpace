package frc.team1983.commands.elevator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class IncrementElevatorPosition extends InstantCommand
{

    public IncrementElevatorPosition(Elevator elevator, double offset)
    {
        super(elevator, () -> elevator.set(ControlMode.Position, offset + elevator.getPosition()));
    }

    public IncrementElevatorPosition(double offset)
    {
        this(Robot.getInstance().getElevator(), offset);
    }
}
