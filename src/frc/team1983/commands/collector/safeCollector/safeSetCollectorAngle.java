package frc.team1983.commands.collector.safeCollector;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team1983.Robot;
import frc.team1983.commands.collector.CollectionManager;
import frc.team1983.subsystems.Collector;

public class safeSetCollectorAngle extends InstantCommand
{
    public CollectionManager.State currentState;
    public CollectionManager collectionManager;

    public safeSetCollectorAngle(Collector collector, double angle)
    {
        super(collector, () -> collector.setAngle(angle));
    }

    public safeSetCollectorAngle(double angle)
    {

        this(Robot.getInstance().getCollector(), angle);
    }


}
