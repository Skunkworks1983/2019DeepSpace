# 2019DeepSpace
[![Build Status](https://travis-ci.com/Skunkworks1983/2019DeepSpace.svg?token=LRT7pzmjJLyz9XdCdnjU&branch=master)](https://travis-ci.com/Skunkworks1983/2019DeepSpace)
[![codecov](https://codecov.io/gh/Skunkworks1983/2019DeepSpace/branch/master/graph/badge.svg?token=oHTQ86GQVg)](https://codecov.io/gh/Skunkworks1983/2019DeepSpace)

Skunk Works Robotics' code for FRC 2019 Destination: Deep Space

**Cool features:**
- Automated continuous integration and code coverage using Travis and Codecov
- Intuitive custom motor and sensor abstractions (utilities/motors/, utilities/sensors)
- Custom PID and motion profiling system (MotorGroupController.java, MotionProfile.java)
- Vectors, splines and linecasting (Vector2.java, Bezier.java, Line.java)
- Linear robot state estimation (StateEstimator.java)
- Pure pursuit path following (Pose.java, Path.java, PurePursuitController.java)
- Custom live dashboard for visualizing path splines and the robot's position on the field (smellydash/)
- (buggy) Superstructure coordination code (Collector.java, Elevator.java)
- Automated climbing sequence (Climb.java)
- Gyro-based arcade drive (RunGyroDrive.java)

Thanks Mr. Vader :)
