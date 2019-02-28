package frc.team1983.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.collector.CollectionManager;
import frc.team1983.commands.collector.SetCollectorAngle;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Elevator;

public class SafeAutomationManager
{
    public CollectionManager collectionManager;

    public SafeAutomationManager()
    {
        collectionManager = Robot.getInstance().getCollectionManager();
    }

    //state management//

    public CommandGroup moveEleDZWhileCollectorFolding(double value)
    {
        CommandGroup group = new CommandGroup();

        group.addSequential(new SetCollectorAngle(Collector.DOWN_ANGLE));
        group.addParallel(new SetCollectorFolded(true));
      //  addSequential(new WaitForState(CollectionManager.State.E_RISING__COL_SAFE));
        group.addSequential(new SetElevatorPosition(value));

        return group;
    }
    public CommandGroup moveEleDZWhileCollectorUnfolding(double value)
    {
        CommandGroup group = new CommandGroup();

        if(Robot.getInstance().getCollector().getAngle() > Collector.DANGERZONE_ANGLE *0.75)
        {
            group.addSequential(new SetCollectorAngle(Collector.DOWN_ANGLE));
            group.addParallel(new SetCollectorFolded(true));
          //  addSequential(new WaitForState(CollectionManager.State.E_SAFE__COL_DZ));
            group.addSequential(new SetElevatorPosition(value));
        }
        else
        {
            group.addSequential(new SetCollectorAngle(0));
            group.addParallel(new SetCollectorFolded(false));
           // addSequential(new WaitForState(CollectionManager.State.E_SAFE__COL_SAFE));
            group.addSequential(new SetElevatorPosition(value));
        }
        return group;
    }

    public CommandGroup moveCollectorDZWhileEleInIllegalState(double angle)
    {
        CommandGroup group = new CommandGroup();
        group.addSequential(new SetElevatorPosition(Elevator.DANGERZONE_HEIGHT));
        group.addSequential(new SetCollectorAngle(angle));

        return group;
    }

}
