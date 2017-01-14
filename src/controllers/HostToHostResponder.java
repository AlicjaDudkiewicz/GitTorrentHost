package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import messages.FilePartRequest;
import messages.Request;
import messages.Response;


public class HostToHostResponder implements Runnable
{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public HostToHostResponder(Socket clientSocket)
    {
        try
        {
            this.socket = clientSocket;
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        Request request;
        ArrayList<Response> responseBatch;

        while (true)
            try
            {
                request = (Request) input.readObject();
                if (request == null)
                    tryCloseSocket();
                else
                {
                    responseBatch=handleRequest(request);
                    for(Response response:responseBatch)
                        output.writeObject(response);
                    output.flush();
                }
            }
            catch (IOException | ClassNotFoundException e)
            {
                tryCloseSocket();
                return;
            }
    }

    private ArrayList<Response> handleRequest(Request request)
    {
        ArrayList<Response> responseBatch = new ArrayList<>();
        HostToHostResponseBuilder responseBuilder = new HostToHostResponseBuilder();
        if(request instanceof FilePartRequest)
        {
            responseBatch.addAll(responseBuilder.buildFilePartResponse((FilePartRequest)request));
        }
        else
        {
            responseBatch.add(new Response());
        }

        return responseBatch;
    }

    private void tryCloseSocket()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            //socket already closed
        }
    }


}
