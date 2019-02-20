package frc.team1983;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_Mockito
{
    @Mock
    private ClassToBeMocked mockedClass;

    @Before
    public void setup()
    {
        initMocks(this);
    }

    @Test
    public void mockitoOverridesActualMethodCall()
    {
        when(mockedClass.returnFalse()).thenReturn(true);

        Assert.assertTrue(mockedClass.returnFalse());
    }

    static class ClassToBeMocked
    {
        boolean returnFalse()
        {
            return false;
        }
    }
}
