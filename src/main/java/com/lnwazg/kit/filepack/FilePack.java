package com.lnwazg.kit.filepack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;

import com.lnwazg.kit.filepack.callback.FileCallback;
import com.lnwazg.kit.filepack.callback.impl.StringFileCallback;
import com.lnwazg.kit.io.StreamUtils;
import com.lnwazg.kit.log.Logs;

/**
 * 文件打包器与解码器<br>
 * 常用于游戏打包领域，当然也可以完美支持一些小型资源的的快速打包解析工作
 * @author nan.li
 * @version 2018年10月17日
 */
public class FilePack
{
    public static void main(String[] args)
    {
        String file1 = "D:/1.txt";
        String file2 = "D:/2.txt";
        String[] sourceFileNames = {file1, file2};
        File targetFile = pack(sourceFileNames, "D:/target.fp");
        readPack(targetFile, new StringFileCallback(CharEncoding.UTF_8)
        {
            @Override
            public void handle(String str)
            {
                System.out.println("读取到的字符串为:" + str);
            }
        });
    }
    
    /**
     * 读包并使用
     * @author nan.li
     * @param targetFile
     * @param fileCallback
     */
    private static void readPack(File targetFile, FileCallback fileCallback)
    {
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(targetFile);
            dataInputStream = new DataInputStream(fileInputStream);
            Logs.i("开始读取数据包文件:" + targetFile.getCanonicalPath());
            short memberSize = dataInputStream.readShort();
            int[] fileSizeArray = new int[memberSize];
            Logs.i("包成员文件数量：" + memberSize);
            for (int i = 0; i < memberSize; i++)
            {
                fileSizeArray[i] = dataInputStream.readInt();
            }
            for (int i = 0; i < fileSizeArray.length; i++)
            {
                byte[] contentBytes = new byte[fileSizeArray[i]];
                //一次性读入
                dataInputStream.read(contentBytes);
                if (fileCallback != null)
                {
                    fileCallback.call(contentBytes);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            StreamUtils.close(dataInputStream, fileInputStream);
        }
    }
    
    /**
     * 文件打包器，将一系列文件打包成一个单独的文件
     * @author nan.li
     * @param sourceFileNames
     * @param string
     * @return
     */
    private static File pack(String[] sourceFileNames, String targetFileName)
    {
        //文件格式：
        //short：文件数
        //Integer: size
        //具体的字节数组
        File targetFile = new File(targetFileName);
        FileOutputStream fileOutputStream = null;
        DataOutputStream dataOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(targetFile);
            dataOutputStream = new DataOutputStream(fileOutputStream);
            dataOutputStream.writeShort(sourceFileNames.length);
            for (int i = 0; i < sourceFileNames.length; i++)
            {
                dataOutputStream.writeInt((int)(new File(sourceFileNames[i]).length()));
            }
            for (int i = 0; i < sourceFileNames.length; i++)
            {
                dataOutputStream.write(IOUtils.toByteArray(new FileInputStream(sourceFileNames[i])));
            }
            Logs.i("Pack OK!");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            StreamUtils.close(dataOutputStream, fileOutputStream);
        }
        return targetFile;
    }
}
