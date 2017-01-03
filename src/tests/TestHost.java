package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sockets.DispatcherHost;


public class TestHost extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fl = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = fl.load();
        primaryStage.setTitle("GitTorrent");
        primaryStage.setScene(new Scene(root, 900, 550));
        primaryStage.show();
        
        DispatcherHost dispatcherHost= new DispatcherHost(fl.getController());
        dispatcherHost.connectToServer("localhost",21370);        
        //Request request= new FileListUploadRequest();
        //dispatcherHost.sendRequest(request);

    }
}
