package frc.team1983.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.MotorMap;
import frc.team1983.services.logging.Logger;

/**
 * The manipulator is mounted to the elevator, and is how we score game pieces. It has four actuators on it:
 * The extender pushes the entire mechanism out and in. The claw grabs hatch panels by opening up. The rollers grab cargo.
 */
public class Manipulator extends Subsystem
{
    private Logger logger;

    private DoubleSolenoid extender;
    private DoubleSolenoid claw;

    private TalonSRX leftRoller;
    private TalonSRX rightRoller;

    private boolean isExtended;
    private boolean isOpen;

    /**
     * This constructor is mainly for unit testing.
     */
    public Manipulator(DoubleSolenoid extender, DoubleSolenoid claw, TalonSRX leftRoller, TalonSRX rightRoller, Logger logger)
    {
        this.extender = extender;
        this.claw = claw;
        this.leftRoller = leftRoller;
        this.rightRoller = rightRoller;
        this.logger = logger;

        isExtended = extender.get() == DoubleSolenoid.Value.kForward;
        isOpen = claw.get() == DoubleSolenoid.Value.kForward;
    }

    /**
     * This constructor creates all the solenoids and talons, and also inverts the talons
     */
    public Manipulator()
    {
        this(new DoubleSolenoid(MotorMap.Manipulator.EXTENDER_FORWARD, MotorMap.Manipulator.EXTENDER_REVERSE),
                new DoubleSolenoid(MotorMap.Manipulator.CLAW_FORWARD, MotorMap.Manipulator.CLAW_REVERSE),
                new TalonSRX(MotorMap.Manipulator.LEFT_ROLLER), new TalonSRX(MotorMap.Manipulator.RIGHT_ROLLER),
                Logger.getInstance());

        leftRoller.setInverted(MotorMap.Manipulator.LEFT_ROLLER_REVERSED);
        rightRoller.setInverted(MotorMap.Manipulator.RIGHT_ROLLER_REVERSED);
    }

    /**
     * @param isExtended If the manipulator should be extended or not
     */
    public void setExtender(boolean isExtended)
    {
        this.isExtended = isExtended;
        logger.debug((isExtended ? "Extending" : "Retracting") + " manipulator", this.getClass());
        extender.set(isExtended ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    /**
     * @param shouldOpen If the claw should be opened or closed
     */
    public void setOpen(boolean shouldOpen)
    {
        logger.debug((shouldOpen ? "Opening" : "Closing") + " claw", this.getClass());
        extender.set(isOpen ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
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
     * @return if the claw is currently open or closed
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
