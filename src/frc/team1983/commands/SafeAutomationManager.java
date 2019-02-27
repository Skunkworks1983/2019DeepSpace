package frc.team1983.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team1983.Robot;
import frc.team1983.commands.collector.CollectionManager;
import frc.team1983.commands.collector.SetCollectorAngle;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.constants.CollectorConstants;
import frc.team1983.constants.ElevatorConstants;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class SafeAutomationManager extends CommandGroup
{
    public CollectionManager collectionManager;
    public Elevator elevator;
    public Collector collector;

    public SafeAutomationManager()
    {
        collectionManager = Robot.getInstance().getCollectionManager();
        elevator = Robot.getInstance().getElevator();
        collector = Robot.getInstance().getCollector();
    }

    //state management//

    public void moveEleDZWhileCollectorFolding(double value)
    {
        addSequential(new SetCollectorAngle(CollectorConstants.WristSetpoints.DOWN));
        addParallel(new SetCollectorFolded(true));
        addSequential(new SetElevatorPosition(value));
    }
    public void moveEleDZWhileCollectorUnfolding(double value)
    {
        if(collector.getAngle() > CollectorConstants.WristSetpoints.DZ*0.75)
        {
            addSequential(new SetCollectorAngle(CollectorConstants.WristSetpoints.DOWN));
            addParallel(new SetCollectorFolded(true));
            addSequential(new SetElevatorPosition(value));
        }
        else
        {
            addSequential(new SetCollectorAngle(CollectorConstants.WristSetpoints.BACK));
            addParallel(new SetCollectorFolded(false));
            addSequential(new SetElevatorPosition(value));
        }
    }

    public void moveCollectorDZWhileEleInIllegalState(double angle)
    {
        System.out.println("TRYING TO RUN AUTOMATION");
        addSequential(new SetElevatorPosition(ElevatorConstants.SetPoints.ELE_SAFE));
        addSequential(new SetCollectorAngle(angle));
        Scheduler.getInstance().add(this);
    }

}
