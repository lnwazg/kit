package com.lnwazg.kit.task.tiny;

public interface DoThis<T>
{
    void ifOK(T result);
    
    void ifNotOK(Exception e);
}
