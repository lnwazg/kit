package com.lnwazg.kit.filepack;

/**
 * 文件包类型
 * @author nan.li
 * @version 2018年10月22日
 */
public enum PackType
{
    /**
     * 文件数量-字节 （缺失文件名信息）
     */
    NumByte((byte)0x01),
    /**
     * 文件数量-文件名（支持文件夹内置）-字节（所有信息都有，中规中矩）
     */
    NumNameByte((byte)0x02),
    
    /**
     * 文件数量-文件名（支持文件夹内置）-压缩字节（所有信息都有，并且文件大小最小，适合性能优先并不需要太考虑安全性的场景，默认推荐的类型）
     */
    NumNameCompressByte((byte)0x03),
    
    /**
     * 文件数量-文件名（支持文件夹内置）-加密字节（所有信息都有，安全性增强，体积会比普通文件略大，注重安全时可以采用）
     */
    NumNameEncryptByte((byte)0x04),
    /**
     * 文件数量-文件名（支持文件夹内置）-加密压缩字节（所有信息都有，安全性增强，体积会比普通文件略大，对于大量注重安全的文件场景可以采用）
     */
    NumNameEncryptCompressByte((byte)0x05);
    
    PackType(byte type)
    {
        this.type = type;
    }
    
    private byte type;
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    /**
     * 根据字节信息查找对应的打包类型
     * @author nan.li
     * @param paramByte
     * @return
     */
    public static PackType of(byte paramByte)
    {
        if (paramByte == NumByte.getType())
        {
            return NumByte;
        }
        else if (paramByte == NumNameByte.getType())
        {
            return NumNameByte;
        }
        else if (paramByte == NumNameCompressByte.getType())
        {
            return NumNameCompressByte;
        }
        else if (paramByte == NumNameEncryptByte.getType())
        {
            return NumNameEncryptByte;
        }
        else if (paramByte == NumNameEncryptCompressByte.getType())
        {
            return NumNameEncryptCompressByte;
        }
        else
        {
            return null;
        }
    }
}
