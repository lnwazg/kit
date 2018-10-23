package com.lnwazg.kit.filepack;

/**
 * 文件包类型<br>
 * 实践证明，应该先压缩后加密（而非先加密后压缩），这样可以大大降低目标文件体积。
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
     * 文件数量-文件名（支持文件夹内置）-压缩字节（所有信息都有，并且文件大小最小，适合性能优先并不需要太考虑安全性的场景）
     */
    NumNameCompressByte((byte)0x03),
    /**
     * 文件数量-文件名（支持文件夹内置）-加密字节（所有信息都有，安全性增强，体积会比普通文件略大，注重安全时可以采用）
     */
    NumNameEncryptByte((byte)0x04),
    /**
     * 文件数量-文件名（支持文件夹内置）-压缩加密字节（所有信息都有，安全性增强）
     */
    NumNameCompressEncryptByte((byte)0x05),
    /**
     * JSON大小-JSON-字节
     */
    IntJsonByte((byte)0x11),
    /**
     * JSON大小-JSON-压缩字节
     */
    IntJsonCompressByte((byte)0x12),
    /**
     * JSON大小-JSON-压缩加密字节（面向未来的安全压缩格式）
     */
    IntJsonCompressEncryptByte((byte)0x13);
    
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
        for (PackType packType : values())
        {
            if (packType.getType() == paramByte)
            {
                return packType;
            }
        }
        return null;
    }
}
