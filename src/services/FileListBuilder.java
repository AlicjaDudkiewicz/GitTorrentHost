package services;
import java.io.File;
import java.util.ArrayList;

import model.FileInstance;


public class FileListBuilder
{
    public static ArrayList<FileInstance> getAvailableFilesList()
    {
        ArrayList<FileInstance> availableFilesList= new ArrayList<>();
        File[] files = new File(ConfigProvider.getRootDirectory()).listFiles();
        for(File file: files)
        {
            FileInstance fileInstance = new FileInstance(file);
            availableFilesList.add(fileInstance);
        }
        return availableFilesList;
    }
}
