package frc.team1983.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.CollectorConstants;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.Spark;
import frc.team1983.utilities.motors.Transmission;

public class Collector extends Subsystem
{
    private Transmission wrist;
    private Spark roller;
    private double targetPosition;
    private double initialEncoderPosition;

    public Collector()
    {
        wrist = new Transmission("Collector wrist", FeedbackType.POSITION,
                new Spark(RobotMap.Collector.LEFT, RobotMap.Collector.LEFT_REVERSED),
                new Spark(RobotMap.Collector.RIGHT, RobotMap.Collector.RIGHT_REVERSED));

        roller = new Spark(RobotMap.Collector.)
    }
    @Override
    public void initDefaultCommand(){}
}
