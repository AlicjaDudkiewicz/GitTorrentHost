package tests;

import java.io.File;
import java.util.ArrayList;

import controllers.ResponseBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messages.FilePartRequest;
import messages.FilePartResponse;
import model.FileInstance;


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
        
        //HostDispatcher dispatcherHost= new HostDispatcher(fl.getController());
        //dispatcherHost.connectToServer("localhost",21370);

        ResponseBuilder rb = new ResponseBuilder();

        ArrayList<FilePartResponse> fp = rb.buildFilePartResponse(new FilePartRequest(new FileInstance(new File("C:/s.txt")),1,4099),"");

        fp.clear();

    }
}
