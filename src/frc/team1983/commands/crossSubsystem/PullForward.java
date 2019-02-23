package frc.team1983.commands.crossSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorRollerThrottle;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.motors.ControlMode;

public class PullForward extends CommandGroup
{
    public PullForward()
    {
        Drivebase drivebase = Robot.getInstance().getDrivebase();
        Elevator elevator = Robot.getInstance().getElevator();
        Collector collector = Robot.getInstance().getCollector();

        addParallel(new SetCollectorRollerThrottle(collector, .5));

        //*How do I add this to run at the same time as the collector wheels?* make a command, velocity contrlmd
        drivebase.setRight(ControlMode.Throttle, .5);
        drivebase.setLeft(ControlMode.Throttle, .5);
    }
}
