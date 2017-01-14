package controllers;

import java.util.ArrayList;

import messages.FilesListResponse;
import messages.HostListResponse;
import messages.Response;
import model.FileInstance;
import model.Host;
import services.FileListBuilder;
import tests.Logger;
import views.MainWindowController;

public class MainServerResponseHandler
{
    private MainWindowController viewController;

    public MainServerResponseHandler(MainWindowController mainWindowController)
    {
        this.viewController = mainWindowController;
    }

    public void serveResponse(Response response)
    {
        Logger.logMessage("Response "+response.getClass().getName() + " received");
        if (response instanceof FilesListResponse)
        {
            ArrayList<FileInstance> filesList = ((FilesListResponse) response)
                    .getFilesList();
            filesList.removeAll(FileListBuilder.getAvailableFilesList());
            viewController.refreshFilesList(filesList);
        }
        if (response instanceof HostListResponse)
        {
            ArrayList<Host> hostList = ((HostListResponse) response)
                    .getHostList();
            viewController.setHostList(hostList);
        }
        Logger.logMessage("Response "+response.getClass().getName() + " handled");
    }
}