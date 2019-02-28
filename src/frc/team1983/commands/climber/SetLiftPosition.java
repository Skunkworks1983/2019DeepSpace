package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.subsystems.Climber;
import frc.team1983.utilities.motors.ControlMode;

public class SetLiftPosition extends InstantCommand
{
    public SetLiftPosition(Climber climber, double setpoint)
    {
        super(climber, () -> climber.set(ControlMode.Position, setpoint));
    }

    public SetLiftPosition(double setpoint)
    {

        this(Robot.getInstance().getClimber(), setpoint);
    }
}
