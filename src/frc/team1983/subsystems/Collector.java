package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Spark;

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
                (((Collector) collector).piston.get() == DoubleSolenoid.Value.kForward ? kL : 1) *
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

    public void setWristThrottle(double output)
    {
        wrist.set(ControlMode.Throttle, output);
    }

    public void setWristBrake(boolean brake)
    {
        wrist.setBrake(brake);
    }

    public void setAngle(double angle)
    {
        wrist.set(ControlMode.Position, angle * ticksPerDegree);
    }

    public double getTargetAngle()
    {
        return wrist.getTargetValue() / ticksPerDegree;
    }

    public double getAngle()
    {
        return wrist.getPositionTicks() / ticksPerDegree;
    }
}
