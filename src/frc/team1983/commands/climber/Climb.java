package frc.team1983.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Climber;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.sensors.Gyro;

public class Climb extends Command
{
    private Climber climber;
    private Collector collector;
    private Drivebase drivebase;
    private Gyro gyro;
    private double climbHeight;
    private double driveHeight;

    public Climb(Climber climber, Collector collector, Drivebase drivebase, Gyro gyro, double climbHeight, double driveHeight)
    {
        this.climber = climber;
        this.collector = collector;
        this.drivebase = drivebase;
        this.gyro = gyro;
        this.climbHeight = climbHeight;
        this.driveHeight = driveHeight;
    }

    public Climb(double climbHeight, double driveHeight)
    {
        this(Robot.getInstance().getClimber(), Robot.getInstance().getCollector(), Robot.getInstance().getDrivebase(), Robot.getInstance().getGyro(), climbHeight, driveHeight);
    }

    @Override
    public void initialize()
    {
        Robot.getInstance().getCollector().climbing = true;
        gyro.setPitch(0);
        climber.set(ControlMode.Position, climbHeight);
    }

    @Override
    protected void execute()
    {
        double throttle = gyro.getPitch() / 4;

        if (collector.getAngle() < 90 && throttle < 0) throttle = 0;
        if (collector.getAngle() > 190 && throttle > 0) throttle = 0;
        collector.setWristThrottle(throttle);

        collector.setRollerThrottle(climber.getPosition() > driveHeight ? 1 : 0.4);

        if(climber.getPosition() <= driveHeight)
            drivebase.set(ControlMode.Throttle, 0.2);
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
