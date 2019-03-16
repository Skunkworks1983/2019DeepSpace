package frc.team1983.commands.drivebase;

import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.pathing.Pose;

public class DriverSwitch extends RunTankDrive
{
    protected StateEstimator estimator;
    protected Pose resetPose;
    protected OI.Joysticks joy;
    protected int button;

    public DriverSwitch(Drivebase drivebase, OI oi, StateEstimator estimator)
    {
        super(drivebase, oi);
        this.estimator = estimator;
        this.resetPose = resetPose;
        this.joy = joy;
        this.button = button;
    }

    public DriverSwitch()
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getOI(), Robot.getInstance().getEstimator());
    }

    @Override
    protected boolean isFinished()
    {
        return oi.getButton(OI.Joysticks.LEFT, OI.JOYSTICK_TOP_BUTTON).get() || oi.getButton(OI.Joysticks.RIGHT, OI.JOYSTICK_TOP_BUTTON).get();
    }
}
