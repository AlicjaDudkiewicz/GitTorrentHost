package controllers;

import messages.PullFileRequest;
import messages.PushFileRequest;
import messages.Request;
import messages.Response;

public class RequestController
{
    public Response serveRequest(Request request)
    {
        if (request != null)
        {
            if (request instanceof PullFileRequest)
            {

            }
            if (request instanceof PushFileRequest)
            {

            }

        }
        return null;
    }
}
