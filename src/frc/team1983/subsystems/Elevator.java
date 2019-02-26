package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Robot;
import frc.team1983.commands.collector.CollectionManager;
import frc.team1983.constants.ElevatorConstants;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Spark;

/**
 * The elevator uses inches. Anywhere where 'height' is mentioned used the height of the carriage,
 * so the distance traveled by the first stage times three.
 */
public class Elevator extends Subsystem
{
    public static final double kG = 0.05; //Tested on practice bot with full battery
    public static final double INCHES_PER_TICK = (22.0 * 3.0) / 95.0; // TODO: add math

    public MotorGroup motorGroup;

    public CollectionManager collectionManager;


    public Elevator()
    {
        Robot robot = Robot.getInstance();
        if(robot == null)
        {
            System.out.println("ROBOT IS NULL IN ELEVATOR :(");
        }
        collectionManager = robot.getCollectionManager();

        motorGroup = new MotorGroup("Left Elevator", FeedbackType.POSITION,
                new Spark(RobotMap.Elevator.LEFT, RobotMap.Elevator.LEFT_REVERSED),
                new Spark(RobotMap.Elevator.RIGHT, RobotMap.Elevator.RIGHT_REVERSED)
        );

        motorGroup.setConversionRatio(INCHES_PER_TICK);

        motorGroup.setMovementAcceleration(12);
        motorGroup.setCruiseVelocity(12);
        motorGroup.setPID(0.18, 0, 0); // TODO: add values

        motorGroup.setFFOperator(this);
        motorGroup.addFFTerm(Elevator -> kG);


        zero();
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {

    }

    public void zero()
    {
        motorGroup.zero();
    }

    public void set(ControlMode mode, double value)
    {
        if(collectionManager.getCurrentState() == CollectionManager.State.E_SAFE__COL_FOLDING && value <= ElevatorConstants.SetPoints.ELE_DZ)
        {
            System.out.println("beep beep THAT'S ILLEGAL u can't do that");
        }
        else if(collectionManager.getCurrentState() == CollectionManager.State.E_SAFE__COL_UNFOLDING && value <= ElevatorConstants.SetPoints.ELE_DZ)
        {
            System.out.println("beep beep THAT'S ILLEGAL u can't do that");
        }
        else
        {
            motorGroup.set(mode, value);
        }
    }

    public double getPosition()
    {
        return motorGroup.getPositionTicks() * INCHES_PER_TICK;
    }

    public double getVelocity()
    {
        return motorGroup.getVelocity();
    }

    public void setBrake(boolean brake)
    {
        motorGroup.setBrake(brake);
    }
}
