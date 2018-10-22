package com.lnwazg.kit.filepack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.lnwazg.kit.compress.GzipBytesUtils;
import com.lnwazg.kit.filepack.callback.FileCallback;
import com.lnwazg.kit.filepack.callback.impl.ExtractFileCallback;
import com.lnwazg.kit.filepack.callback.impl.SimpleFileCallback;
import com.lnwazg.kit.io.StreamUtils;
import com.lnwazg.kit.log.Logs;
import com.lnwazg.kit.security.SecurityUtils;

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
        FilePackUtils.registerMoreTxtTypes("properties");
        String[] sourceFileNames = {"D:/1.txt", "D:/2.txt", "D:/secure.properties"};
        
        //打包类型：
        //        PackType packtype = PackType.NumByte;//文件数量-字节   551 
        //        PackType packtype = PackType.NumNameByte;//文件数量-文件名（支持文件夹内置）-字节   599
        PackType packtype = PackType.NumNameCompressByte;//文件数量-文件名（支持文件夹内置）-压缩字节  394 （推荐的类型）
        //                PackType packtype = PackType.NumNameEncryptByte;//文件数量-文件名（支持文件夹内置）-加密字节   623
        //        PackType packtype = PackType.NumNameEncryptCompressByte;//文件数量-文件名（支持文件夹内置）-加密压缩字节  687
        
        File targetFile = pack(sourceFileNames, "D:/target" + packtype.getType() + ".fp", packtype);
        
        readPack(targetFile, new SimpleFileCallback()
        {
            @Override
            public void handle(FileInfo fileInfo, String fileContent)
            {
                System.out.println("文件名：" + fileInfo.getFileName() + " 读取到的文件内容为:" + fileContent);
            }
        });
        
        List<String> uniqueFileNameList = new ArrayList<String>();
        readPack(targetFile, new ExtractFileCallback("D:/extract/", uniqueFileNameList));
    }
    
    /**
     * 读包并使用
     * @author nan.li
     * @param targetFile
     * @param fileCallback
     */
    public static void readPack(File targetFile, FileCallback fileCallback)
    {
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(targetFile);
            dataInputStream = new DataInputStream(fileInputStream);
            Logs.i("开始读取数据包文件:" + targetFile.getCanonicalPath());
            
            //读取文件类型的字节信息
            byte ptByte = dataInputStream.readByte();
            short memberSize = dataInputStream.readShort();
            Logs.i("包成员文件数量：" + memberSize);
            
            PackType packType = PackType.of(ptByte);
            //文件类型匹配并处理
            switch (packType)
            {
                case NumByte:
                    for (int i = 0; i < memberSize; i++)
                    {
                        int byteNum = dataInputStream.readInt();
                        byte[] contentBytes = new byte[byteNum];
                        //一次性读入
                        dataInputStream.read(contentBytes);
                        if (fileCallback != null)
                        {
                            fileCallback.call(new FileInfo().setExtractFileIndex(i).setFileContentBytes(contentBytes));
                        }
                    }
                    break;
                case NumNameByte:
                case NumNameCompressByte:
                case NumNameEncryptByte:
                case NumNameEncryptCompressByte:
                    for (int i = 0; i < memberSize; i++)
                    {
                        //文件名
                        int byteNum = dataInputStream.readInt();
                        byte[] fileNameBytes = new byte[byteNum];
                        //一次性读入
                        dataInputStream.read(fileNameBytes);
                        
                        //文件内容
                        byteNum = dataInputStream.readInt();
                        byte[] dataFileBytes = new byte[byteNum];
                        //一次性读入
                        dataInputStream.read(dataFileBytes);
                        
                        if (packType == PackType.NumNameCompressByte)
                        {
                            dataFileBytes = GzipBytesUtils.unzip(dataFileBytes);
                        }
                        else if (packType == PackType.NumNameEncryptByte)
                        {
                            dataFileBytes = SecurityUtils.aesDecode(dataFileBytes, FilePackUtils.AES_KEY);
                        }
                        else if (packType == PackType.NumNameEncryptCompressByte)
                        {
                            dataFileBytes = GzipBytesUtils.unzip(dataFileBytes);
                            dataFileBytes = SecurityUtils.aesDecode(dataFileBytes, FilePackUtils.AES_KEY);
                        }
                        if (fileCallback != null)
                        {
                            fileCallback.call(new FileInfo()
                                .setExtractFileIndex(i)
                                .setFileNameBytes(fileNameBytes)
                                .setFileContentBytes(dataFileBytes));
                        }
                    }
                    break;
                default:
                    Logs.e("Unsupported type!");
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
     * 文件打包器，将一系列文件打包成一个单独的文件，采用默认的文件压缩类型进行打包
     * @author nan.li
     * @param sourceFileNames
     * @param targetFileName
     * @return
     */
    public static File pack(String[] sourceFileNames, String targetFileName)
    {
        return pack(sourceFileNames, targetFileName, PackType.NumNameCompressByte);
    }
    
    /**
     * 文件打包器，将一系列文件打包成一个单独的文件
     * @author nan.li
     * @param sourceFileNames
     * @param string
     * @return
     */
    public static File pack(String[] sourceFileNames, String targetFileName, PackType packType)
    {
        File targetFile = new File(targetFileName);
        FileOutputStream fileOutputStream = null;
        DataOutputStream dataOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(targetFile);
            dataOutputStream = new DataOutputStream(fileOutputStream);
            
            //先输出一份文件类型的字节数据
            dataOutputStream.writeByte(packType.getType());
            dataOutputStream.writeShort(sourceFileNames.length);//最高支持2^16=65535个文件
            //根据具体的打包类型，进行有差别的打包操作
            switch (packType)
            {
                case NumByte:
                    //文件格式：
                    //short：文件数
                    //Integer: size
                    for (int i = 0; i < sourceFileNames.length; i++)
                    {
                        dataOutputStream.writeInt((int)(new File(sourceFileNames[i]).length()));
                        dataOutputStream.write(IOUtils.toByteArray(new FileInputStream(sourceFileNames[i])));
                    }
                    break;
                case NumNameByte:
                case NumNameCompressByte:
                case NumNameEncryptByte:
                case NumNameEncryptCompressByte:
                    //文件格式：
                    //short：文件数
                    //Integer: size（文件数*2，一份写入文件名的长度，还有一份写入文件大小）
                    for (int i = 0; i < sourceFileNames.length; i++)
                    {
                        File dataFile = new File(sourceFileNames[i]);
                        String fullPath = dataFile.getCanonicalPath();
                        byte[] fileNameBytes = fullPath.getBytes(FilePackUtils.ENCODING);
                        dataOutputStream.writeInt(fileNameBytes.length);
                        dataOutputStream.write(fileNameBytes);
                        FileInputStream fileInputStream = new FileInputStream(dataFile);
                        byte[] dataFileBytes = IOUtils.toByteArray(fileInputStream);
                        StreamUtils.close(fileInputStream);
                        if (packType == PackType.NumNameCompressByte)
                        {
                            dataFileBytes = GzipBytesUtils.zip(dataFileBytes);
                        }
                        else if (packType == PackType.NumNameEncryptByte)
                        {
                            dataFileBytes = SecurityUtils.aesEncode(dataFileBytes, FilePackUtils.AES_KEY);
                        }
                        else if (packType == PackType.NumNameEncryptCompressByte)
                        {
                            dataFileBytes = SecurityUtils.aesEncode(dataFileBytes, FilePackUtils.AES_KEY);
                            dataFileBytes = GzipBytesUtils.zip(dataFileBytes);
                        }
                        dataOutputStream.writeInt(dataFileBytes.length);
                        dataOutputStream.write(dataFileBytes);
                    }
                    break;
                default:
                    break;
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
