package frc.team1983.utilities.control;

public class TalonSRX extends com.ctre.phoenix.motorcontrol.can.TalonSRX implements Motor
{
    public TalonSRX(int deviceNumber)
    {
        super(deviceNumber);
    }

    @Override
    public void set(double output)
    {

    }

    @Override
    public double get()
    {
        return 0;
    }
}
