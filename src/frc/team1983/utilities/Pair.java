package frc.team1983.utilities;

// Represents a pair of two arbitrary types and values.
public class Pair<Type1, Type2>
{
    private Type1 value1;
    private Type2 value2;

    public Pair(Type1 value1, Type2 value2)
    {
        this.value1 = value1;
        this.value2 = value2;
    }

    public Type1 getValue1()
    {
        return value1;
    }

    public Type2 getValue2()
    {
        return value2;
    }

    public void setValue1(Type1 value)
    {
        value1 = value;
    }

    public void setValue2(Type2 value)
    {
        value2 = value;
    }
}
