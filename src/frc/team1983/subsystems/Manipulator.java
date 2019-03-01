package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Robot;
import frc.team1983.constants.RobotMap;
import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Talon;
import frc.team1983.utilities.sensors.DigitalInputEncoder;

/**
 * The manipulator is mounted to the elevator, and is how we score game pieces. It has four actuators on it:
 * The extender pushes the entire mechanism out and in. The hooks grabs hatch panels by opening up. The grippers grip cargo.
 */
public class Manipulator extends Subsystem
{
    private DoubleSolenoid extender;
    private DoubleSolenoid hooks;

    private MotorGroup grippers;

    private DigitalInput ballSensor;
    private DigitalInput hatchSensor;

    public Manipulator()
    {
        extender = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Manipulator.EXTENDER_FORWARD, RobotMap.Manipulator.EXTENDER_REVERSE);
        hooks = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Manipulator.HOOKS_FORWARD, RobotMap.Manipulator.HOOKS_REVERSE);

        grippers = new MotorGroup("Manipulator Grippers", RobotMap.Manipulator.GRIPPER_ENCODER,
                new Talon(RobotMap.Manipulator.LEFT_GRIPPER, RobotMap.Manipulator.LEFT_GRIPPER_REVERSED),
                new Talon(RobotMap.Manipulator.RIGHT_GRIPPER, RobotMap.Manipulator.RIGHT_GRIPPER_REVERSED));

        ballSensor = new DigitalInput(RobotMap.Manipulator.BALL_SENSOR);
        hatchSensor = new DigitalInput(RobotMap.Manipulator.HATCH_SENSOR);
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {
//        System.out.println("ballSensor: " + ballSensor.get());
//        System.out.println("hatchSensor: " + hatchSensor.get());
//        System.out.println("gripperEncoder: " + grippers.getPositionTicks());
    }

    /**
     * @param shouldExtend If the manipulator should be extended or not
     */
    public void setExtender(boolean shouldExtend)
    {
        extender.set(shouldExtend ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public boolean isExtenderExtended()
    {
        return extender.get() == DoubleSolenoid.Value.kForward;
    }

    /**
     *
     * @return state of the extender //todo fix naming convention
     */
    public boolean getExtender()
    {
        return extender.get() == DoubleSolenoid.Value.kForward;
    }

    /**
     * @param shouldOpen If the hooks should be opened or closed
     */
    public void setHooks(boolean shouldOpen)
    {
        hooks.set(shouldOpen ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public boolean isHooksOpen()
    {
        return hooks.get() == DoubleSolenoid.Value.kForward;
    }

    /**
     *
     * @return state of the hooks //todo fix naming convention
     */
    public boolean getHooks()
    {
        return hooks.get() == DoubleSolenoid.Value.kForward;
    }

    /**
     * @param output The percentoutput that should be applied to the motors
     */
    public void setGrippers(double output)
    {
        grippers.set(ControlMode.Throttle, output);
    }

    public boolean getBallSensorValue()
    {
        return ballSensor.get();
    }

    public boolean getHatchSensorValue()
    {
        return hatchSensor.get();
    }
}
