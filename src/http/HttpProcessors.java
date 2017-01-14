package http;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import services.ConfigProvider;
import tests.LogMessage;
import tests.Logger;

/**
 * Created by User on 2017-01-13.
 */
public class HttpProcessors
{
    public static String getServerTime()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    public static String getRootDirectory()
    {
        return ConfigProvider.getRootDirectory();
    }

    public static String getLogTable()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("<table>");
        for(LogMessage log: Logger.getLogs())
        {
            builder.append("<tr>");
            builder.append("<td>"+log.getLogDate().toString()+"</td>");
            builder.append("<td>"+ log.getLogMessage()+"</td>");
            builder.append("</tr>");
        }
        builder.append("</table>");

        return builder.toString();
    }

}
