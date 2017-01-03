package tests;

import messages.FileListUploadRequest;
import messages.Request;
import sockets.HostDispatcher;
import views.MainWindowController;

public class TestHost2
{
    public static void main(String[] args)
    {

        HostDispatcher dispatcherHost= new HostDispatcher(new MainWindowController());
        dispatcherHost.connectToServer("localhost",21370);
        Request request= new FileListUploadRequest();
        dispatcherHost.sendRequestToServer(request);
    }
}
