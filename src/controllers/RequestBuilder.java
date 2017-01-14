package controllers;

import java.util.ArrayList;

import messages.FilePartRequest;
import model.FileInstance;

public class RequestBuilder
{
    public static ArrayList<FilePartRequest> buildPartFileRequest(FileInstance fileInstance, int partCount)
    {
        ArrayList<FilePartRequest> partFileRequests= new ArrayList<>();
        long fileSize= fileInstance.getSize();
        long partSize= fileSize/partCount;
        long startByte=0;
        
        for(int i=0; i<partCount; i++)
        {
            FilePartRequest request;
            if(i!=partCount-1)
            {
                request = new FilePartRequest(fileInstance, startByte, partSize);
            }
            else
            {
                request = new FilePartRequest(fileInstance, startByte, fileInstance.getSize()-startByte);
            }
            startByte+=partSize;
            partFileRequests.add(request);
        }
        return partFileRequests;
    }
    
}
