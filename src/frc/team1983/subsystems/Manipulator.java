package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Robot;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Talon;

/**
 * The manipulator is mounted to the elevator, and is how we score game pieces. It has four actuators on it:
 * The extender pushes the entire mechanism out and in. The opener grabs hatch panels by opening up. The grippers grip cargo.
 */
public class Manipulator extends Subsystem
{
    private DoubleSolenoid extender;
    private DoubleSolenoid opener;

    private MotorGroup grippers;

    private DigitalInput ballSensor;
    private DigitalInput hatchSensor;

    private boolean lastBallSensorValue = false;


    public Manipulator()
    {
        extender = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Manipulator.EXTENDER_FORWARD, RobotMap.Manipulator.EXTENDER_REVERSE);
        opener = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Manipulator.HOOKS_FORWARD, RobotMap.Manipulator.HOOKS_REVERSE);

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
        boolean value = ballSensor.get();
        System.out.println(value);

        if(value && !lastBallSensorValue)
            Robot.getInstance().getElevator().setPosition(Math.min(Robot.getInstance().getElevator().getPosition() + 5.0, 12.0));
//        System.out.println("ballSensor: " + ballSensor.get());
//        System.out.println("hatchSensor: " + hatchSensor.get());
//        System.out.println("gripperEncoder: " + grippers.getPositionTicks());

        lastBallSensorValue = value;
    }

    /**
     * @param shouldExtend If the manipulator should be extended or not
     */
    public void setExtended(boolean shouldExtend)
    {
        extender.set(shouldExtend ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }

    /**
     *
     * @return state of the extender //todo fix naming convention
     */
    public boolean getExtended()
    {
        return extender.get() == DoubleSolenoid.Value.kReverse;
    }

    /**
     * @param open If the opener should be opened or closed
     */
    public void setOpen(boolean open)
    {
        opener.set(open ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }
    /**
     *
     * @return state of the opener //todo fix naming convention
     */
    public boolean getOpen()
    {
        return opener.get() == DoubleSolenoid.Value.kReverse;
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
