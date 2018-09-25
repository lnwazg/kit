package com.lnwazg.kit.bytes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.lnwazg.kit.compress.GzipBytesUtils;
import com.lnwazg.kit.security.SecurityUtils;

/**
 * 字节码工具类，方便地在字节数组和String之间进行来回转换<br>
 * 特别适用于不方便传递字节数组的场景，以及只能用字符串进行数据传递的场景
 * @author nan.li
 * @version 2017年10月26日
 */
public final class ByteUtils
{
    public static void main(String[] args)
        throws FileNotFoundException, IOException
    {
        byte[] picBytes = IOUtils.toByteArray(new FileInputStream("c:\\js.png"));
        
        String result = toHexAscii(picBytes);
        FileUtils.writeStringToFile(new File("c:\\js.png.txt"), result, CharEncoding.UTF_8);
        
        result = toGzipHexAscii(picBytes);
        FileUtils.writeStringToFile(new File("c:\\js_gzip.png.txt"), result, CharEncoding.UTF_8);
        
        result = SecurityUtils.base64Encode(picBytes);
        FileUtils.writeStringToFile(new File("c:\\js_base64.png.txt"), result, CharEncoding.UTF_8);
        
        result = SecurityUtils.base64Encode(GzipBytesUtils.zip(picBytes));
        FileUtils.writeStringToFile(new File("c:\\js_gzip_base64.png.txt"), result, CharEncoding.UTF_8);
        
        System.out.println("OK!");
        
        picBytes = IOUtils.toByteArray(new FileInputStream("c:\\22.jpg"));
        result = parseBytes2String(picBytes);
        FileUtils.writeStringToFile(new File("c:\\22.jpg.txt"), result, CharEncoding.UTF_8);
        picBytes = parseString2Bytes(result);
        IOUtils.write(picBytes, new FileOutputStream("c:\\22_new.jpg"));
        System.out.println("OK!");
    }
    
    /**
     * 将字节数组转为String<br>
     * 内部实现：采用先gzip加密后base64编码的方式
     * @author nan.li
     * @param bytes
     * @return
     */
    public static String parseBytes2String(byte[] bytes)
    {
        return SecurityUtils.base64Encode(GzipBytesUtils.zip(bytes));
    }
    
    /**
     * 将String转为字节数组<br>
     * 内部实现：先base64解码，再gzip解密
     * @author nan.li
     * @param str
     * @return
     */
    public static byte[] parseString2Bytes(String str)
    {
        return GzipBytesUtils.unzip(SecurityUtils.base64DecodeToBytes(str));
    }
    
    /**
     * 压缩后将字节数组转为16进制ascii码
     * @author nan.li
     * @param bytes
     * @return
     */
    public static String toGzipHexAscii(byte[] bytes)
    {
        return toHexAscii(GzipBytesUtils.zip(bytes));
    }
    
    /**
     * 从16进制ascii码回转为字节数组并解压缩
     * @author nan.li
     * @param asciiStr
     * @return
     */
    public static byte[] fromGzipHexAscii(String asciiStr)
    {
        return GzipBytesUtils.unzip(fromHexAscii(asciiStr));
    }
    
    /**
     * 将字节数组转为16进制ascii码
     * @author nan.li
     * @param bytes
     * @return
     */
    public static String toHexAscii(byte[] bytes)
    {
        int len = bytes.length;
        StringWriter sw = new StringWriter(len * 2);
        for (int i = 0; i < len; ++i)
        {
            addHexAscii(bytes[i], sw);
        }
        return sw.toString();
    }
    
    /**
     * 从16进制ascii码回转为字节数组
     * @author nan.li
     * @param asciiStr
     * @return
     * @throws NumberFormatException
     */
    public static byte[] fromHexAscii(String asciiStr)
        throws NumberFormatException
    {
        try
        {
            int len = asciiStr.length();
            if ((len % 2) != 0)
            {
                throw new NumberFormatException("Hex ascii must be exactly two digits per byte.");
            }
            int out_len = len / 2;
            byte[] out = new byte[out_len];
            int i = 0;
            StringReader sr = new StringReader(asciiStr);
            while (i < out_len)
            {
                int val = (16 * fromHexDigit(sr.read())) + fromHexDigit(sr.read());
                out[i++] = (byte)val;
            }
            return out;
        }
        catch (IOException e)
        {
            throw new InternalError("IOException reading from StringReader?!?!");
        }
    }
    
    /**
     * 无符号的最大值255
     */
    public final static short UNSIGNED_MAX_VALUE = (Byte.MAX_VALUE * 2) + 1;
    
    /**
     * 将字节转为无符号的short
     * @author nan.li
     * @param b
     * @return
     */
    public static short toUnsigned(byte b)
    {
        return (short)(b < 0 ? (UNSIGNED_MAX_VALUE + 1) + b : b);
    }
    
    /**
     * 将字节转为16进制ascii码
     * @author nan.li
     * @param b
     * @return
     */
    public static String toHexAscii(byte b)
    {
        StringWriter sw = new StringWriter(2);
        addHexAscii(b, sw);
        return sw.toString();
    }
    
    /**
     * byte转string
     * @author nan.li
     * @param b
     * @param sw
     */
    static void addHexAscii(byte b, StringWriter sw)
    {
        short ub = toUnsigned(b);
        int h1 = ub / 16;
        int h2 = ub % 16;
        sw.write(toHexDigit(h1));
        sw.write(toHexDigit(h2));
    }
    
    private static int fromHexDigit(int c)
        throws NumberFormatException
    {
        if (c >= 0x30 && c < 0x3A)
            return c - 0x30;
        else if (c >= 0x41 && c < 0x47)
            return c - 0x37;
        else if (c >= 0x61 && c < 0x67)
            return c - 0x57;
        else
            throw new NumberFormatException('\'' + c + "' is not a valid hexadecimal digit.");
    }
    
    /* note: we do no arg. checking, because     */
    /* we only ever call this from addHexAscii() */
    /* above, and we are sure the args are okay  */
    private static char toHexDigit(int h)
    {
        char out;
        if (h <= 9)
            out = (char)(h + 0x30);
        else
            out = (char)(h + 0x37);
        //System.err.println(h + ": " + out);
        return out;
    }
    
    private ByteUtils()
    {
    }
}
