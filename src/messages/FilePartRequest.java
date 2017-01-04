package messages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import model.FileInstance;

public class FilePartRequest extends Request
{
    private FileInstance fileInstance;
    private long partStartByte;
    private long partFileSize;

    public FilePartRequest(FileInstance fileInstance, long partStartByte)
    {
        this.fileInstance = fileInstance;
        this.partStartByte = partStartByte;
    }

    public FileInstance getFileInstance()
    {
        return fileInstance;
    }

    public void setFileInstance(FileInstance fileInstance)
    {
        this.fileInstance = fileInstance;
    }

    public long getPartStartByte()
    {
        return partStartByte;
    }

    public void setPartStartByte(long partStartByte)
    {
        this.partStartByte = partStartByte;
    }

    public long getPartFileSize()
    {
        return partFileSize;
    }

    public void setPartFileSize(long partFileSize)
    {
        this.partFileSize = partFileSize;
    }

}
