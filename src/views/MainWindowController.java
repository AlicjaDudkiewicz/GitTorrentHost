package views;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import controllers.RequestBuilder;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import messages.FilePartRequest;
import messages.FilesListRequest;
import messages.HostListRequest;
import model.FileInstance;
import model.Host;
import sockets.HostDispatcher;
import tests.HttpServer;

public class MainWindowController
{
    private HostDispatcher dispatcher;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Host> hostList;

    @FXML
    private TableView<FileInstance> fileTable;

    @FXML
    private Label statusLabel;

    @FXML
    private TableColumn<FileInstance, String> md5Column;

    @FXML
    private TableColumn<FileInstance, String> filenameColumn;

    @FXML
    private TableColumn<FileInstance, String> filesizeColumn;

    @FXML
    void closeMenuItemClicked(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML
    void aboutMenuItemClicked(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("GitTorrent by Alicja Dudkiewicz");
        alert.setHeaderText(null);
        alert.setTitle("About");
        alert.show();
    }

    @FXML
    void downloadButtonClicked(ActionEvent event)
    {
        FileInstance fileInstance = fileTable.getSelectionModel().getSelectedItem();
        if(fileInstance!=null)
        {
            ObservableList<Host> hosts = hostList.getItems();
            if(hosts.size()>0)
            {
                ArrayList<FilePartRequest> filePartRequests = RequestBuilder.buildPartFileRequest(fileInstance, hosts.size());
                for (int i = 0; i < hosts.size(); i++)
                {
                    Host host = hosts.get(i);
                    dispatcher.sendRequestToHost(host.getIp(), host.getPort(), filePartRequests.get(i));
                }
            }
        }
    }

    @FXML
    void uploadButtonClicked(ActionEvent event)
    {

    }

    public void updateConnectionStatus(String status)
    {
        statusLabel.setText("Status połączenia: "+status);
    }

    @FXML
    void openLogs(ActionEvent event) {
        if(Desktop.isDesktopSupported())
        {
            try
            {
                Desktop.getDesktop().browse(new URI("http://localhost:"+ HttpServer.getInstance().getServerPort()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void refreshButtonClicked(ActionEvent event)
    {
        dispatcher.sendRequestToServer(new FilesListRequest());
    }

    public void refreshFilesList(ArrayList<FileInstance> fileList)
    {
        fileTable.getItems().clear();
        for (FileInstance f : fileList)
        {
            fileTable.getItems().add(f);
        }
    }

    @FXML
    void initialize()
    {
        assert hostList != null : "fx:id=\"hostList\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert fileTable != null : "fx:id=\"fileTable\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'mainWindow.fxml'.";

        md5Column.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getMd5()));
        filenameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));
        filesizeColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSize() + ""));

        fileTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                HostListRequest hostListRequest = new HostListRequest();
                hostListRequest.setRequiredFile(newValue);
                this.dispatcher.sendRequestToServer(hostListRequest);
            }
        }));

        this.updateConnectionStatus("OFFLINE");

        Timer tableRefresher = new Timer();
        tableRefresher.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                dispatcher.sendRequestToServer(new FilesListRequest());
            }
        },1000,10000);
    }


    public void setHostList(ArrayList<Host> hostList)
    {
        this.hostList.getItems().clear();
        for (Host host : hostList)
        {
            this.hostList.getItems().add(host);
        }

    }

    public void setDispatcher(HostDispatcher dispatcherHost)
    {
        this.dispatcher = dispatcherHost;
    }

    public void showConnectErrorWindow()
    {
        Alert connectionAlert = new Alert(Alert.AlertType.ERROR);
        connectionAlert.setHeaderText(null);
        connectionAlert.setContentText("Błąd połączenia do serwera");
        Button okButton = (Button)connectionAlert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Ponów próbę");
        okButton.setOnAction(event -> dispatcher.connectToServer());
        connectionAlert.showAndWait();
    }
}
