package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.Pair;
import frc.team1983.utilities.control.PurePursuitController;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.pathing.Path;

public class DrivePath extends Command
{
    public final static double DEADZONE_TIME = 5.0; // seconds

    protected Drivebase drivebase;
    protected StateEstimator estimator;
    protected Path path;
    protected double velocity;

    private double time;
    private double lastTime;
    private double dt;
    private double endTimer;

    public DrivePath(Drivebase drivebase, StateEstimator estimator, Path path, double velocity)
    {
        requires(drivebase);

        this.drivebase = drivebase;
        this.estimator = estimator;
        this.path = path;
        this.velocity = velocity;
    }

    public DrivePath(Path path, double velocity)
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getEstimator(), path, velocity);
    }

    @Override
    protected void initialize()
    {
        lastTime = RobotController.getFPGATime();
    }

    @Override
    protected void execute()
    {
        time = RobotController.getFPGATime();
        dt = time - lastTime;

        Pair output = PurePursuitController.evaluateOutput(estimator.getCurrentPose(), path, velocity);

        drivebase.setLeft(ControlMode.Throttle, (double) output.getValue1() / Drivebase.MAX_VELOCITY);
        drivebase.setRight(ControlMode.Throttle, (double) output.getValue2() / Drivebase.MAX_VELOCITY);

        if(PurePursuitController.inDeadzone(estimator.getCurrentPose(), path))
            endTimer += dt;
        else
            endTimer = 0;

        lastTime = time;
    }

    @Override
    protected boolean isFinished()
    {
        return endTimer > DEADZONE_TIME;
    }

    @Override
    protected void end()
    {
        drivebase.setLeft(ControlMode.Throttle, 0);
        drivebase.setRight(ControlMode.Throttle, 0);
    }
}
