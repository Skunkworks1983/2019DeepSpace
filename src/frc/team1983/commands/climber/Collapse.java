package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.subsystems.Elevator;

public class Collapse extends CommandGroup
{
    public Collapse(Elevator elevator, Collector collector, Drivebase drivebase)
    {
        addSequential(new SetElevatorPosition(elevator, 22));
        addSequential(new SetCollectorFolded(collector, true));
        addSequential(new SetElevatorPosition(elevator, 0));
        drivebase.setBrake(true);
    }

    public Collapse()
    {
        this(Robot.getInstance().getElevator(), Robot.getInstance().getCollector(), Robot.getInstance().getDrivebase());
    }
}
