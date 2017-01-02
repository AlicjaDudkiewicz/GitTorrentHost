package tests;

import host.DispatcherHost;
import host.MainWindowController;
import messages.FileListUploadRequest;
import messages.Request;

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
