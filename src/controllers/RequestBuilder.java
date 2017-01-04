package controllers;

import java.util.ArrayList;

import messages.FilePartRequest;
import model.FileInstance;
import model.Host;

public class RequestBuilder
{
    public ArrayList<FilePartRequest> buildPartFileRequest(FileInstance fileInstance, ArrayList<Host> hostList)
    {
        ArrayList<FilePartRequest> partFileRequests= new ArrayList<>();
        int hostsAmount=hostList.size();
        long fileSize= fileInstance.getSize();
        long partSize= fileSize/hostsAmount;
        long startByte=0;
        
        for(int i=0; i<hostsAmount; i++)
        {   
            
            FilePartRequest request= new FilePartRequest(fileInstance,startByte);
            request.setPartStartByte(startByte);
            request.setPartFileSize(partSize);
            startByte=startByte+partSize;
            partFileRequests.add(request);
        }
        return partFileRequests;
    }
}
