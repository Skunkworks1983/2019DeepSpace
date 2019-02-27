package frc.team1983.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.commands.collector.CollectionManager;
import frc.team1983.subsystems.Collector;

public class WaitForState extends Command
{
    public CollectionManager collectionManager;
    public CollectionManager.State targetState;

    public WaitForState(CollectionManager.State targetState)
    {
        this.targetState = targetState;
        collectionManager = Robot.getInstance().getCollectionManager();
    }

    @Override
    public void initialize()
    {

    }

    @Override
    public boolean isFinished()
    {
      if(this.targetState == collectionManager.getCurrentState())
      {
          return true;
      }
      else
      {
          return false;
      }
    }
    @Override
    public void end(){}
    @Override
    public void interrupted(){end();}

}
