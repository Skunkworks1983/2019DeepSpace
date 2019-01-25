package frc.team1983.commands.collector;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.team1983.commands.CommandBase;
import frc.team1983.subsystems.Collector;

/**
 * StubbornThread.java prevents the collector from moving, responding dynamically
 * to movement away from initial position. Designed for NEO integration and
 * uses external WPILib PidController instead of NEO built-in PID Controller
 * to facilitate use of an ANALOG ENCODER. Implements PIDSource and PIDOutput
 * referencing in-class methods.
 * Bind this command to Collector Intake Button with a whilePressed() to keep
 * collector at initial position pushing down on the ball, and ending when the ball
 * is released to regain control of Collector.
 */
public class StubbornThread extends CommandBase implements PIDSource, PIDOutput
{
    private PIDController stubbornPid;
    private double initialPos;
    private AnalogInput encoder;
    PIDSourceType sourceType;
    Collector collector;

    public StubbornThread()
    {
        encoder = new AnalogInput(3);
        collector = new Collector();
    }
    public void initialize()
    {
        initialPos = encoder.getAverageValue(); //getAverageValue is more accurate, less jitter/noise
        System.out.println("initial pos: "+initialPos);
        this.sourceType = PIDSourceType.kDisplacement;

        collector.getWristRight().setInverted(true);
        collector.getWristRight().setSmartCurrentLimit(20); //had issues with overheating (NEO smoked)
        collector.getWristRight().setIdleMode(CANSparkMax.IdleMode.kBrake); //we don't want it moving unless we tell it to, will fall slower in brake mode

        this.stubbornPid = new PIDController(0.007,0.000,0.1, 0.0, this, this, .02);
        this.stubbornPid.setAbsoluteTolerance(17);
        //this.stubbornPid.setSetpoint(initialPos-150); //pushes down on ball
        this.stubbornPid.setSetpoint(initialPos);
        this.stubbornPid.setOutputRange(-1, 1);
        this.stubbornPid.enable();
    }
    public void execute(){}
    public boolean isFinished(){return false;} //will run until button is released
    public void interrupted(){this.end();}
    public void end(){this.stubbornPid.disable();}
    public double pidGet(){return this.encoder.getAverageValue();} //PIDInput Implementation
    public void setPIDSourceType(PIDSourceType pidSource){this.sourceType = pidSource;} //PIDInput Implementation
    public PIDSourceType getPIDSourceType(){return this.sourceType;}//PIDInput Implementation
    public void pidWrite(double out){ collector.getWristRight().set(out);} //PIDOutput Implementation
}
