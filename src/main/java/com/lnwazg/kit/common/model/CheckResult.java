package com.lnwazg.kit.common.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 检查结果对象
 * @author nan.li
 * @version 2017年7月22日
 */
public class CheckResult
{
    /**
     * 是否检查通过
     */
    private boolean success;
    
    /**
     * 错误码<br>
     * 0表示成功，其他表示失败
     */
    private Integer errno;
    
    /**
     * 错误描述
     */
    private String errmsg;
    
    /**
     * 额外的数据<br>
     */
    private Object data;
    
    public Integer getErrno()
    {
        return errno;
    }
    
    public CheckResult setErrno(Integer errno)
    {
        this.errno = errno;
        return this;
    }
    
    public String getErrmsg()
    {
        return errmsg;
    }
    
    public CheckResult setErrmsg(String errmsg)
    {
        this.errmsg = errmsg;
        return this;
    }
    
    public Object getData()
    {
        return data;
    }
    
    public CheckResult setData(Object data)
    {
        this.data = data;
        return this;
    }
    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    public boolean isSuccess()
    {
        return success;
    }
    
    public CheckResult setSuccess(boolean success)
    {
        this.success = success;
        return this;
    }
}
