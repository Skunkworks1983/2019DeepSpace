package frc.team1983.commands.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.subsystems.Manipulator;

public class ExtendManipulator extends Command
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

    public void initialize()
    {
        manipulator.setExtender(shouldExtend);
    }

    public boolean isFinished()
    {
        return true;
    }
}
