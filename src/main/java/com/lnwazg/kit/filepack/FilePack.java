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
import com.lnwazg.kit.datastructure.Pair;
import com.lnwazg.kit.filepack.callback.FileCallback;
import com.lnwazg.kit.filepack.callback.impl.ExtractFileCallback;
import com.lnwazg.kit.filepack.callback.impl.SimpleFileCallback;
import com.lnwazg.kit.gson.GsonKit;
import com.lnwazg.kit.io.StreamUtils;
import com.lnwazg.kit.log.Logs;
import com.lnwazg.kit.reflect.TypeReference;
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
        
        String baseDir = "D:/";
        String[] sourceFileNames = {"1.txt", "2.txt", "3.txt", "opt/log/test.log"};
        
        //打包类型：
        for (PackType packtype : PackType.values())
        {
            System.out.println("\nbegin to test packtype: " + packtype);
            File targetFile = pack(baseDir, sourceFileNames, "D:/fp" + packtype.getType() + "." + FilePackUtils.FILE_TYPE_STRING.toLowerCase(), packtype);
            readPack(targetFile, new SimpleFileCallback()
            {
                @Override
                public void handle(FileInfo fileInfo, String fileContent)
                {
                    System.out.println("文件名：" + fileInfo.getFileName() + " 读取到的文件内容为:" + fileContent);
                }
            });
            readPack(targetFile, new ExtractFileCallback("D:/extract" + packtype.getType() + "/"));
        }
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
            Logs.i("Begin to read pack file: " + targetFile.getCanonicalPath());
            
            //read file type String
            char[] chars = FilePackUtils.FILE_TYPE_STRING.toCharArray();
            for (int i = 0; i < chars.length; i++)
            {
                byte c = dataInputStream.readByte();
                if (c != chars[i])
                {
                    Logs.e("readPack() meet invalid file type!");
                    return;
                }
            }
            
            //读取文件类型的字节信息
            byte packTypeByte = dataInputStream.readByte();
            short memberCount;
            PackType packType = PackType.of(packTypeByte);
            //文件类型匹配并处理
            switch (packType)
            {
                case NumByte:
                    memberCount = dataInputStream.readShort();
                    Logs.i("包成员文件数量：" + memberCount);
                    for (int i = 0; i < memberCount; i++)
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
                case NumNameCompressEncryptByte:
                    memberCount = dataInputStream.readShort();
                    Logs.i("包成员文件数量：" + memberCount);
                    for (int i = 0; i < memberCount; i++)
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
                        else if (packType == PackType.NumNameCompressEncryptByte)
                        {
                            dataFileBytes = SecurityUtils.aesDecode(dataFileBytes, FilePackUtils.AES_KEY);
                            dataFileBytes = GzipBytesUtils.unzip(dataFileBytes);
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
                case IntJsonByte:
                case IntJsonCompressByte:
                case IntJsonCompressEncryptByte:
                    int jsonBytesSize, dataFileAllBytesSize;
                    byte[] jsonBytes;
                    byte[] dataFileAllBytes = null;
                    jsonBytesSize = dataInputStream.readInt();
                    jsonBytes = new byte[jsonBytesSize];
                    dataInputStream.read(jsonBytes);
                    jsonBytes = GzipBytesUtils.unzip(jsonBytes);//解压缩
                    String json = new String(jsonBytes, FilePackUtils.ENCODING);
                    List<PackUnit> packUnits = GsonKit.parseString2Object(json, new TypeReference<List<PackUnit>>()
                    {
                    }.getType());
                    
                    dataFileAllBytesSize = dataInputStream.readInt();
                    dataFileAllBytes = new byte[dataFileAllBytesSize];
                    dataInputStream.read(dataFileAllBytes);
                    if (packType == PackType.IntJsonCompressByte)
                    {
                        dataFileAllBytes = GzipBytesUtils.unzip(dataFileAllBytes);
                    }
                    else if (packType == PackType.IntJsonCompressEncryptByte)
                    {
                        dataFileAllBytes = SecurityUtils.aesDecode(dataFileAllBytes, FilePackUtils.AES_KEY);
                        dataFileAllBytes = GzipBytesUtils.unzip(dataFileAllBytes);
                    }
                    //here we get all the real bytes
                    
                    int readIndex = 0;
                    Pair<byte[], Integer> dataInfoPair = new Pair<>();
                    dataInfoPair.setLeft(dataFileAllBytes);
                    dataInfoPair.setRight(readIndex);
                    
                    for (int i = 0; i < packUnits.size(); i++)
                    {
                        PackUnit packUnit = packUnits.get(i);
                        //file name size
                        int n = packUnit.getN();
                        
                        byte[] fileNameBytes = resolveBytes(dataInfoPair, n);
                        
                        //data size
                        int d = packUnit.getD();
                        byte[] dataFileBytes = resolveBytes(dataInfoPair, d);
                        
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
    public static File pack(String baseDir, String[] sourceFileNames, String targetFileName)
    {
        return pack(baseDir, sourceFileNames, targetFileName, PackType.IntJsonCompressEncryptByte);
    }
    
    /**
     * 文件打包器，将一系列文件打包成一个单独的文件
     * @author nan.li
     * @param sourceFileNames
     * @param string
     * @return
     */
    public static File pack(String baseDir, String[] sourceFileNames, String targetFileName, PackType packType)
    {
        File targetFile = new File(targetFileName);
        FileOutputStream fileOutputStream = null;
        DataOutputStream dataOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(targetFile);
            dataOutputStream = new DataOutputStream(fileOutputStream);
            
            //write file type String
            dataOutputStream.writeBytes(FilePackUtils.FILE_TYPE_STRING);
            
            //先输出一份文件类型的字节数据
            dataOutputStream.writeByte(packType.getType());
            
            //根据具体的打包类型，进行有差别的打包操作
            switch (packType)
            {
                case NumByte:
                    //文件格式：
                    //short：文件数
                    //Integer: size
                    dataOutputStream.writeShort(sourceFileNames.length);//最高支持2^16=65535个文件
                    for (int i = 0; i < sourceFileNames.length; i++)
                    {
                        File dataFile = new File(baseDir, sourceFileNames[i]);
                        FileInputStream fileInputStream = new FileInputStream(dataFile);
                        byte[] dataFileBytes = IOUtils.toByteArray(fileInputStream);
                        StreamUtils.close(fileInputStream);
                        
                        dataOutputStream.writeInt(dataFileBytes.length);
                        dataOutputStream.write(dataFileBytes);
                    }
                    break;
                case NumNameByte:
                case NumNameCompressByte:
                case NumNameEncryptByte:
                case NumNameCompressEncryptByte:
                    //文件格式：
                    //short：文件数
                    //Integer: size（文件数*2，一份写入文件名的长度，还有一份写入文件大小）
                    dataOutputStream.writeShort(sourceFileNames.length);//最高支持2^16=65535个文件
                    for (int i = 0; i < sourceFileNames.length; i++)
                    {
                        File dataFile = new File(baseDir, sourceFileNames[i]);
                        byte[] fileNameBytes = sourceFileNames[i].getBytes(FilePackUtils.ENCODING);
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
                        else if (packType == PackType.NumNameCompressEncryptByte)
                        {
                            dataFileBytes = GzipBytesUtils.zip(dataFileBytes);
                            dataFileBytes = SecurityUtils.aesEncode(dataFileBytes, FilePackUtils.AES_KEY);
                        }
                        dataOutputStream.writeInt(dataFileBytes.length);
                        dataOutputStream.write(dataFileBytes);
                    }
                    break;
                case IntJsonByte:
                case IntJsonCompressByte:
                case IntJsonCompressEncryptByte:
                    int jsonBytesSize, dataFileAllBytesSize;
                    byte[] jsonBytes;
                    byte[] dataFileAllBytes = null;
                    
                    List<PackUnit> packUnits = new ArrayList<>();
                    for (int i = 0; i < sourceFileNames.length; i++)
                    {
                        PackUnit packUnit = new PackUnit();
                        
                        File dataFile = new File(baseDir, sourceFileNames[i]);
                        byte[] fileNameBytes = sourceFileNames[i].getBytes(FilePackUtils.ENCODING);
                        
                        dataFileAllBytes = appendBytes(dataFileAllBytes, fileNameBytes);
                        
                        FileInputStream fileInputStream = new FileInputStream(dataFile);
                        byte[] dataFileBytes = IOUtils.toByteArray(fileInputStream);
                        StreamUtils.close(fileInputStream);
                        
                        dataFileAllBytes = appendBytes(dataFileAllBytes, dataFileBytes);
                        
                        //record them and combine them
                        packUnit.setN(fileNameBytes.length).setD(dataFileBytes.length);
                        packUnits.add(packUnit);
                    }
                    //serialize to String
                    
                    String json = GsonKit.parseObject2String(packUnits);
                    jsonBytes = json.getBytes(FilePackUtils.ENCODING);
                    jsonBytes = GzipBytesUtils.zip(jsonBytes);//做一次gzip压缩，避免明文化
                    jsonBytesSize = jsonBytes.length;
                    
                    if (packType == PackType.IntJsonCompressByte)
                    {
                        dataFileAllBytes = GzipBytesUtils.zip(dataFileAllBytes);
                    }
                    else if (packType == PackType.IntJsonCompressEncryptByte)
                    {
                        dataFileAllBytes = GzipBytesUtils.zip(dataFileAllBytes);
                        dataFileAllBytes = SecurityUtils.aesEncode(dataFileAllBytes, FilePackUtils.AES_KEY);
                    }
                    
                    dataFileAllBytesSize = dataFileAllBytes.length;
                    
                    dataOutputStream.writeInt(jsonBytesSize);
                    dataOutputStream.write(jsonBytes);
                    dataOutputStream.writeInt(dataFileAllBytesSize);
                    dataOutputStream.write(dataFileAllBytes);
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
    
    /**
     * 字节追加
     * @author nan.li
     * @param base
     * @param appendBytes
     */
    private static byte[] appendBytes(byte[] base, byte[] appendBytes)
    {
        byte[] result = null;
        if (base == null)
        {
            result = appendBytes;
        }
        else
        {
            result = new byte[base.length + appendBytes.length];
            for (int i = 0; i < base.length; i++)
            {
                result[i] = base[i];
            }
            for (int i = 0; i < appendBytes.length; i++)
            {
                result[i + base.length] = appendBytes[i];
            }
        }
        return result;
    }
    
    /**
     * 字节解析
     * @author nan.li
     * @param dataInfoPair
     * @param size
     * @return
     */
    private static byte[] resolveBytes(Pair<byte[], Integer> dataInfoPair, int size)
    {
        byte[] sourceBytes = dataInfoPair.getLeft();
        int readIndex = dataInfoPair.getRight();
        
        byte[] result = new byte[size];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = sourceBytes[i + readIndex];
        }
        readIndex += size;
        
        //move the readIndex to new position
        dataInfoPair.setRight(readIndex);
        return result;
    }
}
