package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.constants.CollectorConstants;
import frc.team1983.constants.ElevatorConstants;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Collector;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;

import static frc.team1983.services.OI.MID;

public class CollectionManager extends Command
{
    private boolean collectionMode;
    private Robot robot = Robot.getInstance();
    private Collector collector;
    private Elevator elevator;
    private Manipulator manipulator;
    private OI oi;
    public State currentState;

    public CollectionManager()
    {
        collectionMode = false;
        currentState = State.START_STATE;
    }
    public enum State {
        START_STATE,
        E_RISING__COL_SAFE,
        E_DANGER__COL_SAFE,
        E_LOWERING__COL_SAFE,
        E_SAFE__COL_SAFE,
        E_SAFE__COL_FOLDING,
        E_SAFE__COL_UNFOLDING,
        E_SAFE__COL_DZ,
        E_RISING__COL_DZ
    }
    @Override
    public void initialize()
    {
        robot = Robot.getInstance();
        collector = robot.getCollector();
        elevator = robot.getElevator();
        manipulator = robot.getManipulator();
        oi = new OI();


    }

    @Override
    public void execute()
    {
        switch (currentState)
        {
            case START_STATE:
                if(elevator.getVelocity() < 0)
                {
                    currentState = State.E_RISING__COL_DZ;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_RISING__COL_SAFE:
                if(elevator.getPosition()>= ElevatorConstants.SetPoints.ELE_SAFE  && collector.currentState == Collector.State.STOPPED)
                {
                    currentState = State.E_SAFE__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_RISING__COL_DZ:
                if(elevator.getPosition()>=ElevatorConstants.SetPoints.ELE_SAFE && collector.currentState == Collector.State.STOPPED)
                {
                    currentState = State.E_SAFE__COL_DZ;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_DANGER__COL_SAFE:
                if(elevator.getVelocity() < 0)
                {
                    currentState = State.E_RISING__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                if(elevator.getVelocity() > 0)
                {
                    currentState = State.E_LOWERING__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_LOWERING__COL_SAFE:
                if(elevator.getPosition() <= ElevatorConstants.SetPoints.ELE_SAFE && collector.currentState == Collector.State.STOPPED)
                {
                    currentState = State.E_DANGER__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_SAFE__COL_SAFE:
                if(collector.currentState == Collector.State.FOLDING)
                {
                    currentState = State.E_SAFE__COL_FOLDING;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                if(elevator.getVelocity() > 0)
                {
                    currentState = State.E_LOWERING__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                if(elevator.getVelocity() < 0)
                {
                    currentState = State.E_RISING__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_SAFE__COL_FOLDING:
                if(collector.getAngle() <= CollectorConstants.WristSetpoints.DZ)
                {
                    new SetCollectorFolded(true);
                }
                if(collector.getAngle() <= CollectorConstants.WristSetpoints.DZ && collector.currentState == Collector.State.STOPPED)
                {
                    currentState = State.E_SAFE__COL_DZ;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_SAFE__COL_UNFOLDING:
                new SetCollectorFolded(false);
                if(collector.getAngle() > CollectorConstants.WristSetpoints.DZ && collector.currentState == Collector.State.STOPPED)
                {
                    currentState = State.E_SAFE__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_SAFE__COL_DZ:
                if(collector.currentState == Collector.State.UNFOLDING)
                {
                    currentState = State.E_SAFE__COL_UNFOLDING;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                if(collector.currentState == Collector.State.STOPPED && elevator.getPosition() < ElevatorConstants.SetPoints.ELE_SAFE)
                {
                    currentState = State.START_STATE;
                    System.out.println("SWITCH TO : " + currentState);
                    break;
                }
                break;
            default:
                //java code
                break;
        }
    }

    public State getCurrentState()
    {
        System.out.println("GETTING CURRENT STATE, CURRENT STATE IS : " + currentState);
        return currentState;
    }

    @Override
    public boolean isFinished(){return false;}

    @Override
    public void end(){}

    @Override
    public void interrupted(){end();}

}
