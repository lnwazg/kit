package com.lnwazg.kit.filepack.callback.impl;

import java.io.UnsupportedEncodingException;

import com.lnwazg.kit.filepack.callback.FileCallback;

public abstract class StringFileCallback implements FileCallback
{
    private String encoding;
    
    public StringFileCallback(String encoding)
    {
        this.encoding = encoding;
    }
    
    @Override
    public void call(byte[] contentBytes)
    {
        try
        {
            String str = new String(contentBytes, encoding);
            handle(str);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    public abstract void handle(String str);
}
