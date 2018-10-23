package com.lnwazg.kit.filepack.callback.impl;

import java.io.UnsupportedEncodingException;

import com.lnwazg.kit.filepack.FileInfo;
import com.lnwazg.kit.filepack.FilePackUtils;
import com.lnwazg.kit.filepack.callback.FileCallback;

public abstract class SimpleFileCallback implements FileCallback
{
    @Override
    public void call(FileInfo fileInfo)
    {
        try
        {
            fileInfo.build();
            if (FilePackUtils.isTxtType(fileInfo.getFileNameExtension()))
            {
                String fileContent = new String(fileInfo.getFileContentBytes(), FilePackUtils.ENCODING);
                handle(fileInfo, fileContent);
            }
            else
            {
                //二进制类型
                //只能打出文件名信息瞧瞧
                handle(fileInfo, "[binary bytes, register it if it's txt type]");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    public abstract void handle(FileInfo fileInfo, String fileContent);
}
