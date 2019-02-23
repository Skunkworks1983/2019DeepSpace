package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
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
    private MotorGroup wrist;

    private static final double TICKS_PER_DEGREE = 1; // TODO find more exact value
    // The gravity gain (used when calculating feedforward, is multiplied by the cosine of the angle of the collector)
    private static final double K_G = 0; // TODO determine if needed
    // The length gain (is multiplied by the gravity gain if the piston is extended)
    private static final double K_L = 1.01; // TODO determine if needed

    public Collector()
    {
        roller = new Talon(RobotMap.Collector.ROLLER, RobotMap.Collector.ROLLER_REVERSED);

        piston = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Collector.PISTON_FORWARD, RobotMap.Collector.PISTON_REVERSE);

        wrist = new MotorGroup("Collector Wrist", FeedbackType.POSITION,
                new Spark(RobotMap.Collector.LEFT, RobotMap.Collector.LEFT_REVERSED),
                new Spark(RobotMap.Collector.RIGHT, RobotMap.Collector.RIGHT_REVERSED));

        wrist.setPID(0.06, 0, 0);
        wrist.setMovementVelocity(6);
        wrist.setMovementAcceleration(6);
        wrist.setFFOperator(this);
        //        wrist.addFFTerm((collector) ->
        //                (((Collector) collector).piston.get() == DoubleSolenoid.Value.kForward ? K_L : 1) *
        //                        K_G * Math.cos(((Collector) collector).getAngle()));
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
     * @param output Sets the percent output of the wrist motors
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

    /**
     * Sets the angle of the arm using motion profiling
     *
     * @param angle The desired angle of the arm
     */
    public void setAngle(double angle)
    {
        wrist.set(ControlMode.Position, angle * TICKS_PER_DEGREE);
    }

    /**
     * @return The target angle of the motion profile (will be nonsense if not in position mode)
     */
    public double getTargetAngle()
    {
        return wrist.getTargetValue() / TICKS_PER_DEGREE;
    }

    /**
     * @param folded If the piston should be extended or not
     */
    public void setFolded(boolean folded)
    {
        piston.set(folded ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    /**
     * @return True if the piston is extended, false if not (or if the solenoid is off)
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
        return wrist.getPositionTicks() / TICKS_PER_DEGREE;
    }

    /**
     * @return The current ticks of the arm
     */
    public double getTicks()
    {
        return wrist.getPositionTicks();
    }

    /**
     * Zeros the wrist
     */
    public void zero()
    {
        wrist.zero();
    }
}
