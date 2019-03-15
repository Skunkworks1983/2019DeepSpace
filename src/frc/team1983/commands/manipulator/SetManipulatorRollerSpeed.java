package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.subsystems.Manipulator;

/**
 * Sets the speed of the grippers on the manipulator
 */
public class SetManipulatorRollerSpeed extends Command
{
    private Manipulator manipulator;
    private double leftSpeed;
    private double rightSpeed;
    private boolean zeroOnFinish;

    /**
     * This constructor allows setting both gripper speeds independently
     *
     * @param manipulator     The manipulator
     * @param leftSpeed       The percent output of the left gripper
     * @param rightSpeed      The percent output of the right gripper
     * @param zeroOnInterrupt If the command should run forever and zero the grippers when it ends
     */
    public SetManipulatorRollerSpeed(Manipulator manipulator, double leftSpeed, double rightSpeed, boolean zeroOnInterrupt)
    {
        this.manipulator = manipulator;
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
        this.zeroOnFinish = zeroOnInterrupt;
    }

    /**
     * This constructor allows setting both gripper speeds to the same value
     *
     * @param manipulator     The manipulator
     * @param speed           The percent output of both grippers
     * @param zeroOnInterrupt If the command should run forever and zero the grippers when it is interrupted
     */
    public SetManipulatorRollerSpeed(Manipulator manipulator, double speed, boolean zeroOnInterrupt)
    {
        this(manipulator, speed, speed, zeroOnInterrupt);
    }

    public SetManipulatorRollerSpeed(double speed, boolean zeroOnInterrupt)
    {
        this(Robot.getInstance().getManipulator(), speed, zeroOnInterrupt);
    }

    public SetManipulatorRollerSpeed(double speed)
    {
        this(speed, true);
    }

    @Override
    public void initialize()
    {
        manipulator.setGrippers(leftSpeed);
    }

    @Override
    public boolean isFinished()
    {
        return !zeroOnFinish;
    }

    @Override
    public void interrupted()
    {
        manipulator.setGrippers(0);
    }
}
