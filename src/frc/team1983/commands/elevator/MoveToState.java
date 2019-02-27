package frc.team1983.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.commands.collector.CollectionManager;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class MoveToState extends Command
{
    private Elevator elevator;
    private double position;
    private CollectionManager.State targetState;

    public MoveToState(Elevator elevator, double position, CollectionManager.State targetState)
    {
        this.elevator = elevator;
        this.position = position;
        this.targetState = targetState;
    }

    public MoveToState(double throttle, CollectionManager.State targetState)
    {
        this(Robot.getInstance().getElevator(), throttle, targetState);
    }

    @Override
    protected void initialize()
    {
        elevator.set(ControlMode.Position, position);

    }

    @Override
    protected void end()
    {
        elevator.set(ControlMode.Throttle, 0);
    }

    @Override
    protected boolean isFinished()
    {
        if (Robot.getInstance().getCollectionManager().currentState==targetState)
        {
            return true;
        }
        return false;
    }
}
