package frc.team1983.utilities.sensors;

public class DigitalInputEncoder extends edu.wpi.first.wpilibj.Encoder implements Encoder, Runnable
{
    private static final int UPDATE_RATE = 20;

    private double velocity;

    private double prevPos;
    private long prevTime;

    public DigitalInputEncoder(int channelA, int channelB)
    {
        super(channelA, channelB);
        new Thread(this).start();
    }

    public DigitalInputEncoder(int channelA)
    {
        this(channelA, channelA + 1);
    }

    @Override
    public void configure()
    {

    }

    @Override
    public double getPosition()
    {
        return get();
    }

    @Override
    public double getVelocity()
    {
        return velocity;
    }

    @Override
    public void run()
    {
        while(true)
        {
            double currentPos = getPosition();
            long currentTime = System.currentTimeMillis();

            //(double) casts ints to doubles, preventing integer division
            velocity = (((double) (currentPos - prevPos)) / ((double) (currentTime - prevTime)));

            prevPos = currentPos;
            prevTime = currentTime;

            try
            {
                Thread.sleep((long) 1000.0 / UPDATE_RATE);
            }
            catch(InterruptedException exception)
            {
                exception.printStackTrace();
            }
        }
    }
}