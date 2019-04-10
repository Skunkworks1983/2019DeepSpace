package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;

public class RunGyroDrive extends Command
{
    private Drivebase drivebase;
    private OI oi;

	private double targetHeading = 0;

    public RunGyroDrive(Drivebase drivebase, OI oi)
    {
        requires(drivebase);

        this.drivebase = drivebase;
        this.oi = oi;
    }

    public RunGyroDrive()
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getOI());
    }

	@Override
	protected void initialize()
	{
		targetHeading = Robot.getInstance().getGyro().getHeading();
	}

    @Override
    protected void execute()
    {
		boolean slow = !oi.getButton(OI.Joysticks.LEFT, 1).get();

		double currentHeading = Robot.getInstance().getGyro().getHeading();
		double turnThrottle = 0.02 * (currentHeading - targetHeading);
		double driveThrottle = oi.getRightY() * (slow ? 0.65 : 1);

		if(Math.abs(oi.getLeftX()) > 0.05) 
		{
			turnThrottle = oi.getLeftX() * (slow ? 0.2 : 0.4);
			targetHeading = Robot.getInstance().getGyro().getHeading();
		}

		drivebase.setLeft(ControlMode.Throttle, driveThrottle + turnThrottle);
		drivebase.setRight(ControlMode.Throttle, driveThrottle - turnThrottle);
	}

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
    
	}
}
