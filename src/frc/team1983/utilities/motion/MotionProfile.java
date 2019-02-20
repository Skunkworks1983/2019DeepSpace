package frc.team1983.utilities.motion;

import frc.team1983.utilities.motors.FeedbackType;

public interface MotionProfile
{
    double evaluate(double time);
    double getDuration();
    double getEndpoint();
    FeedbackType getFeedbackType();

    /**
     * Generates a motion profile for motion magic
     *
     * @param startpoint     Where the system will start
     * @param endpoint       The desired position of the system
     * @param cruiseVelocity The maximum velocity this profile should achieve
     * @param acceleration   The maximum acceleration this profile should achieve
     */
    static MotionProfile generateProfile(double startpoint, double endpoint, double cruiseVelocity, double acceleration, FeedbackType feedbackType)
    {
        // Distance the system will travel (difference between current and desired position)
        double distance = Math.abs(endpoint - startpoint); int velocitySign = endpoint > startpoint ? 1 : -1;

        // Remember: The maximum height of the motion profile is cruiseVelocity, and the maximum slop is acceleration
        // The maximum length of one of the triangles (cruiseVelocity / t = acceleration -> cruiseVelocity / acceleration = t)
        double maxTriangleLength = cruiseVelocity / acceleration;
        // The max area of one of the triangles (triangle area = 1/2 * base * height)
        double maxTriangleSize = .5 * maxTriangleLength * cruiseVelocity;
        // If the system does not have time to reach its max velocity than the motion profile will be triangular
        boolean profileIsTriangular = distance <= 2 * maxTriangleSize;

        if(profileIsTriangular)
        {
            // Calculating the profile's max velocity and total time involves solving a system of equations.
            // The equations are:
            // maxVelocity / (.5 * profileLength) = acceleration
            // maxVelocity * .5 * profileLength = distance
            // first, maxVelocity / (.5 * profileLength) = acceleration -> 2 * maxVelocity / profileLength = acceleration ->
            //   2 * maxVelocity / acceleration = profileLength
            // next, maxVelocity * .5 * profileLength = distance -> maxVelocity * .5 * 2 * maxVelocity / acceleration = distance ->
            //   (maxVelocity ^ 2) / acceleration = distance -> (maxVelocity ^ 2) = distance * acceleration -> 
            //   maxVelocity = sqrt(distance * acceleration)
            // finally, put these together for profileLength = 2 * sqrt(distance * acceleration) / acceleration
            double profileLength = 2 * Math.sqrt(distance * acceleration) / acceleration;

            return feedbackType == FeedbackType.POSITION ?
                new MotionProfile()
                {
                    @Override
                    public double evaluate(double time)
                    {
                        return time <= .5 * profileLength ? // Are we in the first or second triangle?
                                // Evaluate the position by finding the area of the first triangle
                                startpoint + (velocitySign * .5 * time * time * acceleration) :
                                // Evaluate the position by finding the the remaining distance we need to travel
                                // during this profile and subtracting it from total distance
                                startpoint + (velocitySign * (distance - (.5 * (profileLength - time) * (profileLength - time) * acceleration)));
                    }

                    @Override
                    public double getDuration()
                    {
                        return profileLength;
                    }

                    @Override
                    public double getEndpoint()
                    {
                        return endpoint;
                    }

                    @Override
                    public FeedbackType getFeedbackType()
                    {
                        return FeedbackType.POSITION;
                    }
                } :
                new MotionProfile()
                {
                    @Override
                    public double evaluate(double time)
                    {
                        // Calculating velocity is easy, we check which part of the profile we are on and then
                        // multiply the distance from the beginning or end by the acceleration
                        return velocitySign * (time < .5 * profileLength ? time * acceleration :
                                (profileLength - time) * acceleration);
                    }

                    @Override
                    public double getDuration()
                    {
                        return profileLength;
                    }

                    @Override
                    public double getEndpoint()
                    {
                        return endpoint;
                    }

                    @Override
                    public FeedbackType getFeedbackType()
                    {
                        return FeedbackType.VELOCITY;
                    }
                };
        }
        else
        {
            // The size of the rectangle is how much distance is left after the triangles
            double rectangleSize = distance - (2 * maxTriangleSize);
            // Divide the rectangle area by its height to find the base
            double rectangleLength = rectangleSize / cruiseVelocity;

            // The length of the profile is length of the two triangles and the rectangle
            double profileLength = 2 * maxTriangleLength + rectangleLength;

            return feedbackType == FeedbackType.POSITION ?
                new MotionProfile()
                {
                    @Override
                    public double evaluate(double time)
                    {
                        // If we are in the first triangle, we can just find the area
                        if(time < maxTriangleLength) return startpoint + (velocitySign * .5 * time * time * acceleration);
                        // If we are at cruise velocity we can sum the first triangle with the area of the square we have
                        // traveled
                        if(time < maxTriangleLength + rectangleLength)
                            return startpoint + (velocitySign * (maxTriangleSize + ((time - maxTriangleLength) * cruiseVelocity)));
                        // If we are in the last triangle we can subtract the distance we need to travel from the total
                        // area of the profile
                        return startpoint + (velocitySign * (distance - (.5 * (profileLength - time) * (profileLength - time) * acceleration)));
                    }

                    @Override
                    public double getDuration()
                    {
                        return profileLength;
                    }

                    @Override
                    public double getEndpoint()
                    {
                        return endpoint;
                    }

                    @Override
                    public FeedbackType getFeedbackType()
                    {
                        return FeedbackType.POSITION;
                    }
                } :
                new MotionProfile()
                {
                    @Override
                    public double evaluate(double time)
                    {
                        // Velocity is easy. We simply multiply the time by the acceleration.
                        if(time < maxTriangleLength) return velocitySign * time * acceleration;
                        // Or just return the cruise velocity if we are in the square
                        if(time < maxTriangleLength + rectangleLength) return velocitySign * cruiseVelocity;
                        // Or multiply distance from the end by acceleration
                        return velocitySign * (profileLength - time) * acceleration;
                    }

                    @Override
                    public double getDuration()
                    {
                        return profileLength;
                    }

                    @Override
                    public double getEndpoint()
                    {
                        return endpoint;
                    }

                    @Override
                    public FeedbackType getFeedbackType()
                    {
                        return FeedbackType.VELOCITY;
                    }
                };
    }
    }
}
