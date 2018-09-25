package com.lnwazg.kit.bytes;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 字节转十六进制工具
 * @author nan.li
 * @version 2018年3月28日
 */
public class ByteToHex
{
    /** 十六进制一个字节的最大长度 **/
    private static final int HEX_LENGTH = 2;
    
    /** 二进制一个字节的最大长度 **/
    private static final int BINARY_LENGTH = 8;
    
    /**
     * 将字节数组转为十六进制字符串
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src)
    {
        StringBuilder result = new StringBuilder("");
        if (src == null || src.length <= 0)
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < HEX_LENGTH)
            {
                //4位二进制等同于1位十六进制，所以一个字节十六进制的长度应该是小于等于2
                result.append(0);
            }
            result.append(hv);
        }
        return result.toString();
    }
    
    /**
     * 将十六进制字符串转为字节数组
     * @author nan.li
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString)
    {
        if (hexString == null || hexString.equals(""))
        {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++)
        {
            int pos = i * 2;
            d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    
    /**
     * 将字节数组转为二进制字符串
     * @author nan.li
     * @param src
     * @return
     */
    public static String bytesToBinaryString(byte[] src)
    {
        final String ZERO = "00000000";
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            String hv = Integer.toBinaryString(src[i]);
            if (hv.length() < BINARY_LENGTH)
            {//因为toBinaryString产生的字符串可能不足8个字符长度
                stringBuilder.append(ZERO.substring(hv.length()) + hv);
            }
            else if (hv.length() > BINARY_LENGTH)
            {//也可能是32个字符长度
                stringBuilder.append(hv.substring(hv.length() - 8));
            }
        }
        return stringBuilder.toString();
    }
    
    /**
     * 字符转字节
     * @author nan.li
     * @param c
     * @return
     */
    private static byte charToByte(char c)
    {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
    
    public static void main(String[] args)
        throws UnsupportedEncodingException
    {
        byte[] bytes = "我".getBytes("UTF-8");
        System.out.println(bytesToBinaryString(bytes));
        System.out.println(bytesToHexString(bytes));
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = charset.encode("我"); // 字节缓冲区
        System.out.println(ByteToHex.bytesToBinaryString(byteBuffer.array()));
        System.out.println(ByteToHex.bytesToHexString(byteBuffer.array()));
        System.out.println(new String(ByteToHex.hexStringToBytes("BDE1"), "GB18030"));
    }
}
