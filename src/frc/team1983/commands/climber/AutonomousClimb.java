package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorRollerThrottle;
import frc.team1983.subsystems.Climber;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class AutonomousClimb extends CommandGroup
{
    public AutonomousClimb(Elevator elevator, Climber climber, Drivebase drivebase, Collector collector)
    {
        addSequential(new ClimbSetup());

        //LiftRobot CommandGroup

        //Drive Collector wheels forward (Parallel)
        // TODO: change from testing throttles
        addParallel(new SetCollectorRollerThrottle(collector, .5));
        drivebase.setLeft(ControlMode.Throttle, .5);
        drivebase.setRight(ControlMode.Throttle, .5);

        //Drivebase starts to roll
        drivebase.setLeft(ControlMode.Throttle, .5);
        drivebase.setRight(ControlMode.Throttle, .5);

        addSequential(new Collapse());
    }

    public AutonomousClimb()
    {
        this(Robot.getInstance().getElevator(), Robot.getInstance().getClimber(), Robot.getInstance().getDrivebase(), Robot.getInstance().getCollector());
    }
}

