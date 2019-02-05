package frc.team1983.utilities.control;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.Transmission;

import java.util.HashMap;
import java.util.function.Function;

public class Profiler extends Thread
{
    private Transmission transmission;
    private double kP, kI, kD;
    private HashMap<String, Function<Double, Double>> feedForwards;

    private double prevVel, prevTime, cumulativeError, targetVelocity;
    private ControlMode controlMode;
    private Logger logger = Logger.getInstance();
    private Function<Double, Double> motionProfile;
    private double profileLength;
    private boolean runMotionProfile;
    private long profileStartTime;

    public Profiler(Transmission transmission, double kP, double kI, double kD,
                    HashMap<String, Function<Double, Double>> feedForwards)
    {
        this.transmission = transmission;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.feedForwards = feedForwards;

        //These should probably be reset again before first execution, because construction usually happens a long time
        // before execution, but this at least prevents null pointer exceptions
        prevVel = transmission.getTicksPerSecond();
        prevTime = System.currentTimeMillis();
        cumulativeError = 0;
        targetVelocity = 0;
        controlMode = ControlMode.PercentOutput;
        runMotionProfile = false;
    }

    public Profiler(Transmission transmission, double kP, double kI, double kD)
    {
        this(transmission, kP, kI, kD, new HashMap<>());
    }

    @Override
    public void run()
    {
        if (controlMode != ControlMode.MotionMagic && controlMode != ControlMode.Velocity) return;
        if (controlMode == ControlMode.MotionMagic)
        {
            if(!runMotionProfile)
            {
                transmission.set(ControlMode.PercentOutput, 0);
                return;
            }
            // Cast 1000 to double to prevent integer division
            double time = (System.currentTimeMillis() - profileStartTime) / ((double) 1000);
            targetVelocity = motionProfile.apply(time);
            if(time > profileLength)
            {
                targetVelocity = 0;
                transmission.set(ControlMode.PercentOutput, 0);
                runMotionProfile = false;
                logger.info("Profiler for " + transmission.getName() + " finished its motion profile",
                        this.getClass());
            }
        }
        calculateVelocityPIDF(targetVelocity);
    }

    /**
     * @param key      A string that can be used to access this feed forward term
     * @param function A lambda which takes in the current position of the system and returns a percentoutput
     *                 that will be added to the output
     */
    public synchronized void addFeedForwardFunction(String key, Function<Double, Double> function)
    {
        feedForwards.put(key, function);
    }

    /**
     * @param key The key of the feed forward function that should no longer be applied to the velocity controller
     */
    public synchronized void removeFeedForwardFunction(String key)
    {
        feedForwards.remove(key);
    }

    /**
     * @param target The target velocity
     */
    private void calculateVelocityPIDF(double target)
    {
        double currentVel = transmission.getTicksPerSecond();
        double currentTime = System.currentTimeMillis();
        double currentPos = transmission.getPositionTicks();

        double error = target - currentVel; // Current error
        double de = currentVel - prevVel; // Change in error since last calculation
        double dt = currentTime - prevTime; // Change in time since last calculation
        double output; // Percent output to be applied to the motor

        output = error * kP;
        cumulativeError += error * dt;
        output += cumulativeError * kI;
        output -= de / dt * kD;

        for (Function<Double, Double> feedForward : feedForwards.values())
            output += feedForward.apply(currentPos);

        transmission.set(ControlMode.PercentOutput, output);

        prevVel = currentVel;
        prevTime = currentTime;
    }

    /**
     * General function for setting the transmission. Generally should only be MotionMagic or Velocity, but
     * PercentOutput is also supported. MotionMagic will not start automatically, but Velocity will, so be careful.
     *
     * @param controlMode The control mode that the value should set to
     * @param value       For PercentOutput: double between -1 and 1. For Velocity: a speed (ticks / second), not
     *                    bounded by the configured max velocity or acceleration, so be careful. For MotionMagic: the
     *                    setpoint that the system should be driven towards, bound by the min and max system positions
     */
    public synchronized void set(ControlMode controlMode, double value)
    {
        runMotionProfile = false;
        transmission.set(ControlMode.PercentOutput, 0); // Stop the transmission
        this.controlMode = controlMode;

        switch (controlMode)
        {
            case PercentOutput:
                transmission.set(controlMode, value);
                break;
            case MotionMagic:
                generateMotionProfile((int) value);
                break;
            case Velocity:
                targetVelocity = value;
                break;
            default:
                logger.warn("Profiler for " + transmission.getName() + " was set to an invalid control mode",
                        this.getClass());
        }
    }

    /**
     * Generates a motion profile for motion magic
     * @param setpoint The desired position of the system
     */
    public void generateMotionProfile(int setpoint)
    {
        double vMax = transmission.getMaxVelocity(); // The maximum velocity the system can safely travel at
        double aMax = transmission.getMaxAcceleration(); // The maximum acceleration the system can safely achieve

        // Distance the system will travel (difference between current and desired position)
        double distance = Math.abs(setpoint - transmission.getPositionTicks());

        // Remember: The maximum height of the motion profile is vMax, and the maximum slop is aMax
        // The maximum length of one of the triangles (vMax / t = aMax -> vMax / aMax = t)
        double maxTriangleLength = vMax / aMax;
        // The max area of one of the triangles (triangle area = 1/2 * base * height)
        double maxTriangleSize = .5 * maxTriangleLength * vMax;
        // If the system does not have time to reach its max velocity than the motion profile will be triangular
        boolean profileIsTriangular = distance <= 2 * maxTriangleSize;

        if (profileIsTriangular)
        {
            // Calculating the profile's max velocity and total time involves solving a system of equations.
            // The equations are:
            // vMax / (.5 * profileLength) = aMax
            // vMax * .5 * profileLength = distance
            // first, vMax / (.5 * profileLength) = aMax -> 2 * vMax / profileLength = aMax ->
            //   2 * vMax / aMax = profileLength
            // next, vMax * .5 * profileLength = distance -> vMax * .5 * 2 * vMax / aMax = distance ->
            //   (vMax ^ 2) / aMax = distance -> (vMax ^ 2) = distance * aMax -> vMax = sqrt(distance * aMax)
            double profileVMax = Math.sqrt(distance * aMax);
            profileLength = 2 * profileVMax / aMax;

            // Create a lambda expression to express the desired velocity as a function of time
            motionProfile = time -> time < .5 * profileLength ? time * aMax : (profileLength - time) * aMax;
        } else
        {
            // The size of the rectangle is how much distance is left after the triangles
            double rectangleSize = distance - (2 * maxTriangleSize);
            // Divide the rectangle area by its height to find the base
            double rectangleLength = rectangleSize / vMax;

            // The length of the profile is length of the two triangles and the rectangle
            profileLength = 2 * maxTriangleLength + rectangleLength;

            // Create a lambda expression to express the desired velocity as a function of time
            motionProfile = time ->
            {
                if(time < maxTriangleLength) return time * aMax;
                if(time < maxTriangleLength + rectangleLength) return vMax;
                return (profileLength - time) * aMax;
            };
        }
    }

    /**
     * Starts the motion magic motion profile
     */
    public synchronized void startMotionMagic()
    {
        profileStartTime = System.currentTimeMillis();
        runMotionProfile = true;
    }

    /**
     * Stops the motion magic motion profile
     */
    public synchronized void stopMotionMagic()
    {
        runMotionProfile = false;
    }

    public synchronized void setkP(double kP)
    {
        this.kP = kP;
    }

    public synchronized void setkI(double kI)
    {
        this.kI = kI;
    }

    public synchronized void setkD(double kD)
    {
        this.kD = kD;
    }
}
