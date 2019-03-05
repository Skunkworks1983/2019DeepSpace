package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Robot;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Spark;

import java.util.Map;
import java.util.Set;

/**
 * The elevator uses inches. Anywhere where 'height' is mentioned used the height of the carriage,
 * so the distance traveled by the first stage times three.
 */
public class Elevator extends Subsystem
{
    public enum FunctionalSetpoint
    {
        GROUND_COLLECT, LOADING_STATION, CARGO_SHIP,
        ROCKET_BOTTOM, ROCKET_MIDDLE, ROCKET_TOP, CUSTOM
    }

    public static class Setpoints
    {
        public static final double BOTTOM = 0.0;

        public static final Map<FunctionalSetpoint, Double> Panel = Map.ofEntries(
                Map.entry(FunctionalSetpoint.GROUND_COLLECT, 7.0),
                Map.entry(FunctionalSetpoint.LOADING_STATION, 7.0),
                Map.entry(FunctionalSetpoint.CARGO_SHIP, 7.0),
                Map.entry(FunctionalSetpoint.ROCKET_BOTTOM, 7.0),
                Map.entry(FunctionalSetpoint.ROCKET_MIDDLE, 34.5),
                Map.entry(FunctionalSetpoint.ROCKET_TOP, 63.5)
        );

        public static final Map<FunctionalSetpoint, Double> Ball = Map.ofEntries(
                Map.entry(FunctionalSetpoint.GROUND_COLLECT, 0.0),
                Map.entry(FunctionalSetpoint.LOADING_STATION, 34.5),
                Map.entry(FunctionalSetpoint.CARGO_SHIP, 30.0),
                Map.entry(FunctionalSetpoint.ROCKET_BOTTOM, 17.0),
                Map.entry(FunctionalSetpoint.ROCKET_MIDDLE, 44.0),
                Map.entry(FunctionalSetpoint.ROCKET_TOP, 72.5)
        );
    }

    public static final double INCHES_PER_TICK = (19.5 * 3.0) / 59.5;
    public static final double CLOSED_LOOP_TOLERANCE = 2.0; // inches

    public static final double DANGER_ZONE = 26.0; // inches
    public static final double kG = 0.07; // %

    private double desiredPosition = Setpoints.BOTTOM;
    private FunctionalSetpoint functionalSetpoint = null;

    private boolean automationEnabled = true;

    private MotorGroup motorGroup;

    public Elevator()
    {
        motorGroup = new MotorGroup("Left Elevator",
                new Spark(RobotMap.Elevator.LEFT, RobotMap.Elevator.LEFT_REVERSED),
                new Spark(RobotMap.Elevator.RIGHT, RobotMap.Elevator.RIGHT_REVERSED)
        );

        motorGroup.setConversionRatio(INCHES_PER_TICK);

        motorGroup.setMovementAcceleration(90);
        motorGroup.setCruiseVelocity(90);
        motorGroup.setPIDF(0.05, 0, 0, 0);

        zero();
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {
        if(!automationEnabled) return;

        if(functionalSetpoint != FunctionalSetpoint.CUSTOM)
            desiredPosition = (Robot.getInstance().isInPanelMode() ? Setpoints.Panel : Setpoints.Ball).get(functionalSetpoint);

        // if we are in the way of the collector, move out of the way
        if (Robot.getInstance().getCollector().elevatorIsInCollectorPath())
            motorGroup.set(ControlMode.PositionProfiled, DANGER_ZONE);
        else// otherwise, proceed to our desired position
            motorGroup.set(ControlMode.PositionProfiled, desiredPosition);
    }

    public void zero()
    {
        motorGroup.zero();
    }

    public void set(ControlMode mode, double value)
    {
        automationEnabled = false;
        motorGroup.set(mode, value);
    }

    public void setPosition(double position)
    {
        Map<FunctionalSetpoint, Double> setpoints = Robot.getInstance().isInPanelMode() ? Setpoints.Panel : Setpoints.Ball;

        functionalSetpoint = FunctionalSetpoint.CUSTOM;
        for(Map.Entry<FunctionalSetpoint, Double> entry : setpoints.entrySet())
            if(entry.getValue() == position)
                functionalSetpoint = entry.getKey();

        automationEnabled = true;
        desiredPosition = position;
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

    public boolean isInDangerZone()
    {
        return getPosition() <= DANGER_ZONE - CLOSED_LOOP_TOLERANCE;
    }

    public boolean collectorIsInElevatorPath()
    {
        Collector collector = Robot.getInstance().getCollector();
        return (collector.isInDangerZone() && collector.getAngle() > Collector.STOW_ZONE) && (desiredPosition < DANGER_ZONE
                || getPosition() < DANGER_ZONE);

    }
}
