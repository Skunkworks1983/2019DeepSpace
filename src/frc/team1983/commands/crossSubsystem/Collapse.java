package frc.team1983.commands.crossSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.subsystems.Elevator;

public class Collapse extends CommandGroup {

    private Elevator elevator;
    private Collector collector;
    private Drivebase drivebase;


    public Collapse()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Collector collector = Robot.getInstance().getCollector();
        Drivebase drivebase = Robot.getInstance().getDrivebase();

        addSequential(new SetElevatorPosition(elevator, 22));
        addSequential(new SetCollectorFolded(collector, true));
        addSequential(new SetElevatorPosition(elevator, 0));
        drivebase.setBrake(true);
    }


}
