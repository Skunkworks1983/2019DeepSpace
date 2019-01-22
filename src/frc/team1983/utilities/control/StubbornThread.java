package frc.team1983.utilities.control;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.team1983.commands.CommandBase;

public class StubbornThread extends CommandBase implements PIDSource, PIDOutput
{
    CANSparkMax neoArm;
    private PIDController stubbornPid;
    private double initialPos;
    private AnalogInput encoder;
    PIDSourceType sourceType;

    public StubbornThread()
    {
        encoder = new AnalogInput(3);
    }
    public void initialize()
    {

        initialPos = encoder.getAverageValue();
        System.out.println("initial pos: "+initialPos);
        this.sourceType = PIDSourceType.kDisplacement;
        this.neoArm = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.neoArm.setInverted(true);
        this.neoArm.setSmartCurrentLimit(20);
        this.neoArm.setIdleMode(CANSparkMax.IdleMode.kBrake);
        this.stubbornPid = new PIDController(0.007,0.000,0.1, 0.0, this, this, .02);
        this.stubbornPid.setAbsoluteTolerance(17);
        //this.stubbornPid.setSetpoint(initialPos-150); //pushes down on ball
        this.stubbornPid.setSetpoint(initialPos);
        this.stubbornPid.setOutputRange(-1, 1);
        this.stubbornPid.enable();

    }
    public void execute()
    {
        //System.out.println("PID ENABLED " + stubbornPid.isEnabled());
        System.out.println("PID ERROR: " + stubbornPid.getError());
        //System.out.println("ENCODER VALUE : " + encoder.getAverageValue());
        //this.neoArm.set(0.1);

    }
    public void pidWrite(double out){


           this.neoArm.set(out);

    }
    public boolean isFinished()
    {
        return false;
    }
    public void interrupted(){this.end();}
    public void end()
    {
        this.stubbornPid.disable();
    }
    public AnalogInput getEncoder()
    {
        return encoder;
    }
    public double pidGet()
    {
        //System.out.println("PID GET CALLED");
        return this.encoder.getAverageValue();
    }
    public void setPIDSourceType(PIDSourceType pidSource) {
        this.sourceType = pidSource;
    }
    public PIDSourceType getPIDSourceType() {
        return this.sourceType;
    }

}
