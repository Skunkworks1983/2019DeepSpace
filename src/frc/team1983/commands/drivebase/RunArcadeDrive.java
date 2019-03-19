package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;

public class RunArcadeDrive extends Command
{
    private Drivebase drivebase;
    private OI oi;
    private final double spinThreshold;

    public RunArcadeDrive(Drivebase drivebase, OI oi, double spinThreshold)
    {
        this.drivebase = drivebase;
        this.oi = oi;
        this.spinThreshold = spinThreshold;
    }

    public RunArcadeDrive()
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getOI(), 0.9);
    }

    @Override
    protected void initialize()
    {
        requires(drivebase);
    }

    @Override
    protected void execute()
    {
        double throttle = oi.getRightY();
        double x = oi.getRightX();

        if(x > 0)
            drivebase.set(ControlMode.Throttle, throttle, throttle * (1 - (Math.abs(x) / spinThreshold)));
        else
            drivebase.set(ControlMode.Throttle, throttle * (1 - (Math.abs(x) / spinThreshold)), throttle);
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
