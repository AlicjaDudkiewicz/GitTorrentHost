package tests;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Supplier;

import http.HttpProcessors;

public class HttpServer
{
    private static HttpServer   instance     = null;
    private        int          serverPort   = 0;
    private        ServerSocket serverSocket = null;

    private HttpServer()
    {
        tryStartServer();
        new Thread(this::startHttpServer).start();
    }

    private void tryStartServer()
    {
        try
        {
            serverSocket = new ServerSocket(0);
            serverPort = serverSocket.getLocalPort();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static HttpServer getInstance()
    {
        if (instance == null)
        {
            instance = new HttpServer();
        }
        return instance;
    }

    private void startHttpServer()
    {
        try
        {
            while (true)
            {
                Socket connection = serverSocket.accept();
                new Thread(() -> serveHttpRequest(connection)).start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void serveHttpRequest(Socket connection)
    {
        try
        {
            Scanner scanner = new Scanner(connection.getInputStream());
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            ArrayList<String> request = new ArrayList<>();

            HashMap<String, Supplier<String>> processors = new HashMap<>();
            processors.put("#'time'", HttpProcessors::getServerTime);
            processors.put("#'table'", HttpProcessors::getLogTable);
            processors.put("#'rootdir'", HttpProcessors::getRootDirectory);

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                request.add(line);
                if (line.equals(""))
                {
                    break;
                }
            }

            boolean isCss = false;
            if (request.get(0).contains(".css"))
            {
                isCss = true;
            }
            String[] split = request.get(0).split(" ");

            String resource = split[1];
            if (resource.equals("") || resource.equals("/"))
            {
                resource = "/index.html";
            }

            writeHttpHeaders(writer, isCss);

            Scanner fileScanner = new Scanner(HttpServer.class.getResourceAsStream("../http" + resource));
            while (fileScanner.hasNextLine())
            {
                String line = fileScanner.nextLine();

                line = transformLine(processors, line);

                writer.println(line);
            }

            writer.flush();
            connection.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void writeHttpHeaders(PrintWriter writer, boolean isCss)
    {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Date: " + HttpProcessors.getServerTime());
        if (isCss)
        {
            writer.println("Content-Type: text/css; charset=utf-8");
        }
        else
        {
            writer.println("Content-Type: text/html; charset=utf-8");
        }
        writer.println("Server: GitTorrent Http Server");
        writer.println("Connection: close");
        writer.println("");
    }

    private String transformLine(HashMap<String, Supplier<String>> processors, String line)
    {
        for (String s : processors.keySet())
        {
            if (line.contains(s))
            {
                line = line.replaceAll(s, processors.get(s).get());
            }
        }
        return line;
    }

    public int getServerPort()
    {
        return serverPort;
    }
}
