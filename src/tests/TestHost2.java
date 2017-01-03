package tests;

import messages.FileListUploadRequest;
import messages.Request;
import sockets.DispatcherHost;
import views.MainWindowController;

public class TestHost2
{
    public static void main(String[] args)
    {

        DispatcherHost dispatcherHost= new DispatcherHost(new MainWindowController());
        dispatcherHost.connectToServer("localhost",21370);
        Request request= new FileListUploadRequest();
        dispatcherHost.sendRequest(request);
    }
}
