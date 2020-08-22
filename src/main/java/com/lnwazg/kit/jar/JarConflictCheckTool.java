package com.lnwazg.kit.jar;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.ArrayUtils;

/**
 * Jar包的类冲突检测工具
 * @author nan.li
 * @version 2020年8月22日
 */
public class JarConflictCheckTool
{
    /*待检测的jar包目录*/
    public static final String CHECK_JARS_DIR = "D:\\IdeaProjects\\应用侧\\message\\build\\libs\\lib";
    
    /*要从结果中过滤掉的关键字的数组*/
    public static String[] RESULT_FILTER_KEYWORDS = {"lnwazg"};
    //    public static String[] RESULT_FILTER_KEYWORDS = {};
    
    @SuppressWarnings({"resource", "rawtypes"})
    public static void main(String[] args)
    {
        System.out.println("【使用帮助】Gradle->build->bootJar,打好fatJar，然后将fatJar解压，找到lib目录，将其配置到上面的CHECK_JARS_DIR。");
        System.out.println("【使用帮助】RESULT_FILTER_KEYWORDS是你要过滤显示的关键字数组。如果该数组为空，将全量显示所有冲突(全量的结果可能非常庞大！)。");
        System.out.println("【开始运行】开始jar包冲突检测，待检测的lib目录为：" + CHECK_JARS_DIR);
        File libDir = new File(CHECK_JARS_DIR);
        Map<String, Set<String>> conflictClassJarNamesSetMap = new HashMap<>();
        if ((libDir != null) && (libDir.exists()) && (libDir.isDirectory()))
        {
            File[] jarFiles = libDir.listFiles();
            for (File file : jarFiles)
            {
                String fileName = file.getName();
                if ((!file.isFile()) || (!fileName.endsWith(".jar")))
                {
                    //只检测jar包冲突，非jar包的冲突一律忽略
                    continue;
                }
                try
                {
                    JarFile jarFile = new JarFile(file);
                    Enumeration jarEntryEnumeration = jarFile.entries();
                    while (jarEntryEnumeration.hasMoreElements())
                    {
                        JarEntry jarEntry = (JarEntry)jarEntryEnumeration.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class"))
                        {
                            //对class冲突进行检测
                            if (conflictClassJarNamesSetMap.containsKey(jarEntryName))
                            {
                                //发现了有冲突的类！
                                conflictClassJarNamesSetMap.get(jarEntryName).add(fileName);
                            }
                            else
                            {
                                Set<String> jarNamesSet = new HashSet<>();
                                jarNamesSet.add(fileName);
                                conflictClassJarNamesSetMap.put(jarEntryName, jarNamesSet);
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            
            Set<String> conflictClassNames = conflictClassJarNamesSetMap.keySet();
            if (ArrayUtils.isNotEmpty(RESULT_FILTER_KEYWORDS))
            {
                System.out.println("【开启过滤打印】将过滤显示含有以下关键字的结果：" + ArrayUtils.toString(RESULT_FILTER_KEYWORDS));
            }
            else
            {
                System.out.println("【开始全量打印】RESULT_FILTER_KEYWORDS为空，过滤打印已关闭，开启全量打印。");
            }
            System.out.println("存在重复类的Jar包有：");
            for (String conflictClassName : conflictClassNames)
            {
                if (conflictClassJarNamesSetMap.get(conflictClassName).size() <= 1)
                {
                    //只有一个的不算冲突
                    continue;
                }
                Set<String> jarNameSet = conflictClassJarNamesSetMap.get(conflictClassName);
                String repeatedJarNames = "";
                for (String jarName : jarNameSet)
                {
                    repeatedJarNames += (jarName + ", ");
                }
                repeatedJarNames = repeatedJarNames.substring(0, repeatedJarNames.length() - 2);
                
                if (ArrayUtils.isNotEmpty(RESULT_FILTER_KEYWORDS))
                {
                    if (filterKeywordsContains(conflictClassName))
                    {
                        System.out.println(repeatedJarNames + " \n\t有重复类： " + conflictClassName);
                    }
                }
                else
                {
                    System.out.println(repeatedJarNames + " \n\t有重复类： " + conflictClassName);
                }
            }
        }
        else
        {
            System.out.println("待检测的目录不存在，请修改CHECK_JARS_DIR！");
        }
    }
    
    /**
     * 该类是否在预配置好的关键字列表中
     */
    private static boolean filterKeywordsContains(String conflictClassName)
    {
        if (ArrayUtils.isEmpty(RESULT_FILTER_KEYWORDS))
        {
            return false;
        }
        for (String keyword : RESULT_FILTER_KEYWORDS)
        {
            if (conflictClassName.indexOf(keyword) != -1)
            {
                return true;
            }
        }
        return false;
    }
}