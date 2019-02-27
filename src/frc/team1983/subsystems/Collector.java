package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Robot;
import frc.team1983.commands.SafeAutomationManager;
import frc.team1983.commands.collector.CollectionManager;
import frc.team1983.commands.collector.SetCollectorFolded;
import frc.team1983.constants.CollectorConstants;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.*;

/**
 * The collector arm mechanism on the front of the robot. This subsystem consists of the motors that control its angle,
 * the double solenoid that controls the extension of the arm, and the motor that drives the roller wheels.
 */
public class Collector extends Subsystem
{
    private Talon roller;
    private DoubleSolenoid piston;
    public MotorGroup wristLeft, wristRight;
    public State currentState;
    public CollectionManager collectionManager;
    public Elevator elevator;
    public SafeAutomationManager safeAutomationManager;

    public static final double DEGREES_PER_TICK = 90.0 / 93.0; // TODO find more exact value

    public static final double CLOSED_LOOP_TOLERANCE = 4.0; // degrees

    public Collector()
    {
        roller = new Talon(RobotMap.Collector.ROLLER, RobotMap.Collector.ROLLER_REVERSED);
        Robot robot = Robot.getInstance();
        if(robot == null)
        {
            System.out.println("ROBOT IS NULL IN COLLECTOR :(");
        }
        collectionManager = robot.getCollectionManager();
        elevator = robot.getElevator();

        piston = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Collector.PISTON_FORWARD, RobotMap.Collector.PISTON_REVERSE);

        wristRight = new MotorGroup("Collector Wrist Right", FeedbackType.POSITION,
                new Spark(RobotMap.Collector.RIGHT, RobotMap.Collector.RIGHT_REVERSED));

        wristRight.setConversionRatio(DEGREES_PER_TICK);
        wristRight.setPID(0.06, 0, 0);
        wristRight.setUseMotionProfiles(false);

        wristLeft = new MotorGroup("Collector Wrist Left", FeedbackType.POSITION,
                                   new Spark(RobotMap.Collector.LEFT, RobotMap.Collector.LEFT_REVERSED));

        wristLeft.setPID(0.21, 0, 0);
        wristLeft.follow(wristRight);


        currentState = State.STOPPED;
        safeAutomationManager = new SafeAutomationManager();
    }
    public enum State {
        STOPPED,
        UNFOLDING,
        FOLDING

    }

    @Override
    public void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {
        switch (currentState)
        {
            case STOPPED:
                if(getAngularVelocity() > 10)
                {
                    currentState = State.UNFOLDING;
                    System.out.println("SWITCHING TO COLLECTOR STATE: " + currentState);
                    break;
                }
                if(getAngularVelocity() < -10)
                {
                    currentState = State.FOLDING;
                    System.out.println("SWITCHING TO COLLECTOR STATE: " + currentState);
                    break;
                }
                break;
            case UNFOLDING:
                new SetCollectorFolded(false);
                if(getAngularVelocity() <= 10 && getAngularVelocity() >= -10)
                {
                    currentState = State.STOPPED;
                    System.out.println("SWITCHING TO COLLECTOR STATE: " + currentState);
                    break;
                }
                break;
            case FOLDING:
                if(this.getAngle() <= CollectorConstants.WristSetpoints.DZ)
                {
                    new SetCollectorFolded(true);
                }
                if(getAngularVelocity() <= 10 && getAngularVelocity() >= -10)
                {
                    currentState = State.STOPPED;
                    System.out.println("SWITCHING TO COLLECTOR STATE: " + currentState);
                    break;
                }
                break;
        }
    }

    /**
     * @param output Sets the percent output of the wrist motors
     */
    public void setWristThrottle(double output)
    {
        wristRight.set(ControlMode.Throttle, output);
    }

    /**
     * @param brake Whether the wrist motors should be in break mode or not
     */
    public void setWristBrake(boolean brake)
    {
        wristLeft.setBrake(brake);
        wristRight.setBrake(brake);
    }

    /**
     * Sets the angle of the arm using motion profiling
     *
     * @param angle The desired angle of the arm
     */
    public void setAngle(double angle)
    {
        System.out.println("ANGLE : " + angle);
        System.out.println("CURRENT STATE: " + collectionManager.getCurrentState());
        if(collectionManager.getCurrentState() == CollectionManager.State.E_DANGER__COL_SAFE && angle <  CollectorConstants.WristSetpoints.DZ)
        {
            safeAutomationManager.moveCollectorDZWhileEleInIllegalState(angle);
        }
        else if(collectionManager.getCurrentState() == CollectionManager.State.E_LOWERING__COL_SAFE && angle <  CollectorConstants.WristSetpoints.DZ)
        {
            safeAutomationManager.moveCollectorDZWhileEleInIllegalState(angle);
        }
        else if(collectionManager.getCurrentState() == CollectionManager.State.E_RISING__COL_SAFE && angle <  CollectorConstants.WristSetpoints.DZ)
        {
            safeAutomationManager.moveCollectorDZWhileEleInIllegalState(angle);
        }
        else if(collectionManager.getCurrentState() == CollectionManager.State.START_STATE)
        {
            safeAutomationManager.moveCollectorDZWhileEleInIllegalState(angle);
        }
        else
        {
            wristRight.set(ControlMode.Position, angle);
        }
    }

    /**
     * @param folded If the piston should be extended or not
     */

    public void setFolded(boolean folded)
    {
        piston.set(folded ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }
    /**
     *
     * @return state of the collector
     */
    public boolean isFolded()
    {
        return piston.get() == DoubleSolenoid.Value.kReverse;
    }

    /**
     * @param throttle Sets the throttle of the roller motor
     */
    public void setRollerThrottle(double throttle)
    {
        roller.set(ControlMode.Throttle, throttle);
    }

    /**
     * @return The current angle of the arm
     */
    public double getAngle()
    {

        return wristRight.getPosition();
    }

    public double getAngularVelocity()
    {
        return wristRight.getVelocity();
    }

    /**
     * @return The current ticks of the arm
     */
    public double getTicks()
    {
        return wristRight.getPosition();
    }

    /**
     * Zeros the wrist
     */
    public void zero()
    {
        wristLeft.zero();
        wristRight.zero();
    }

    public boolean isAtSetpoint()
    {
        return Math.abs(wristRight.getPosition() - wristRight.getTarget()) < CLOSED_LOOP_TOLERANCE;
    }
}
