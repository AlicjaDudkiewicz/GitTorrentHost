package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import messages.FilePartRequest;
import messages.FilePartResponse;
import model.FileInstance;
import services.ConfigProvider;

public class HostToHostResponseBuilder
{
    public static ArrayList<FilePartResponse> buildFilePartResponse(FilePartRequest filePartRequest)
    {
        ArrayList<FilePartResponse> filePartResponses = new ArrayList<>();
        FileInstance fileInstance = filePartRequest.getFileInstance();
        long partStartByte = filePartRequest.getPartStartByte();
        long partFileSize = filePartRequest.getPartFileSize();
        long distanceToPartEnd = partFileSize;
        final int BYTE_ARRAY_SIZE = 524288;

        String fileName = fileInstance.getName();

        File fileToSend = new File(ConfigProvider.getRootDirectory() + "/" + fileName);

        try
        {
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileToSend, "r");

            while (distanceToPartEnd > 0)
            {
                FilePartResponse filePartResponse = new FilePartResponse(fileInstance,partStartByte);

                randomAccessFile.seek(partStartByte);
                if (distanceToPartEnd >= BYTE_ARRAY_SIZE)
                {
                    byte[] filePartData = new byte[BYTE_ARRAY_SIZE];
                    try
                    {
                        randomAccessFile.read(filePartData, 0, BYTE_ARRAY_SIZE);
                        distanceToPartEnd = distanceToPartEnd - BYTE_ARRAY_SIZE;
                        partStartByte = partStartByte + BYTE_ARRAY_SIZE;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    filePartResponse.setFilePartData(filePartData);
                }
                else
                {
                    byte[] filePartData = new byte[Math.toIntExact(distanceToPartEnd)];
                    randomAccessFile.read(filePartData, 0, Math.toIntExact(distanceToPartEnd));
                    distanceToPartEnd = 0;
                    filePartResponse.setFilePartData(filePartData);
                }
                filePartResponses.add(filePartResponse);

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return filePartResponses;
    }
}
