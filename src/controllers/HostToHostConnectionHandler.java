package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.FilePartResponse;
import messages.Request;
import messages.Response;
import tests.Logger;

public class HostToHostConnectionHandler implements Runnable
{
    private Socket connectionSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Request requestToSend;


    public HostToHostConnectionHandler(String ip, int port, Request request) throws IOException
    {
        connectionSocket = new Socket(ip,port);
        input = new ObjectInputStream(connectionSocket.getInputStream());
        output = new ObjectOutputStream(connectionSocket.getOutputStream());
        requestToSend = request;
    }


    @Override
    public void run()
    {
        Logger.logMessage("Connection to host "+ getRemoteHostName() + " started");
        try
        {
            output.writeObject(requestToSend);
            Logger.logMessage("Request "+requestToSend.getClass().getName()+" sent to host "+getRemoteHostName());
            while(true)
            {
                Response response = (Response) input.readObject();
                Logger.logMessage("Response "+response.getClass().getName()+" received from host "+getRemoteHostName());
                handleResponse(response);
            }
        }
        catch (ClassNotFoundException | IOException e )
        {
            //connection was closed
            tryCloseSocket();
        }
    }

    private String getRemoteHostName()
    {
        return connectionSocket.getInetAddress().toString()+":" + connectionSocket.getPort();
    }

    private void tryCloseSocket()
    {
        try
        {
            connectionSocket.close();
        }
        catch (IOException e)
        {
           //socket was already closed, so no need to close it again
        }
        Logger.logMessage("Connection to host "+getRemoteHostName()+" closed");
    }

    private void handleResponse(Response response)
    {
        HostResponseHandler handler = new HostResponseHandler();

        if(response instanceof FilePartResponse)
        {
            handler.handleFilePartResponse((FilePartResponse)response);
        }

    }
}
