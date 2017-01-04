package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.Request;
import messages.Response;

public class HostConnectionHandler implements Runnable
{
    private Socket connectionSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Request requestToSend;

    public HostConnectionHandler(String ip, int port, Request request) throws IOException
    {
        connectionSocket = new Socket(ip,port);
        input = new ObjectInputStream(connectionSocket.getInputStream());
        output = new ObjectOutputStream(connectionSocket.getOutputStream());
        requestToSend = request;
    }


    @Override
    public void run()
    {
        try
        {
            output.writeObject(requestToSend);
            while(true)
            {
                Response response = (Response) input.readObject();
                handleResponse(response);
            }
        }
        catch (Exception e)
        {
            //connection was closed
            tryCloseSocket();
            System.out.println("huj zamkłem sie");
        }
    }

    private void tryCloseSocket()
    {
        try
        {
            connectionSocket.close();
        }
        catch (IOException e)
        {
           
        }
    }

    private void handleResponse(Response response)
    {
        System.out.println(response.getStatus());

    }
}
