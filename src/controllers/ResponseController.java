package controllers;

import java.util.ArrayList;

import host.MainWindowController;
import messages.FilesListResponse;
import messages.HostListResponse;
import messages.Response;
import model.FileInstance;
import model.Host;

public class ResponseController
{
    private MainWindowController viewController;

    public void serveResponse(Response response)
    {
        // MainWindowController controller = null;
        if (response instanceof FilesListResponse)
        {
            ArrayList<FileInstance> filesList = ((FilesListResponse) response)
                    .getFilesList();
            viewController.refreshFilesList(filesList);
        }
        if (response instanceof HostListResponse)
        {
            ArrayList<Host> hostList = ((HostListResponse) response)
                    .getHostList();
            viewController.setHostList();
        }
    }
}