package frc.team1983.commands.drivebase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Drivebase;

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
    public void execute()
    {
        drivebase.setLeft(ControlMode.PercentOutput, oi.getLeftY());
        drivebase.setRight(ControlMode.PercentOutput, oi.getRightY());
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }

    @Override
    public void end()
    {
        drivebase.setLeft(ControlMode.PercentOutput, 0);
        drivebase.setRight(ControlMode.PercentOutput, 0);
    }
}
