package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messages.FileListUploadRequest;
import messages.FilesListRequest;
import services.ConfigProvider;
import services.FileListBuilder;
import sockets.HostDispatcher;


public class TestHost extends Application
{
    public static void main(String[] args)
    {
        ConfigProvider.setDirectory(args[0]);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Logger.logMessage("Host started");
        FXMLLoader fl = new FXMLLoader(getClass().getResource("../views/mainWindow.fxml"));
        Parent root = fl.load();
        primaryStage.setTitle("GitTorrent");
        primaryStage.setScene(new Scene(root, 900, 550));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        HostDispatcher dispatcher = new HostDispatcher(fl.getController());
        dispatcher.connectToServer();
        dispatcher.sendRequestToServer(new FileListUploadRequest(FileListBuilder.getAvailableFilesList()));
        dispatcher.sendRequestToServer(new FilesListRequest());


    }
}
