package controllers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import messages.FilePartResponse;
import services.ConfigProvider;

public class HostResponseHandler
{
    public void handleFilePartResponse(FilePartResponse filePartResponse)
    {
        String name = filePartResponse.getFileInstance().getName();
        long startByte = filePartResponse.getStartByte();
        byte[] filePartData = filePartResponse.getFilePartData();

        byte[] file = filePartResponse.getFilePartData();
        File fileToSave = new File(ConfigProvider.getRootDirectory() + "/" + name);
        try
        {
            if(!fileToSave.exists())
                fileToSave.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileToSave, "rw");
            randomAccessFile.seek(startByte);
            randomAccessFile.write(file, 0, filePartData.length);
            randomAccessFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
