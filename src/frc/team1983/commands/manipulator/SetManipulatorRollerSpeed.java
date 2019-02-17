package frc.team1983.commands.manipulator;

import frc.team1983.commands.CommandBase;
import frc.team1983.subsystems.Manipulator;

/**
 * Sets the speed of the rollers on the manipulator
 */
public class SetManipulatorRollerSpeed extends CommandBase
{
    private Manipulator manipulator;
    private double leftSpeed;
    private double rightSpeed;
    private boolean zeroOnFinish;

    /**
     * This constructor allows setting both roller speed independently
     *
     * @param manipulator     The manipulator
     * @param leftSpeed       The percent output of the left roller
     * @param rightSpeed      The percent output of the right roller
     * @param zeroOnInterrupt If the command should run forever and zero the rollers when it ends
     */
    public SetManipulatorRollerSpeed(Manipulator manipulator, double leftSpeed, double rightSpeed, boolean zeroOnInterrupt)
    {
        this.manipulator = manipulator;
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
        this.zeroOnFinish = zeroOnInterrupt;
    }

    /**
     * This constructor allows setting both roller speeds to the same value
     *
     * @param manipulator     The manipulator
     * @param speed           The percent output of both rollers
     * @param zeroOnInterrupt If the command should run forever and zero the rollers when it is interrupted
     */
    public SetManipulatorRollerSpeed(Manipulator manipulator, double speed, boolean zeroOnInterrupt)
    {
        this(manipulator, speed, speed, zeroOnInterrupt);
    }

    @Override
    public void initialize()
    {
        manipulator.setLeftRoller(leftSpeed);
        manipulator.setRightRoller(rightSpeed);
    }

    @Override
    public void execute()
    {

    }

    @Override
    public boolean isFinished()
    {
        return !zeroOnFinish;
    }

    @Override
    public void end()
    {

    }

    @Override
    public void interrupted()
    {
        manipulator.setRollers(0);
    }
}
