package frc.team1983.utilities.control;

import frc.team1983.Constants;
import frc.team1983.Robot;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;

public class PurePursuitController implements Runnable
{
//    private final static double LOOK_AHEAD_DISTANCE = Constants.Drivebase.Path.LOOK_AHEAD_DISTANCE;
//    private final static double PATH_RESOLUTION = Constants.Drivebase.Path.PATH_RESOLUTION;
//
//    private final static double MAX_VELOCITY = Constants.Drivebase.MAX_VELOCITY;
//    private final static double THROTTLE = Constants.Drivebase.Path.THROTTLE;
//    private final static double TRACK = Constants.Drivebase.TRACK;
//
//    private final static double K_P = Constants.Drivebase.Path.K_P;
//    private final static double K_I = Constants.Drivebase.Path.K_I;
//    private final static double K_D = Constants.Drivebase.Path.K_D;
//    private final static double K_F = Constants.Drivebase.Path.K_F;

    private Drivebase drivebase;
    private StateEstimator estimator;
    private Thread thread;

    private Path path;
    private int count = 0;

    // TODO: remove (for debug)
    private Vector2 target = new Vector2(-4.54, 17.66);
    private double targetHeading = -90.0;

    public PurePursuitController(Drivebase drivebase, StateEstimator estimator, Path path)
    {
        this.drivebase = drivebase;
        this.estimator = estimator;
        this.path = path;

        thread = new Thread(this);
    }

    public PurePursuitController(Path path)
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getEstimator(), path);
    }

    public synchronized void start()
    {
        thread.start();
    }

    public synchronized void reset()
    {
        estimator.reset();
    }

    protected synchronized void execute()
    {
        double heading = estimator.getHeading();
        Vector2 position = estimator.getPosition();
        double robotT = path.evaluateClosestT(position); // Get the t value of how far the robot is in the path
//        System.out.println(position + ", " + heading);

        // Calculate the t of the look ahead point
        double t = 0;
        Vector2 point;
        double dist;
        double smallestError = Double.MAX_VALUE;

        // Loop through points on path
        for (int i = 0; i <= PATH_RESOLUTION; i++)
        {
            double step = Math.min(i * (1.0 / PATH_RESOLUTION), 1.0);
            point = path.evaluate(step);

            // Skip if this point is behind the robot, we don't want to target a point behind the robot
            if (step < robotT)
            {
                i++;
                continue;
            }

            // Find the distance to the curve
            dist = point.getDistanceTo(position);
            double error = Math.abs(LOOK_AHEAD_DISTANCE - dist);

            // If the new distance is shorter, set shortestDistance to new distance
            if (error < smallestError)
            {
                t = step;
                smallestError = error;
            }
        }

        // Set up points
        point = path.evaluate(t);
        dist = point.getDistanceTo(position);

        // Get radius of curvature, r, between robot and the look ahead point
        double r;
        double dx = point.getX() - position.getX();
        double dy = point.getY() - position.getY();

        if (dy == 0) r = dist / 2.0 * Math.signum(dx);
        else if (dx == 0) r = 0;
        else
        {
            double dtheta = (Math.atan(dx / dy) - heading) * Math.signum(dy);

            // Law of sines
            r = (dist * Math.sin(Math.PI / 2 - dtheta)) / (Math.sin(2 * dtheta));
        }

        double v = MAX_VELOCITY * THROTTLE;
        double vl = v, vr = v;

        if (robotT >= 1.0)
        {
            vl = 0;
            vr = 0;
        }
        else if(r != 0)
        {
            vl = (v * (r + TRACK/2)) / r;
            vr = (v * (r - TRACK/2)) / r;
        }

//        System.out.println(Math.toDegrees(heading));
//        System.out.println(r);
//        System.out.println(position + ", " + robotT + ", " + point + ", " + t/*+ ", " + position + ", " + dist + ", " + r + ", " + t + ", " + vl + ", " + vr*/);
//        if (count % 10 == 0) System.out.println(t + ", " + point + ", " + r);
//        count++;

        System.out.println(Vector2.sub(position, target) + ", " + (Math.toDegrees(heading) - targetHeading));
        drivebase.setMotors(-vl / MAX_VELOCITY, -vr / MAX_VELOCITY);
    }

    @Override
    public void run()
    {
        while(true)
        {
            execute();

            try
            {
                Thread.sleep((long) 1000.0 / Constants.PurePursuitController.UPDATE_RATE);
            }
            catch(InterruptedException exception)
            {
                exception.printStackTrace();
            }
        }
    }
}
