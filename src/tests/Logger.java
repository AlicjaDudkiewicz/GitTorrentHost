package tests;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 2017-01-13.
 */
public class Logger
{
    private static ArrayList<LogMessage> logs = new ArrayList<>();

    public static void logMessage(String message)
    {
        logs.add(new LogMessage(new Date(), message));
    }

    public static ArrayList<LogMessage> getLogs()
    {
        return logs;
    }
}
