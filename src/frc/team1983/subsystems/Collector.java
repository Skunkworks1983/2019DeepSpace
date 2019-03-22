package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Robot;
import frc.team1983.constants.RobotMap;
import frc.team1983.services.OI;
import frc.team1983.utilities.motors.*;

/**
 * The collector arm mechanism on the front of the robot. This subsystem consists of the motors that control its angle,
 * the double solenoid that controls the extension of the arm, and the motor that drives the roller wheels.
 */
public class Collector extends Subsystem
{
    public static class Setpoints
    {
        public static final double STOW = 0.0;
        public static final double STOW_UPPER = 115.0;
        public static final double COLLECT = 140.0;
    }

    private MotorGroup roller;
    private DoubleSolenoid piston;
    public MotorGroup wristLeft, wristRight;

    public static final double DEGREES_PER_TICK = 90.0 / 93.0; // TODO find more exact value
    public static final double CLOSED_LOOP_TOLERANCE = 3.0;

    public static final double DANGER_ZONE = 100.0; //TODO find exact value
    public static final double FOLD_ANGLE = 35.0; //TODO find exact value

    public static final double ELEVATOR_BOUNDARY = 35.0; //TODO change this later
    public static final double STOW_ZONE = 6.0; //TODO change value

    public double desiredAngle = 0.0;
    public boolean automationEnabled = true;
    public boolean climbing = false;

    public boolean desiredFoldedState = false;

    public Collector()
    {
        roller = new MotorGroup("Collector Roller",
                new Talon(RobotMap.Collector.ROLLER1, RobotMap.Collector.ROLLER1_REVERSED)
        );

        piston = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Collector.PISTON_FORWARD, RobotMap.Collector.PISTON_REVERSE);

        wristRight = new MotorGroup("Collector Wrist Right",
                new Spark(RobotMap.Collector.RIGHT, RobotMap.Collector.RIGHT_REVERSED));

        wristRight.setConversionRatio(DEGREES_PER_TICK);
        wristRight.setKP(0.06);
        wristRight.setCurrentLimit(40);


        wristLeft = new MotorGroup("Collector Wrist Left",
                new Spark(RobotMap.Collector.LEFT, RobotMap.Collector.LEFT_REVERSED));

        wristLeft.setKP(0.21);
        wristLeft.follow(wristRight);
        wristLeft.setCurrentLimit(40);
    }

    @Override
    public void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {
        //if(!automationEnabled) return;

        // fold/unfold logic
        if (getAngle() > FOLD_ANGLE)
            piston.set(desiredFoldedState ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
        else if (getAngle() < FOLD_ANGLE)
            piston.set(DoubleSolenoid.Value.kForward);

        if(getAngle() < 15.0 || isFolded()) setRollerThrottle(0);

        // if the elevator is not between where we are and where we want to go,
        // proceed to the desired setpoint
        if (!elevatorIsInCollectorPath())
            wristRight.set(ControlMode.Position, desiredAngle);

        // if we are in the way of the elevator, move to the danger zone to let the elevator go by
        if (Robot.getInstance().getElevator().collectorIsInElevatorPath() && desiredAngle > DANGER_ZONE && !Robot.getInstance().getOI().isInManualMode())
            wristRight.set(ControlMode.Position, DANGER_ZONE);
    }

    /**
     * @param output Sets the percent output of the wrist motors
     */
    public void setWristThrottle(double output)
    {
        automationEnabled = false;
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
        automationEnabled = true;
        desiredAngle = angle;
    }

    /**
     * @param folded If the piston should be extended or not
     */

    public void setFolded(boolean folded)
    {
        desiredFoldedState = folded;
    }
    /**
     *
     * @return state of the collector
     */
    public boolean isFolded()
    {
        return piston.get() == DoubleSolenoid.Value.kForward;
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

    public boolean isAtSetpoint()
    {
        return Math.abs(wristRight.getPosition() - wristRight.getSetpoint()) < CLOSED_LOOP_TOLERANCE;
    }

    public boolean isInDangerZone()
    {
        return getAngle() <= DANGER_ZONE - CLOSED_LOOP_TOLERANCE;
    }

    public boolean elevatorIsInCollectorPath()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        return elevator.isInDangerZone() && ((desiredAngle < ELEVATOR_BOUNDARY && getAngle() >= ELEVATOR_BOUNDARY)
                || (desiredAngle > ELEVATOR_BOUNDARY && getAngle() <= ELEVATOR_BOUNDARY));
    }

    public void zero()
    {
        wristLeft.zero();
        wristRight.zero();
    }
}
