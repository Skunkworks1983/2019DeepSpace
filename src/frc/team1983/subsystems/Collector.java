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
    public MotorGroup wristLeft, wristRight;

    public static final double DEGREES_PER_TICK = 90.0 / 93.0; // TODO find more exact value

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
        wristRight.set(ControlMode.Position, angle);
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
        roller.set(throttle);
    }

    /**
     * @return The current angle of the arm
     */
    public double getAngle()
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
}
