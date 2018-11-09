package com.lnwazg.kit.filepack;

import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件信息<br>
 * 包括文件名、文件字节码信息等
 * @author nan.li
 * @version 2018年10月22日
 */
public class FileInfo
{
    /**
     * 文件名
     */
    String fileName;
    
    /**
     * 文件名扩展信息
     */
    private String fileNameExtension;
    
    /**
     * 文件名字节数组信息
     */
    byte[] fileNameBytes;
    
    /**
     * 文件内容字节数组信息
     */
    byte[] fileContentBytes;
    
    /**
     * 文件序号
     */
    private int extractFileIndex;
    
    public String getFileName()
    {
        return fileName;
    }
    
    public FileInfo setFileName(String fileName)
    {
        this.fileName = fileName;
        return this;
    }
    
    public byte[] getFileNameBytes()
    {
        return fileNameBytes;
    }
    
    public FileInfo setFileNameBytes(byte[] fileNameBytes)
    {
        this.fileNameBytes = fileNameBytes;
        return this;
    }
    
    public byte[] getFileContentBytes()
    {
        return fileContentBytes;
    }
    
    public FileInfo setFileContentBytes(byte[] fileContentBytes)
    {
        this.fileContentBytes = fileContentBytes;
        return this;
    }
    
    public String getFileNameExtension()
    {
        return fileNameExtension;
    }
    
    /**
     * 构建一次
     * @author nan.li
     * @return
     */
    public FileInfo build()
    {
        try
        {
            if (fileNameBytes != null)
            {
                //根据文件名数组信息构建出文件名信息
                this.fileName = new String(fileNameBytes, FilePackUtils.ENCODING);
                if (StringUtils.isNotEmpty(fileName))
                {
                    this.fileNameExtension = FilenameUtils.getExtension(fileName);
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public int getExtractFileIndex()
    {
        return extractFileIndex;
    }

    public FileInfo setExtractFileIndex(int extractFileIndex)
    {
        this.extractFileIndex = extractFileIndex;
        return this;
    }
    
}
