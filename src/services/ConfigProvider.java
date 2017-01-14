package services;

/**
 * Created by User on 2017-01-06.
 */
public class ConfigProvider
{
    private static String directory = "C:";

    public static String getRootDirectory()
    {
        return directory;
    }

    public static void setDirectory(String directory)
    {
        ConfigProvider.directory = directory;
    }

    public static String getServerIp()
    {
        return "localhost";
    }

    public static int getServerPort()
    {
        return 21370;
    }
}
