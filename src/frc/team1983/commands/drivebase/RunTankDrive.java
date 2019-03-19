package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;

public class RunTankDrive extends Command
{
    private Drivebase drivebase;
    private OI oi;

    public RunTankDrive(Drivebase drivebase, OI oi)
    {
        requires(drivebase);

        this.drivebase = drivebase;
        this.oi = oi;
    }

    public RunTankDrive()
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getOI());
    }

    @Override
    protected void execute()
    {
        drivebase.set(ControlMode.Throttle, oi.getLeftY(), oi.getRightY());
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        drivebase.set(ControlMode.Throttle, 0, 0);
    }
}
