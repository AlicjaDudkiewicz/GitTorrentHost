package services;
import java.io.File;
import java.util.ArrayList;

import model.FileInstance;


public class FilesListUploader
{
    private ArrayList<FileInstance> availableFilesList= new ArrayList<>();
    public void uploadFiles(String pathToDirectory)
    {
        File[] files = new File(pathToDirectory).listFiles();
        for(File file: files)
        {
            FileInstance fileInstance = new FileInstance(file);
            availableFilesList.add(fileInstance);
        }
    }

    public ArrayList<FileInstance> getAvailableFilesList(String path)
    {
        uploadFiles(path);
        return availableFilesList;
    }
}
