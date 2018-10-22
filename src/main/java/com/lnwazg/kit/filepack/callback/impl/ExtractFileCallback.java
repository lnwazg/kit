package com.lnwazg.kit.filepack.callback.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.lnwazg.kit.filepack.FileInfo;
import com.lnwazg.kit.filepack.callback.FileCallback;
import com.lnwazg.kit.io.StreamUtils;
import com.lnwazg.kit.log.Logs;

/**
 * 解压缩
 * @author nan.li
 * @version 2018年10月22日
 */
public class ExtractFileCallback implements FileCallback
{
    /**
     * 加压缩出来的文件夹目录
     */
    private String baseDir;
    
    private List<String> uniqueFileNameList;
    
    public ExtractFileCallback(String baseDir, List<String> uniqueFileNameList)
    {
        this.baseDir = baseDir;
        this.uniqueFileNameList = uniqueFileNameList;
    }
    
    @Override
    public void call(FileInfo fileInfo)
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            fileInfo.build();
            String fileName = fileInfo.getFileName();
            
            if (StringUtils.isNotEmpty(fileName))
            {
                fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.length());
            }
            else
            {
                fileName = fileInfo.getExtractFileIndex() + "";
            }
            
            //避免文件名重复导致的解压文件相互覆盖的问题
            if (uniqueFileNameList.contains(fileName))
            {
                fileName = fileName + "_文件名重复";
            }
            uniqueFileNameList.add(fileName);
            
            File extractFile = new File(baseDir, fileName);
            extractFile.getParentFile().mkdirs();
            
            Logs.i("解压文件到路径：" + extractFile.getCanonicalPath());
            fileOutputStream = new FileOutputStream(extractFile);
            IOUtils.write(fileInfo.getFileContentBytes(), fileOutputStream);
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
            StreamUtils.close(fileOutputStream);
        }
    }
    
}
