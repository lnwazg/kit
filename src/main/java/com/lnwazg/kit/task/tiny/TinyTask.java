package com.lnwazg.kit.task.tiny;

import com.lnwazg.kit.executor.ExecMgr;

/**
 * 小任务
 * @author nan.li
 * @version 2018年2月2日
 */
public class TinyTask<T>
{
    private Something<T> something;
    
    private DoThis<T> doThis;
    
    public TinyTask(Something<T> something)
    {
        this.something = something;
    }
    
    public static void main(String[] args)
    {
        TinyTask.perform(new Something<String>()
        {
            @Override
            public String whichDoes()
            {
                return "123";
            }
            
        }).whenDone(new DoThis<String>()
        {
            @Override
            public void ifOK(String result)
            {
                System.out.println("结果是:" + result + " 完成了！");
            }
            
            @Override
            public void ifNotOK(Exception e)
            {
                System.out.println("没完成！");
            }
        }).go();
    }
    
    private void go()
    {
        ExecMgr.cachedExec.execute(() -> {
            if (something != null)
            {
                try
                {
                    T result = something.whichDoes();
                    if (doThis != null)
                    {
                        doThis.ifOK(result);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    if (doThis != null)
                    {
                        doThis.ifNotOK(e);
                    }
                }
            }
        });
    }
    
    private TinyTask<T> whenDone(DoThis<T> doThis)
    {
        this.doThis = doThis;
        return this;
    }
    
    private static <T> TinyTask<T> perform(Something<T> something)
    {
        return new TinyTask<T>(something);
    }
}
