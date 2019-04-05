package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Climber;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.sensors.Gyro;

public class FakeClimb extends Command
{
    private Climber climber;
    private Collector collector;
    private Drivebase drivebase;
    private Gyro gyro;
    private double climbHeight;

    public FakeClimb(Climber climber, Collector collector, Drivebase drivebase, Gyro gyro, double climbHeight)
    {
        this.climber = climber;
        this.collector = collector;
        this.drivebase = drivebase;
        this.gyro = gyro;
        this.climbHeight = climbHeight;
    }

    public FakeClimb(double climbHeight)
    {
        this(Robot.getInstance().getClimber(), Robot.getInstance().getCollector(), Robot.getInstance().getDrivebase(), Robot.getInstance().getGyro(), climbHeight);
    }

    @Override
    public void initialize()
    {
        collector.climbing = true;
        collector.setWristBrake(true);
        collector.setAngle(180);
    }

    @Override
    public void execute()
    {
        if(collector.getAngle() < 175)
        {
            climber.set(ControlMode.Position, 0);
        }
        else
        {
            double throttle = gyro.getPitch() / 3;

            if(collector.getAngle() < 90 && throttle < 0) throttle = 0;
            if(collector.getAngle() > 250 && throttle > 0) throttle = 0;
            collector.setWristThrottle(throttle);

            climber.set(ControlMode.Position, climbHeight);
        }
    }

    @Override
    protected boolean isFinished()
    {
        return !Robot.getInstance().getOI().getButton(OI.Joysticks.LEFT, 4).get();
    }

    @Override
    public void end()
    {
        collector.climbing = true;
        collector.setAngle(Collector.Setpoints.STOW_UPPER);
        climber.set(ControlMode.Position, 0);
        collector.setWristBrake(false);
    }
}
