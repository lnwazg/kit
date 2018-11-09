package com.lnwazg.kit.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lnwazg.kit.io.StreamUtils;

/**
 * gzip字节压缩以及解压缩工具类
 * @author nan.li
 * @version 2018年9月13日
 */
public class GzipBytesUtils
{
    private static final Log logger = LogFactory.getLog(GzipBytesUtils.class);
    
    /** 
     * 解压缩gzip字节数组
     * @param gzipBytes
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static byte[] unzip(byte[] gzipBytes)
    {
        byte[] result = null;
        ByteArrayInputStream bai = null;
        BufferedInputStream bi = null;
        GzipCompressorInputStream zis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            bai = new ByteArrayInputStream(gzipBytes);
            bi = new BufferedInputStream(bai);
            zis = new GzipCompressorInputStream(bi);
            bos = new ByteArrayOutputStream();
            IOUtils.copy(zis, bos);
            bos.close();
            result = bos.toByteArray();
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        finally
        {
            StreamUtils.close(zis, bi, bai);
        }
        return result;
    }
    
    /** 
     * 压缩字节数组
     * @param bytes
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static byte[] zip(byte[] bytes)
    {
        byte[] result = null;
        BufferedOutputStream bo = null;
        GzipCompressorOutputStream zos = null;
        ByteArrayOutputStream bos = null;
        try
        {
            bos = new ByteArrayOutputStream();
            bo = new BufferedOutputStream(bos);
            zos = new GzipCompressorOutputStream(bo);
            IOUtils.write(bytes, zos);
            zos.close();
            result = bos.toByteArray();
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        finally
        {
            StreamUtils.close(bo, bos);
        }
        return result;
    }
}
