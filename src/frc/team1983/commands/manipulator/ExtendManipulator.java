package frc.team1983.commands.manipulator;

import frc.team1983.commands.CommandBase;
import frc.team1983.subsystems.Manipulator;

public class ExtendManipulator extends CommandBase
{
    private Manipulator manipulator;
    private boolean shouldExtend;

    /**
     * This constructor allows you to set if the manipulator is extended or not
     *
     * @param manipulator  The manipulator
     * @param shouldExtend If the manipulator should extend or retract
     */
    public ExtendManipulator(Manipulator manipulator, boolean shouldExtend)
    {
        this.manipulator = manipulator;
        this.shouldExtend = shouldExtend;
    }

    /**
     * This constructor toggles the state of the manipulator
     *
     * @param manipulator The manipulator
     */
    public ExtendManipulator(Manipulator manipulator)
    {
        this(manipulator, !manipulator.isExtended());
    }

    @Override
    public void initialize()
    {
        manipulator.setExtender(shouldExtend);
    }

    @Override
    public void execute()
    {

    }

    @Override
    public boolean isFinished()
    {
        return true;
    }

    @Override
    public void end()
    {

    }

    @Override
    public void interrupted()
    {

    }
}
