package frc.team1983.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
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
    private Collector collector = robot.getCollector();
    private Elevator elevator = robot.getElevator();
    private Manipulator manipulator = robot.getManipulator();
    private OI oi;
    public State currentState;

    public CollectionManager()
    {
        collectionMode = false;
        oi = new OI();
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
    public void initialize(){}

    @Override
    public void execute()
    {

        switch (currentState)
        {
            case START_STATE:
                if(oi.isPressed(OI.CARGO_SHIP)||oi.isPressed(MID)||oi.isPressed(OI.HIGH))
                {
                    currentState = State.E_RISING__COL_DZ;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_RISING__COL_SAFE:
                if(elevator.getPosition()>= ElevatorConstants.SetPoints.ELE_SAFE)
                {
                    currentState = State.E_SAFE__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_RISING__COL_DZ:
                if(elevator.getPosition()>=ElevatorConstants.SetPoints.ELE_SAFE)
                {
                    currentState = State.E_SAFE__COL_DZ;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_DANGER__COL_SAFE:
                if(oi.isPressed(OI.CARGO_SHIP)||oi.isPressed(MID)||oi.isPressed(OI.HIGH))
                {
                    currentState = State.E_RISING__COL_SAFE;
                    System.out.println("SWITCH TO : "+currentState);
                    break;
                }
                break;
            case E_LOWERING__COL_SAFE:
                if(elevator.getPosition() <= ElevatorConstants.SetPoints.ELE_SAFE)
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
                if(oi.isPressed(OI.LOW)||oi.isPressed(OI.FLOOR_COLLECT)||oi.isPressed(OI.STATION_COLLECT))
                {
                    currentState = State.E_LOWERING__COL_SAFE;
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
                break;
            default:
                //java code
                break;
        }
    }

    @Override
    public boolean isFinished(){return false;}

    @Override
    public void end(){}

    @Override
    public void interrupted(){end();}

}
