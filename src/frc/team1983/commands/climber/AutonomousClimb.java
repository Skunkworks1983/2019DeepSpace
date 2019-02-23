package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.commands.collector.SetCollectorRollerThrottle;
import frc.team1983.commands.crossSubsystem.ClimbSetup;
import frc.team1983.commands.crossSubsystem.Collapse;
import frc.team1983.commands.crossSubsystem.PullForward;
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

           //"Grapples onto hab module"
           addSequential(new ClimbSetup());

           //Drive Collector wheels forward
           addSequential(new PullForward());

           //Retracts collector and sets motor brakes.
           addSequential(new Collapse());
       }
}

