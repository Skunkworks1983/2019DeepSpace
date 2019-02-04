package frc.team1983.utilities.control;

import frc.team1983.utilities.motors.Transmission;

import java.util.ArrayList;
import java.util.function.Function;

public class Profiler
{
    private ArrayList<Function<Double, Double>> errorTerms;
    private Transmission transmission;

    public Profiler(Transmission transmission)
    {
        this.transmission = transmission;
    }

    /*
    to be used as:

    errorTerms.add(error -> kp * error);
    errorTerms.add(error -> 0.1);
     */
    private double evaluateOutput()
    {
        double output = 0;
        double error = 0;
        for(Function<Double, Double> function : errorTerms)
            output += function.apply(error);
        return output;
    }
}

