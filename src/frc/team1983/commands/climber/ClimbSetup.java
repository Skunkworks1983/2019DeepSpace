package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Elevator;

public class ClimbSetup extends CommandGroup
{
    public ClimbSetup(Elevator elevator, Collector collector)
    {
        //Raises elevator for collector deploy clearance.
        addSequential(new SetElevatorPosition(elevator, 22));

        //Unfolds collector to prepare for "hooking" onto hab.
        //*At what distance from the hab should we unfold?*
        addSequential(new SetCollectorFolded(collector, false));
        //Lowers Elevator back to original height
        addSequential(new SetElevatorPosition(elevator, 0));
    }

    public ClimbSetup()
    {
        this(Robot.getInstance().getElevator(), Robot.getInstance().getCollector());
    }
}
