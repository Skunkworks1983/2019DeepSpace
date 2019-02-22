package frc.team1983.commands.crossSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Elevator;

public class ClimbSetup extends Command
{
    private Elevator elevator;
    private Collector collector;

    public ClimbSetup()
    {
        elevator = Robot.getInstance().getElevator();
        collector = Robot.getInstance().getCollector();
    }

    @Override
    protected void initialize()
    {

    }

    @Override
    protected void execute()
    {

    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end() {

    }
}
