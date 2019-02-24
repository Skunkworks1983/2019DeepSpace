package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Climber;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;

public class Climb extends Command
{
    private Climber climber;
    private Collector collector;
    private Drivebase drivebase;

    public Climb(Climber climber, Collector collector, Drivebase drivebase)
    {
        this.climber = climber;
        this.collector = collector;
        this.drivebase = drivebase;
    }

    public Climb()
    {
        this(Robot.getInstance().getClimber(), Robot.getInstance().getCollector(), Robot.getInstance().getDrivebase());
    }

    @Override
    protected void initialize()
    {

    }

    @Override
    protected void execute()
    {
        climber.set(ControlMode.Position, -10);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {

    }
}

