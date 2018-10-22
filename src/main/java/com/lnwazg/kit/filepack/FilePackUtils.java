package com.lnwazg.kit.filepack;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharEncoding;

import com.lnwazg.kit.security.PasswordKit;

/**
 * 工具类
 * @author nan.li
 * @version 2018年10月22日
 */
public class FilePackUtils
{
    public static final String ENCODING = CharEncoding.UTF_8;
    
    public static final String AES_KEY = PasswordKit.getAesKey("MyDaddyIsSuperMan!!!");
    
    /**
     * 文本类型的扩展名
     */
    static String[] txtTypes = {"txt", "properties"};
    
    /**
     * 手动注册的更多文本类型的扩展名
     */
    static List<String> moreTxtTypes = new ArrayList<String>();
    
    /**
     * 是否文本类型
     * @author nan.li
     * @param fileNameExtension
     * @return
     */
    public static boolean isTxtType(String fileNameExtension)
    {
        return ArrayUtils.contains(txtTypes, fileNameExtension) || moreTxtTypes.contains(fileNameExtension);
    }
    
    /**
     * 向系统中注册更多的文本类型
     * @author nan.li
     * @param fileExtension
     */
    public static void registerMoreTxtTypes(String... fileExtension)
    {
        for (String extension : fileExtension)
        {
            moreTxtTypes.add(extension);
        }
    }
}
