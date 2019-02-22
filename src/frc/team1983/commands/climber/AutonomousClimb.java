package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.commands.collector.SetCollectorRollerThrottle;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.subsystems.Climber;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.Talon;

public class AutonomousClimb extends CommandGroup
{

    private Elevator elevator;
    private Climber climber;
    private Drivebase drivebase;
    private Collector collector;

       public AutonomousClimb()
       {
           elevator = Robot.getInstance().getElevator();
           climber = Robot.getInstance().getClimber();
           drivebase = Robot.getInstance().getDrivebase();
           collector = Robot.getInstance().getCollector();


           //Raises elevator for collector deploy clearance.
           addSequential(new SetElevatorPosition(elevator, 22));

           //Unfolds collector to prepare for "hooking" onto hab.
           //*At what distance from the hab should we unfold?*
           addSequential(new SetCollectorFolded(collector, false));

           //Lowers Elevator back to original height
           addSequential(new SetElevatorPosition(elevator, 0));


           //Raise Climber *ADD PARALLEL*
           addSequential(new LiftEngage(climber, .5));
           //Raise Collector *ADD PARALLEL*


           //Drive Collector wheels forward (Parallel) **CURRENTLY USING TESTING THROTTLES**
           addParallel(new SetCollectorRollerThrottle(collector, .5));

           //*How do I create a run time for both of them?^*
           //Drivebase starts to roll
           drivebase.setLeft(ControlMode.Throttle, .5);
           drivebase.setRight(ControlMode.Throttle, .5);


           drivebase.setLeft(ControlMode.Throttle, .5);
           drivebase.setRight(ControlMode.Throttle, .5);
           addParallel(new SetCollectorFolded(collector, true));


           //Stop robot from rolling off the hab
           drivebase.setBrake(true);
       }
}

