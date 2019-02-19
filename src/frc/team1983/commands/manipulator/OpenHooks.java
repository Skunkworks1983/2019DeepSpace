package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.subsystems.Manipulator;

public class OpenHooks extends Command
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

    @Override
    public void initialize()
    {
        manipulator.setHooks(shouldOpen);
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
