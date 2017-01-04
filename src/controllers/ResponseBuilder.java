package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import messages.FilePartRequest;
import messages.FilePartResponse;
import model.FileInstance;

public class ResponseBuilder
{
    public ArrayList<FilePartResponse> buildFilePartResponse(
            FilePartRequest filePartRequest, String pathToDirectory)
    {
        ArrayList<FilePartResponse> filePartResponses = new ArrayList<>();
        FileInstance fileInstance = filePartRequest.getFileInstance();
        long partStartByte = filePartRequest.getPartStartByte();
        long partFileSize = filePartRequest.getPartFileSize();
        long distance = partFileSize;
        final int BYTE_ARRAY_SIZE = 4096;

        String fileName = fileInstance.getName();

        File fileToSend = new File(pathToDirectory + "/" + fileName);
        try
        {
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileToSend,
                    "w");
            while (distance >= 0)
            {

                FilePartResponse filePartResponse = new FilePartResponse();
                filePartResponse.setFileInstance(fileInstance);
                filePartResponse.setStartByte(partStartByte);
                if (distance >= BYTE_ARRAY_SIZE)
                {
                    byte[] filePartData = new byte[BYTE_ARRAY_SIZE];
                    try
                    {
                        randomAccessFile.read(filePartData,
                                Math.toIntExact(partStartByte),
                                BYTE_ARRAY_SIZE);
                        distance = distance - BYTE_ARRAY_SIZE;
                        partStartByte = partStartByte + BYTE_ARRAY_SIZE;
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    filePartResponse.setFilePartData(filePartData);
                } else
                {
                    byte[] filePartData = new byte[Math.toIntExact(distance)];
                    filePartResponse.setFilePartData(filePartData);
                }
                filePartResponses.add(filePartResponse);

            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return filePartResponses;
    }
}
