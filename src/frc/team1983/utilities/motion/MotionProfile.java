package frc.team1983.utilities.motion;

public interface MotionProfile
{
    double evaluatePosition(double time);
    double evaluateVelocity(double time);
    double getDuration();

    /**
     * Generates a motion profile for motion magic
     *
     * @param startpoint     Where the system will start
     * @param endpoint       The desired position of the system
     * @param cruiseVelocity The maximum velocity this profile should achieve
     * @param acceleration   The maximum acceleration this profile should achieve
     */
    static MotionProfile generateTrapezoidalProfile(double startpoint, double endpoint, double cruiseVelocity, double acceleration)
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

            return new MotionProfile()
            {
                @Override
                public double evaluatePosition(double time)
                {
                    return velocitySign * (time < .5 * profileLength ? time * acceleration : (profileLength - time) * acceleration);
                }

                @Override
                public double evaluateVelocity(double time)
                {
                    return time <= .5 * profileLength ? startpoint + (velocitySign * .5 * time * time * acceleration) : startpoint + (velocitySign * (distance - (.5 * (profileLength - time) * (profileLength - time) * acceleration)));
                }

                @Override
                public double getDuration()
                {
                    return profileLength;
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

            return new MotionProfile()
            {
                @Override
                public double evaluatePosition(double time)
                {
                    if(time < maxTriangleLength) return velocitySign * time * acceleration;
                    if(time < maxTriangleLength + rectangleLength) return velocitySign * cruiseVelocity;
                    return velocitySign * (profileLength - time) * acceleration;
                }

                @Override
                public double evaluateVelocity(double time)
                {
                    if(time < maxTriangleLength) return startpoint + (velocitySign * .5 * time * time * acceleration);
                    if(time < maxTriangleLength + rectangleLength)
                        return startpoint + (velocitySign * (maxTriangleSize + ((time - maxTriangleLength) * cruiseVelocity)));
                    return startpoint + (velocitySign * (distance - (.5 * (profileLength - time) * (profileLength - time) * acceleration)));
                }

                @Override
                public double getDuration()
                {
                    return profileLength;
                }
            };
        }
    }
}
