package frc.team1983.commands.drivebase;

import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;

public class DriverSwitch extends RunTankDrive
{
    protected StateEstimator estimator;

    public DriverSwitch(Drivebase drivebase, OI oi, StateEstimator estimator)
    {
        super(drivebase, oi);
        this.estimator = estimator;
    }

    public DriverSwitch()
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getOI(), Robot.getInstance().getEstimator());
    }

    @Override
    protected boolean isFinished()
    {
        return oi.getButton(OI.Joysticks.LEFT, OI.JOYSTICK_BOTTOM_BUTTON).get() || oi.getButton(OI.Joysticks.RIGHT, OI.JOYSTICK_BOTTOM_BUTTON).get();
    }
}
