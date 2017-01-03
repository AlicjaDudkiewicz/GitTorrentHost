package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import messages.Request;
import messages.Response;
import views.MainWindowController;

public class HostDispatcher
{
    private ServerSocket serverSocket;
    private Socket socketForClient;
    private Socket clientSocket;
    private MainWindowController viewController;

    public HostDispatcher(MainWindowController viewController)
    {
        this.viewController = viewController;
        this.viewController.setDispatcher(this);
    }

    public void activateServer(int port)
    {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try
        {
            serverSocket = new ServerSocket(port); 
        } catch (IOException e)
        {
            e.printStackTrace();

        }
        while (true)
        {
            try
            {
                socket = serverSocket.accept();
            } catch (IOException e)
            {
                System.out.println("I/O error: " + e);
            }
            new Thread(new HostClientSocketThread(socket)).start();
        }
    }

    public void connectToServer(String host, int port)
    {
        try
        {
            clientSocket = new Socket(host, port);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendRequest(Request request)

    {

        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(
                    clientSocket.getOutputStream());

            oos.writeObject(request);
            ObjectInputStream ois = new ObjectInputStream(
                    clientSocket.getInputStream());
            try
            {

                Response response = (Response) ois.readObject();
                System.out.println(response.getStatus());

            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            oos.close();
            ois.close();
            clientSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
