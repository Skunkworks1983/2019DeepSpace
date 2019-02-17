package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Spark;

/**
 * The collector arm mechanism on the front of the robot. This subsystem consists of the motors that control its angle,
 * the double solenoid that controls the extension of the arm, and the motor that drives the roller wheels.
 */
public class Collector extends Subsystem
{
    private Spark roller;
    private DoubleSolenoid piston;
    private MotorGroup wrist;

    private static final double ticksPerDegree = 1; //TODO
    // The gravity gain (used when calculating feedforward, is multiplied by the cosine of the angle of the collector)
    private static final double kG = 0; //TODO
    // The length gain (is multiplied by the gravity gain if the piston is extended)
    private static final double kL = 1.01; //TODO

    public Collector()
    {
        roller = new Spark(RobotMap.Collector.ROLLER, RobotMap.Collector.ROLLER_REVERSED);

        piston = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Collector.PISTON_FORWARD, RobotMap.Collector.PISTON_REVERSE);

        wrist = new MotorGroup("Collector wrist", FeedbackType.POSITION,
                new Spark(RobotMap.Collector.LEFT, RobotMap.Collector.LEFT_REVERSED),
                new Spark(RobotMap.Collector.RIGHT, RobotMap.Collector.RIGHT_REVERSED));

        wrist.setPID(0.03, 0, 0);
        wrist.setMovementVelocity(3);
        wrist.setMovementAcceleration(3);
        wrist.setFFOperator(this);
        wrist.addFFTerm((collector) ->
            (((Collector) collector).isPistonExtended() ? kL : 1) *
            kG * Math.cos(((Collector) collector).getAngle()));
    }

    @Override
    public void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {

    }

    /**
     * Sets the throttle of the wrist motors. Using this function is not recommended, because it is not motion
     * controlled, but does allow for fine control of the arm.
     * @param output The percent output of the motors
     */
    public void setWristThrottle(double output)
    {
        wrist.set(ControlMode.Throttle, output);
    }

    /**
     * @param brake Whether the wrist motors should be in break mode or not
     */
    public void setWristBrake(boolean brake)
    {
        wrist.setBrake(brake);
    }

    /**Sets the angle of the arm using motion profiling
     * @param angle The desired angle of the arm
     */
    public void setAngle(double angle)
    {
        wrist.set(ControlMode.Position, angle * ticksPerDegree);
    }

    /**
     * @return The target angle of the motion profile (will be nonsense if not in position mode)
     */
    public double getTargetAngle()
    {
        return wrist.getTargetValue() / ticksPerDegree;
    }

    /**
     * @param shouldExtend If the piston should be extended or not
     */
    public void setPiston(boolean shouldExtend)
    {
        piston.set(shouldExtend ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    /**
     * @return True if the piston is extended, false if not (or if the solenoid is off)
     */
    public boolean isPistonExtended()
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
        return wrist.getPositionTicks() / ticksPerDegree;
    }
}
