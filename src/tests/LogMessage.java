package tests;

import java.util.Date;

/**
 * Created by User on 2017-01-13.
 */
public class LogMessage
{
    private Date logDate;

    private String logMessage;

    public LogMessage(Date logDate, String logMessage)
    {
        this.logDate = logDate;
        this.logMessage = logMessage;
    }

    public Date getLogDate()
    {
        return logDate;
    }

    public String getLogMessage()
    {
        return logMessage;
    }
}
