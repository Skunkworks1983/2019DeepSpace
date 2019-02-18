package frc.team1983.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motors.Talon;

/**
 * The manipulator is mounted to the elevator, and is how we score game pieces. It has four actuators on it:
 * The extender pushes the entire mechanism out and in. The hooks grabs hatch panels by opening up. The grippers grip cargo.
 */
public class Manipulator extends Subsystem
{
    private Logger logger;

    private DoubleSolenoid extender;
    private DoubleSolenoid hooks;

    private Talon leftGripper;
    private Talon rightGripper;

    public Manipulator()
    {
        extender = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Manipulator.EXTENDER_FORWARD, RobotMap.Manipulator.EXTENDER_REVERSE);
        hooks = new DoubleSolenoid(RobotMap.COMPRESSOR, RobotMap.Manipulator.HOOKS_FORWARD, RobotMap.Manipulator.HOOKS_REVERSE);
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {
        
    }

    /**
     * @param shouldExtend If the manipulator should be extended or not
     */
    public void setExtender(boolean shouldExtend)
    {
        extender.set(shouldExtend ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    /**
     * @param shouldOpen If the hooks should be opened or closed
     */
    public void setHooks(boolean shouldOpen)
    {
        hooks.set(shouldOpen ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    /**
     * @param output The percentoutput that should be applied to the motor
     */
    public void setLeftGripper(double output)
    {
        leftGripper.set(ControlMode.PercentOutput, output);
    }

    /**
     * @param output The percentoutput that should be applied to the motor
     */
    public void setRightGripper(double output)
    {
        rightGripper.set(ControlMode.PercentOutput, output);
    }

    /**
     * @param output The percentoutput that should be applied to the motors
     */
    public void setGrippers(double output)
    {
        setLeftGripper(output);
        setRightGripper(output);
    }
}
