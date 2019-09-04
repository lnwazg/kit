package com.lnwazg.kit.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.lnwazg.kit.log.Logs;

/**
 * 带提示信息的Job
 * @author Administrator
 * @version 2016年2月12日
 */
public abstract class PromptJob implements Job
{
    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
        Logs.i(String.format("开始JOB任务【%s】", getClass().getCanonicalName()));
        executeCustom(context);
        Logs.i(String.format("完成JOB任务【%s】", getClass().getCanonicalName()));
    }
    
    /**
     * 执行我的自定义方法
     * @author Administrator
     * @param context
     */
    public abstract void executeCustom(JobExecutionContext context);
    
}
