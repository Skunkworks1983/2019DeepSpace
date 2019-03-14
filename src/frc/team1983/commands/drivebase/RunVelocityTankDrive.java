package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;

public class RunVelocityTankDrive extends Command
{
    private Drivebase drivebase;
    private OI oi;
    private double maxVelocity;

    public RunVelocityTankDrive(Drivebase drivebase, OI oi, double maxVelocity)
    {
        this.drivebase = drivebase;
        this.oi = oi;
        this.maxVelocity = maxVelocity;
    }

    public RunVelocityTankDrive()
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getOI(), Drivebase.MAX_VELOCITY);
    }

    @Override
    protected void execute()
    {
        drivebase.set(ControlMode.Velocity,
                oi.getLeftY() * maxVelocity, oi.getRightY() * maxVelocity);
    }

    @Override
    protected void end()
    {
        drivebase.set(ControlMode.Throttle, 0, 0);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
