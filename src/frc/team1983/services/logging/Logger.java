package frc.team1983.services.logging;

import java.util.HashMap;

public class Logger {
    private static Logger ourInstance = new Logger();

    public static Logger getInstance() {
        return ourInstance;
    }

    private HashMap<String, Level> exceptionsList;

    private Level globalLevel = Level.INFO;

    private Logger() {
        exceptionsList = new HashMap<>();
    }

    public void setGlobalLevel(Level newGlobalPrintLevel) {
        this.globalLevel = newGlobalPrintLevel;
    }

    public void addPrintException(Class exceptedClass, Level newLevel)
    {
        exceptionsList.put(exceptedClass.toString(), newLevel);
    }

    public void trace(String message, Class yourClass)
    {
        log(Level.TRACE, message, yourClass);
    }

    public void debug(String message, Class yourClass)
    {
        log(Level.DEBUG, message, yourClass);
    }

    public void info(String message, Class yourClass)
    {
        log(Level.INFO, message, yourClass);
    }

    public void warn(String message, Class yourClass)
    {
        log(Level.WARN, message, yourClass);
    }

    public void error(String message, Class yourClass)
    {
        log(Level.ERROR, message, yourClass);
    }

    private void log(Level level, String message, Class objectClass)
    {
        int minPriority = globalLevel.PRIORITY;

        if(exceptionsList.containsKey(objectClass.toString()))
        {
            minPriority = exceptionsList.get(objectClass.toString()).PRIORITY;
        }

        if(level.PRIORITY >= minPriority)
        {
            System.out.println(objectClass.toString() + " " + level.name() + ": " + message);
        }
    }
}
