package frc.team1983.utilities.motors;

import edu.wpi.first.wpilibj.AnalogInput;

public class ThreadedEncoder extends Thread implements Encoder
{
    private AnalogInput encoder;

    private int position;
    private double velocity;

    private int prevPos;
    private long prevTime;

    public ThreadedEncoder(int port)
    {
        encoder = new AnalogInput(port);

        prevPos = encoder.getValue();
        prevTime = System.currentTimeMillis();
    }

    public synchronized int getPos()
    {
        return this.position;
    }

    public synchronized double getVel()
    {
        return velocity;
    }

    @Override
    public void run()
    {
        int currentPos = encoder.getValue();
        long currentTime = System.currentTimeMillis();

        //(double) casts ints to doubles, preventing integer division
        velocity = (((double) (currentPos - prevPos)) / ((double) (currentTime - prevTime)));

        prevPos = currentPos;
        prevTime = currentTime;
    }
}
