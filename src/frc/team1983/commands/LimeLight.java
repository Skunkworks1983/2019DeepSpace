package frc.team1983.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;

import java.util.Arrays;

public class LimeLight extends Command
{
    private double k = 7.833340412119258;
    private double cameraOffset = 9/12;
    // 4ft = 7.911448100528959
    // 5ft = 7.843946872383423
    // 6ft = 7.808656800069443
    // 7ft = 7.769309875495202
    public double distance;

    public LimeLight()
    {
        //requires(Robot.getInstance().getDrivebase());
    }

    @Override
    public void initialize()
    {

    }

    @Override
    public void execute()
    {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        NetworkTableEntry tv = table.getEntry("tv");
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");
        NetworkTableEntry ts = table.getEntry("ts");
        NetworkTableEntry camtran = table.getEntry("camtran");

        //read values periodically
        double targetDetected = tv.getDouble(0);
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);
        double skew = ts.getDouble(0.0);
        double[] pnp = camtran.getDoubleArray(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0});


//        NetworkTableEntry[] foolist = NetworkTableInstance.getDefault().getEntries("", 0xff);
//
//        //System.out.println("got table");
//        for (NetworkTableEntry ent: foolist)
//        {
//            System.out.println(ent.getName());
//        }

        distance = (k / Math.sqrt(area));

        for(int i = 0; i < pnp.length; i++)
        {
            if (i < pnp.length - 1)
                System.out.print(pnp[i] + ", \t");
            else
                System.out.println(pnp[i]);
        }

        //System.out.println("Distance: " + distance);

    }

    @Override
    public boolean isFinished()
    {
        return isTimedOut();
    }

    @Override
    public void interrupted()
    {
        end();
    }

    public void end()
    {

    }
}
