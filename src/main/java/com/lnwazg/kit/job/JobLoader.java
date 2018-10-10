package com.lnwazg.kit.job;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.lnwazg.kit.cache.redis.RedisClient;
import com.lnwazg.kit.executor.ExecMgr;
import com.lnwazg.kit.log.Logs;
import com.lnwazg.kit.reflect.ClassKit;

/**
 * 简易版的任务加载器
 * @author Administrator
 * @version 2016年4月24日
 */
public class JobLoader
{
    /**
     * 默认的任务扫描包
     */
    static String DEFAULT_JOB_SCAN_PACKAGE = "com.lnwazg.job";
    
    public static void main(String[] args)
    {
        JobLoader.loadDefaultPackageJob();
        JobLoader.loadPackageJob("com.lnwazg.job");
    }
    
    /**
     * 加载默认包下面的job
     * @author Administrator
     */
    public static void loadDefaultPackageJob()
    {
        loadPackageJob(DEFAULT_JOB_SCAN_PACKAGE);
    }
    
    /**
     * 加载指定包下面的job
     * @author Administrator
     * @param string
     */
    public static void loadPackageJob(String packageName)
    {
        //尝试取加载该包下面的所有配置信息
        List<Class<?>> classList = ClassKit.getPackageAllClasses(packageName);
        if (classList.size() > 0)
        {
            Logs.i("根据JOB_SCAN_PACKAGE加载定时器配置...");
            for (Class<?> clazz : classList)
            {
                //如果它继承自Job
                //如果Job是它的父类
                if (Job.class.isAssignableFrom(clazz))
                {
                    //如果是一个合法的类路径，例如：com.lnwazg.job.AlarmPullOutUdiskJob
                    //那么，直接加载初始化这个类即可，默认采用jobType为1的方式进行解析
                    @SuppressWarnings("unchecked")
                    Class<? extends Job> jobClass = (Class<? extends Job>)clazz;
                    String jobName = jobClass.getCanonicalName();
                    //默认采用jobType为1的方式进行解析
                    String cron = "";
                    if (jobClass.isAnnotationPresent(Scheduled.class))
                    {
                        Scheduled scheduled = jobClass.getAnnotation(Scheduled.class);
                        cron = scheduled.cron();
                    }
                    if (StringUtils.isEmpty(cron))
                    {
                        Logs.w(String.format("%s 未提供注解cron表达式配置，因此忽略该JOB！", jobName));
                        continue;
                    }
                    try
                    {
                        StdSchedulerFactory factory = new StdSchedulerFactory();
                        Scheduler scheduler = factory.getScheduler();
                        JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName, Scheduler.DEFAULT_GROUP).build();
                        Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity(jobName + "trigger", Scheduler.DEFAULT_GROUP)
                            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                            .build();
                        scheduler.scheduleJob(job, trigger);
                        scheduler.start();
                        Logs.d(String.format("QUARTZ_JOB %s 【%s】已启动！", jobName, cron));
                    }
                    catch (SchedulerException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            Logs.i("定时器加载完毕！");
        }
    }
    
    /**
     * 多节点Job加载器<br>
     * 需要配合分布式缓存才能有效搭配工作
     * @author nan.li
     * @param packageName
     */
    public static void multiNodeLoadPackageJob(String packageName, RedisClient redisClient, String appQuartzStartedFlagName)
    {
        //一个比较简单的思路： 让集群中只有一个节点跑job即可。
        //应用启动的时候，往redis设置一个flag，有效期为10分钟。 
        //启动时先检查这个flag，若不存在，则插入这个flag，并启动该节点的quartz。
        //否则，该flag存在，说明集群中已经有节点启用了quartz，则本忽略启动quartz。
        
        //启动了quartz的那个节点，每隔10分钟再往redis里面插入一次那个flag，作为心跳，将失效的flag重新刷新出来。
        //这样的话，其它节点就算是超过10分钟再启动也没问题了
        //这个方案比较简单好理解，缺点就是有单点问题
        //集群里谁先启动，定时任务就一直在那台机器上执行，其他机器都不启动定时任务。
        
        Logs.i("multiNodeLoadPackageJob appQuartzStartedFlagName=" + appQuartzStartedFlagName);
        //节点标记的过期时间
        int keyTimeoutSeconds = 1 * 60;
        String appQuartzStartedFlag = redisClient.get(appQuartzStartedFlagName);
        if (StringUtils.isEmpty(appQuartzStartedFlag))
        {
            Logs.i("begin to multiNodeLoadPackageJob...");
            //此时集群的job尚未启动
            //然后就写入标记，标记集群job已经启动了，因为起job本身也要花时间，所以该标记越早设置越好
            ExecMgr.startDaemenThread(() -> {
                
                //这样做的好处是，若job节点因为故障而宕机，那么该标记也会在有限的过期时间后自动释放，集群此时可以通过一次重启来重新进行job启动节点的争夺
                //而如果job节点正常，那么会持续不断地保持该标记，那么其余的后起的节点也会“识趣”地避免job模块被重复启动
                while (true)
                {
                    //先写入标记，指定标记过期时间
                    redisClient.put(appQuartzStartedFlagName, "1", keyTimeoutSeconds);
                    try
                    {
                        //然后休眠过期时间
                        TimeUnit.SECONDS.sleep(keyTimeoutSeconds - 5);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            
            //开始起job
            loadPackageJob(packageName);
        }
        else
        {
            Logs.i("cluster job already started, ignore multiNodeLoadPackageJob!");
        }
    }
}
