package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sockets.HostDispatcher;


public class TestHost extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fl = new FXMLLoader(getClass().getResource("../views/mainWindow.fxml"));
        Parent root = fl.load();
        primaryStage.setTitle("GitTorrent");
        primaryStage.setScene(new Scene(root, 900, 550));
        primaryStage.show();
        
        HostDispatcher dispatcherHost= new HostDispatcher(fl.getController());
        dispatcherHost.connectToServer("localhost",21370);        
    }
}
