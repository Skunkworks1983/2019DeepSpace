package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Robot;
import frc.team1983.constants.RobotMap;
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
        public static final double COLLECT = 140.0;
    }

    private Talon roller;
    private DoubleSolenoid piston;
    public MotorGroup wristLeft, wristRight;

    public static final double DEGREES_PER_TICK = 90.0 / 93.0; // TODO find more exact value
    public static final double CLOSED_LOOP_TOLERANCE = 3.0;

    public static final double DANGER_ZONE = 110.0; //TODO find exact value
    public static final double FOLD_ANGLE = 55.0; //TODO find exact value

    public static final double ELEVATOR_BOUNDARY = 35.0; //TODO change this later
    public static final double STOW_ZONE = 6.0; //TODO change value

    public double desiredAngle = 0.0;

    public Collector()
    {
        roller = new Talon(RobotMap.Collector.ROLLER, RobotMap.Collector.ROLLER_REVERSED);

        piston = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Collector.PISTON_FORWARD, RobotMap.Collector.PISTON_REVERSE);

        wristRight = new MotorGroup("Collector Wrist Right",
                new Spark(RobotMap.Collector.RIGHT, RobotMap.Collector.RIGHT_REVERSED));

        wristRight.setConversionRatio(DEGREES_PER_TICK);
        wristRight.setKP(0.06);

        wristLeft = new MotorGroup("Collector Wrist Left",
                new Spark(RobotMap.Collector.LEFT, RobotMap.Collector.LEFT_REVERSED));

        wristLeft.setKP(0.21);
        wristLeft.follow(wristRight);
    }

    @Override
    public void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {
        // fold/unfold logic
        if(getAngle() > FOLD_ANGLE && isFolded())
            setFolded(false);
        else if(getAngle() < FOLD_ANGLE && !isFolded())
            setFolded(true);

        // if the elevator is not between where we are and where we want to go,
        // proceed to the desired setpoint
        if(!elevatorIsInCollectorPath())
            wristRight.set(ControlMode.Position, desiredAngle);

        // if we are in the way of the elevator, move to the danger zone to let the elevator go by
        if(Robot.getInstance().getElevator().collectorIsInElevatorPath() && desiredAngle > DANGER_ZONE)
            wristRight.set(ControlMode.Position, DANGER_ZONE);
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
        desiredAngle = angle;
    }

    /**
     * @param folded If the piston should be extended or not
     */

    public void setFolded(boolean folded)
    {
        piston.set(folded ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
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
        roller.set(throttle);
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
