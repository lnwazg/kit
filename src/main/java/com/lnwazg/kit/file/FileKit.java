package com.lnwazg.kit.file;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.SystemUtils;

import com.lnwazg.kit.log.Logs;
import com.lnwazg.kit.security.SecurityUtils;

/**
 * 文件工具
 * @author Administrator
 * @version 2016年4月23日
 */
public class FileKit
{
    /**
     * Mac下面，直接用相对路径，反而可以用起来！
     * 这是一种简便易行的方法！
     */
    private static final String CONFIG_BASEPATH_MAC = "JAVA_APP/";
    
    /**
     * 直接用相对路径，反而可以用起来！
     */
    private static final String CONFIG_BASEPATH_LINUX = "JAVA_APP/";
    
    public static void main(String[] args)
    {
        //去除重复下载项
        //        String dir = "I:\\TDDOWNLOAD";
        String dir = "O:\\tumblr";
        FileKit.trimFolderFilesToUnique(dir);
    }
    
    /**
     * 检测某个目录是否已经存在，或者可写成功
     * @author Administrator
     * @param dir
     * @return
     */
    public static boolean testCanWriteDirOrExists(String dir)
    {
        File dirFile = new File(dir);
        return dirFile.exists() || dirFile.mkdirs();
    }
    
    /**
     * 获取我的配置文件的基准目录<br>
     * 用此法获得的基准目录，即使不是管理员登录，也可以让应用顺利执行！
     * @author Administrator
     * @return
     */
    public static String getMyConfigBasePathForWindows()
    {
        String ret = "C:/Windows/LNWAZG/";
        //尝试是否有权限写入文件夹
        if (!FileKit.testCanWriteDirOrExists(ret))
        {
            ret = "JAVA_APP/";//直接采用相对目录
        }
        if (!FileKit.testCanWriteDirOrExists(ret))
        {
            ret = "C:/Program Files/LNWAZG";
        }
        if (!FileKit.testCanWriteDirOrExists(ret))
        {
            ret = "D:/Program Files/LNWAZG";
        }
        if (!FileKit.testCanWriteDirOrExists(ret))
        {
            ret = "C:/JAVA_APPS/";
        }
        if (!FileKit.testCanWriteDirOrExists(ret))
        {
            ret = "D:/JAVA_APPS/";
        }
        if (!FileKit.testCanWriteDirOrExists(ret))
        {
            ret = "C:/LNWAZG/";
        }
        if (!FileKit.testCanWriteDirOrExists(ret))
        {
            ret = "D:/LNWAZG/";
        }
        return ret;
    }
    
    /**
     * 获得全平台的基础路径<br>
     * 真正的跨平台！
     * @author Administrator
     * @return
     */
    public static String getConfigBasePathForAll()
    {
        String ret = "";
        //根据操作系统决定真正的配置路径
        if (SystemUtils.IS_OS_WINDOWS)
        {
            ret = FileKit.getMyConfigBasePathForWindows();
        }
        else if (SystemUtils.IS_OS_LINUX)
        {
            ret = CONFIG_BASEPATH_LINUX;
        }
        else if (SystemUtils.IS_OS_MAC)
        {
            ret = CONFIG_BASEPATH_MAC;
        }
        else
        {
            //其他情况下，都是类Unix的系统，因此均采用Linux的路径设置
            ret = CONFIG_BASEPATH_LINUX;
        }
        return ret;
    }
    
    /**
     * 文件夹内文件除重<br>
     * 去除文件名重复的文件，例如xxx(1).mp4,xxx(2).avi等等<br>
     * 去除checkSum值相同的文件，这些文件虽然文件名不同，但是内容完全一致，因此需要被删除掉
     * @author nan.li
     * @param dir
     */
    public static void trimFolderFilesToUnique(String dir)
    {
        File dirFile = new File(dir);
        trimFolderFilesToUnique(dirFile);
    }
    
    /**
     * 文件夹内文件除重<br>
     * 去除文件名重复的文件，例如xxx(1).mp4,xxx(2).avi等等<br>
     * 去除checkSum值相同的文件，这些文件虽然文件名不同，但是内容完全一致，因此需要被删除掉
     * @author nan.li
     * @param dir
     */
    public static void trimFolderFilesToUnique(File dirFile)
    {
        Logs.i("begin to trimToUnique...");
        if (dirFile.exists())
        {
            File[] files = dirFile.listFiles();
            //本次扫描的文件的checkSum值汇总表
            Set<String> checksums = new HashSet<>();
            for (File file : files)
            {
                //                System.out.println(file.getName());
                
                //首先删除相同checksum的文件
                try
                {
                    String checkSum = SecurityUtils.getMD5Checksum(file);
                    if (!checksums.contains(checkSum))
                    {
                        //记录该checksum值
                        checksums.add(checkSum);
                    }
                    else
                    {
                        //已经包含相同的checksum值，因此不记录，而是直接删除该文件
                        Logs.i("已经存在了内容相同的文件，即将被删除！ fileName=" + file.getName() + " ,checksum=" + checkSum);
                        boolean deleted = file.delete();
                        Logs.i("是否成功删除=" + deleted);
                        continue;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
                //获取文件名
                String fileName = file.getName();
                //获取去扩展名后的文件名
                String baseName = FilenameUtils.getBaseName(fileName);
                //tumblr_ns8hdkYuHg1uc3479(1).mp4
                if (baseName.endsWith(")"))
                {
                    Logs.i("即将清除名称重复的文件！baseName=" + baseName);
                    boolean deleted = file.delete();
                    Logs.i("是否成功删除=" + deleted);
                }
            }
        }
        Logs.i("trimToUnique OK!");
    }
    
    /**
     * 从txt文件中读取格式化的数据
     * @author nan.li
     * @param fileUrl
     * @param columns
     * @param columnSplitter
     * @return
     */
    public static List<Map<String, Object>> readListMapFromTxt(String fileUrl, List<String> columns, String columnSplitter)
    {
        try
        {
            URL url = new URL(fileUrl);
            File file = new File(url.getFile());
            if (!file.exists())
            {
                Logs.e("file not exist! fileUrl=" + fileUrl);
                return null;
            }
            List<String> lines = FileUtils.readLines(file, CharEncoding.UTF_8);
            List<Map<String, Object>> list = new ArrayList<>();
            for (String line : lines)
            {
                line = line.trim();
                Map<String, Object> map = new HashMap<>();
                String[] columnValues = line.split(columnSplitter);
                for (int i = 0; i < columnValues.length; i++)
                {
                    map.put(columns.get(i), columnValues[i]);
                }
                list.add(map);
            }
            return list;
        }
        catch (MalformedURLException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
