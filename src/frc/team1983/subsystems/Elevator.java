package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Robot;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Spark;

/**
 * The elevator uses inches. Anywhere where 'height' is mentioned used the height of the carriage,
 * so the distance traveled by the first stage times three.
 */
public class Elevator extends Subsystem
{
    public static class Setpoints
    {
        public static final double BOTTOM = 0.0;
        public static final double TRAVEL = Panel.GROUND_COLLECT;

        public static class Panel
        {
            public static final double GROUND_COLLECT = 7.0;
            public static final double ROCKET_BOTTOM = GROUND_COLLECT;
            public static final double ROCKET_MIDDLE = 34.5;
            public static final double ROCKET_TOP = 63.5;
            public static final double CARGOSHIP = ROCKET_BOTTOM;
            public static final double LOADING_STATION = GROUND_COLLECT;
            public static final double LOADING_STATION_POP_UP = LOADING_STATION + 7.0;
        }

        public static class Ball
        {
            public static final double GROUND_COLLECT = BOTTOM;
            public static final double ROCKET_BOTTOM = 17.0;
            public static final double ROCKET_MIDDLE = 44.0;
            public static final double ROCKET_TOP = 72.5;
            public static final double CARGOSHIP = 30.0;
            public static final double LOADING_STATION = 33.0;
        }
    }

    //danger zone setpoint
    public static final double INCHES_PER_TICK = (19.5 * 3.0) / 59.5; // Tested on practice bot
    public static final double CLOSED_LOOP_TOLERANCE = 2.0;

    public static final double DANGER_ZONE = 26.0; //TODO add actual values
    public static final double kG = 0.07; // Tested on practice bot with full battery

    public double desiredPosition = Setpoints.BOTTOM;
    public boolean automationEnabled = true;

    public MotorGroup motorGroup;

    public Elevator()
    {
        motorGroup = new MotorGroup("Left Elevator",
                new Spark(RobotMap.Elevator.LEFT, RobotMap.Elevator.LEFT_REVERSED),
                new Spark(RobotMap.Elevator.RIGHT, RobotMap.Elevator.RIGHT_REVERSED)
        );

        motorGroup.setConversionRatio(INCHES_PER_TICK);

        motorGroup.setMovementAcceleration(90);
        motorGroup.setCruiseVelocity(90);
        motorGroup.setKP(0.05);

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
        //if(!automationEnabled) return;

        // if we are in the way of the collector, move out of the way
        if (Robot.getInstance().getCollector().elevatorIsInCollectorPath())
            motorGroup.set(ControlMode.MotionMagic, DANGER_ZONE);
        else// otherwise, proceed to our desired position
            motorGroup.set(ControlMode.MotionMagic, desiredPosition);
    }

    public void zero()
    {
        motorGroup.zero();
    }

    public void set(ControlMode mode, double value)
    {
        if(mode == ControlMode.Throttle) automationEnabled = false;
        motorGroup.set(mode, value);
    }

    public void setPosition(double position)
    {
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

    public boolean isAtSetpoint()
    {
        return Math.abs(motorGroup.getPosition() - motorGroup.getSetpoint()) < CLOSED_LOOP_TOLERANCE;
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
