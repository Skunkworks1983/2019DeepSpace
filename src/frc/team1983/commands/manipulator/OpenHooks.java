package frc.team1983.commands.manipulator;

import frc.team1983.commands.CommandBase;
import frc.team1983.subsystems.Manipulator;

public class OpenHooks extends CommandBase
{
    private Manipulator manipulator;
    private boolean shouldOpen;

    /**
     * This constructor allows you to set if the manipulator is opened or not
     *
     * @param manipulator The manipulator
     * @param shouldOpen  If the manipulator should open or close
     */
    public OpenHooks(Manipulator manipulator, boolean shouldOpen)
    {
        this.manipulator = manipulator;
        this.shouldOpen = shouldOpen;
    }

    /**
     * This constructor toggles the state of the manipulator
     *
     * @param manipulator The manipulator
     */
    public OpenHooks(Manipulator manipulator)
    {
        this(manipulator, !manipulator.isOpen());
    }

    @Override
    public void initialize()
    {
        manipulator.setHooks(shouldOpen);
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
