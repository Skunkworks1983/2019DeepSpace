package frc.team1983.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.MotorMap;
import frc.team1983.services.logging.Logger;

/**
 * The manipulator is mounted to the elevator, and is how we score game pieces. It has four actuators on it:
 * The extender pushes the entire mechanism out and in. The hooks grabs hatch panels by opening up. The rollers grab cargo.
 */
public class Manipulator extends Subsystem
{
    private Logger logger;

    private DoubleSolenoid extender;
    private DoubleSolenoid hooks;

    private TalonSRX leftRoller;
    private TalonSRX rightRoller;

    private boolean isExtended;
    private boolean isOpen;

    /**
     * This constructor is mainly for unit testing.
     */
    public Manipulator(DoubleSolenoid extender, DoubleSolenoid hooks, TalonSRX leftRoller, TalonSRX rightRoller, Logger logger)
    {
        this.extender = extender;
        this.hooks = hooks;
        this.leftRoller = leftRoller;
        this.rightRoller = rightRoller;
        this.logger = logger;

        isExtended = extender.get() == DoubleSolenoid.Value.kForward;
        isOpen = hooks.get() == DoubleSolenoid.Value.kForward;
    }

    /**
     * This constructor creates all the solenoids and talons, and also inverts the talons
     */
    public Manipulator()
    {
        this(new DoubleSolenoid(MotorMap.Manipulator.EXTENDER_FORWARD, MotorMap.Manipulator.EXTENDER_REVERSE),
                new DoubleSolenoid(MotorMap.Manipulator.HOOKS_FORWARD, MotorMap.Manipulator.HOOKS_REVERSE),
                new TalonSRX(MotorMap.Manipulator.LEFT_ROLLER), new TalonSRX(MotorMap.Manipulator.RIGHT_ROLLER),
                Logger.getInstance());

        leftRoller.setInverted(MotorMap.Manipulator.LEFT_ROLLER_REVERSED);
        rightRoller.setInverted(MotorMap.Manipulator.RIGHT_ROLLER_REVERSED);
    }

    /**
     * @param shouldExtend If the manipulator should be extended or not
     */
    public void setExtender(boolean shouldExtend)
    {
        extender.set(shouldExtend ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        logger.debug((shouldExtend ? "Extending" : "Retracting") + " manipulator", this.getClass());
        this.isExtended = shouldExtend;
    }

    /**
     * @param shouldOpen If the hooks should be opened or closed
     */
    public void setHooks(boolean shouldOpen)
    {
        hooks.set(shouldOpen ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        logger.debug((shouldOpen ? "Opening" : "Closing") + " hooks", this.getClass());
        this.isOpen = shouldOpen;
    }

    /**
     * @param output The percentoutput that should be applied to the motor
     */
    public void setLeftRoller(double output)
    {
        leftRoller.set(ControlMode.PercentOutput, output);
    }

    /**
     * @param output The percentoutput that should be applied to the motor
     */
    public void setRightRoller(double output)
    {
        rightRoller.set(ControlMode.PercentOutput, output);
    }

    /**
     * @param output The percentoutput that should be applied to the motors
     */
    public void setRollers(double output)
    {
        setLeftRoller(output);
        setRightRoller(output);
    }

    /**
     * @return if the manipulator is currently extended or not
     */
    public boolean isExtended()
    {
        return isExtended;
    }

    /**
     * @return if the hooks is currently open or closed
     */
    public boolean isOpen()
    {
        return isOpen;
    }

    @Override
    protected void initDefaultCommand()
    {

    }
}
