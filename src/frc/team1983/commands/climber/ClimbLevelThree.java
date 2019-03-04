package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorAngle;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Climber;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.sensors.Gyro;

public class ClimbLevelThree extends Command
{
    private Climber climber;
    private Collector collector;
    private Drivebase drivebase;
    private Gyro gyro;
    private  double throttle;

    public ClimbLevelThree(Climber climber, Collector collector, Drivebase drivebase, Gyro gyro)
    {
        this.climber = climber;
        this.collector = collector;
        this.drivebase = drivebase;
        this.gyro = gyro;
    }

    public ClimbLevelThree()
    {
        this(Robot.getInstance().getClimber(), Robot.getInstance().getCollector(), Robot.getInstance().getDrivebase(), Robot.getInstance().getGyro());
    }

    @Override
    public void initialize()
    {
        Robot.getInstance().getCollector().climbing = true;
        climber.set(ControlMode.Position, -25.5);
        gyro.setPitch(0);
    }

    @Override
    protected void execute()
    {
        throttle = gyro.getPitch() / 4;

        if (collector.getAngle() < 90 && throttle < 0) throttle = 0;
        if (collector.getAngle() > 190 && throttle > 0) throttle = 0;
        collector.setWristThrottle(throttle);

        if(climber.getPosition() <= -16)
        {
            drivebase.set(ControlMode.Throttle, 0.2);
            collector.setRollerThrottle(1);
        }
        else
        {
            collector.setRollerThrottle(0.4);
        }
    }

    @Override
    protected boolean isFinished()
    {
        return !Robot.getInstance().getOI().getButton(OI.Joysticks.PANEL, OI.CLIMB).get();
    }

    @Override
    protected void end()
    {
        Robot.getInstance().getCollector().climbing = false;
        collector.setWristThrottle(0);
        collector.setRollerThrottle(0);
        climber.set(ControlMode.Position, 0);
        drivebase.setBrake(true);
    }
}
