package controllers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import messages.FilePartResponse;

public class HostServerResponseController
{

    public void servePartFileResponse(FilePartResponse filePartResponse,
            String directory)
    {

        String name = filePartResponse.getFileInstance().getName();
        long size = filePartResponse.getFileInstance().getSize();
        long startByte = filePartResponse.getStartByte();
        byte[] filePartData = filePartResponse.getFilePartData();

        byte[] file = new byte[Math.toIntExact(size)];

        try
        {
            RandomAccessFile randomAccessFile = new RandomAccessFile(
                    new File(directory + "/" + name), "r");
            randomAccessFile.write(file, Math.toIntExact(startByte),
                    filePartData.length);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
