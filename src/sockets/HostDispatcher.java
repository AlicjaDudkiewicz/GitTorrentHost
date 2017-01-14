package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import controllers.HostToHostConnectionHandler;
import controllers.HostToHostResponder;
import controllers.MainServerResponseHandler;
import messages.Request;
import messages.Response;
import model.Host;
import services.ConfigProvider;
import tests.Logger;
import views.MainWindowController;

public class HostDispatcher
{
    private Socket               serverSocket;
    private MainWindowController viewController;
    private MainServerResponseHandler mainServerResponseHandler;
    private ObjectOutputStream serverOutputStream;
    private ObjectInputStream serverInputStream;
    private ServerSocket internalServerSocket;


    public HostDispatcher(MainWindowController viewController)
    {
        this.viewController = viewController;
        this.viewController.setDispatcher(this);
        this.mainServerResponseHandler = new MainServerResponseHandler(viewController);
        new Thread(this::activateServer).start();
    }

    public void activateServer()
    {
        try
        {
            internalServerSocket = new ServerSocket(0);
            while (true)
            {
                Socket socket = internalServerSocket.accept();
                new Thread(new HostToHostResponder(socket)).start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendRequestToHost(String ip, int port, Request request)
    {
        try
        {
            new Thread(new HostToHostConnectionHandler(ip, port, request)).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void connectToServer()
    {
        try
        {
            serverSocket = new Socket(ConfigProvider.getServerIp(), ConfigProvider.getServerPort());
            serverOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            serverInputStream = new ObjectInputStream(serverSocket.getInputStream());
            viewController.updateConnectionStatus("ONLINE");
        }
        catch (IOException e)
        {
            viewController.showConnectErrorWindow();
        }
    }

    public void sendRequestToServer(Request request)
    {
        if(serverSocket!=null && serverSocket.isConnected())
        {
            try
            {
                request.setHost(new Host(internalServerSocket.getInetAddress().getHostAddress(), internalServerSocket.getLocalPort()));
                serverOutputStream.writeObject(request);
                Logger.logMessage("Request " + request.getClass().getName() + " sent to main server");
                try
                {
                    Response response = (Response) serverInputStream.readObject();
                    mainServerResponseHandler.serveResponse(response);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void closeServerConnection()
    {
        try
        {
            serverSocket.close();
        }
        catch (IOException e)
        {
            System.out.println("Connection was already closed");
        }
    }

}
