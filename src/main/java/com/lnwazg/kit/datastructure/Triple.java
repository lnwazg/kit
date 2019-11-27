package com.lnwazg.kit.datastructure;

/**
 * 三个对象
 * @author nan.li
 * @version 2018年5月16日
 */
public class Triple<L, M, R>
{
    private L l;
    
    private M m;
    
    private R r;
    
    public Triple()
    {
    }
    
    public Triple(L left, M middle, R right)
    {
        this.l = left;
        this.m = middle;
        this.r = right;
    }
    
    public L getL()
    {
        return l;
    }
    
    public void setL(L l)
    {
        this.l = l;
    }
    
    public M getM()
    {
        return m;
    }
    
    public void setM(M m)
    {
        this.m = m;
    }
    
    public R getR()
    {
        return r;
    }
    
    public void setR(R r)
    {
        this.r = r;
    }
    
    /**
     * 根据等号左边的泛型，自动构造合适的Triple
     */
    public static <L, M, R> Triple<L, M, R> of(L left, M middle, R right)
    {
        return new Triple<L, M, R>(left, middle, right);
    }
    
    @Override
    public String toString()
    {
        return "Triple [l=" + l + ", m=" + m + ", r=" + r + "]";
    }
    
}
