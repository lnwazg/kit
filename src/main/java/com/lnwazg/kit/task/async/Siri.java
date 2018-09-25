package com.lnwazg.kit.task.async;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lnwazg.kit.log.Logs;
import com.lnwazg.kit.reflect.ClassKit;
import com.lnwazg.kit.singleton.B;

/**
 * Siri是一个系统小助手（函数版）<br>
 * 可以帮你在后台默默地完成很多任务， 如果任务较多，则自动排队依次完成<br>
 * Siri是声明式编程的一种重要实践<br>
 * 我原本是想做成一个精简化的mq，分为发送端和接收端<br>
 * 发送端用于发指令、接收端用于接收指令并执行。<br>
 * 指令的数量可能很多，这样就可能会造成消息的堆积，避免瞬间大量的计算压垮app<br>
 * 然而，仔细分析发现，其实发送端和接收端是在同一个应用内，那么rpc调用就可以省略了，这样还能提高性能，也就意味着不需要再用mq了<br>
 * 当然你内置一个mq也不是不行，但是自己发自己收，总有点自己玩自己、脱裤子放屁的感觉，并且那样性能并不好<br>
 * 所以，在保持用户体验(接口调用方式)不变的前提下，将mq方式改成工具类内部直接invoke的方式，会更加性能优越<br>
 * 当然，siri.listen("topicName",params)这样的参数形式其实跟mq的调用体验其实是很类似的，并且还更加简洁易用，这是优越性<br>
 * 其次，如果不使用mq，那么消息堆积能力就是一个考验。好在，内部调用的消息量并不会太多，那么剩下的问题只是如何解决避免瞬间大量的计算压垮app<br>
 * 一个绝佳的方案就是用线程池，让任务在内存中排队，newSingleThreadExecutor()，然后依次顺序静默排队执行，即可完美解决！<br>
 * 这就是Siri这个工具类的前世今生！哈哈哈
 * @author nan.li
 * @version 2018年2月5日
 */
public class Siri
{
    /**
     * 任务注册表
     */
    static Map<String, SiriTask> siriTaskRegistry = new ConcurrentHashMap<>();
    
    /**
     * 单线程的线程池
     */
    public static ExecutorService singleExec = Executors.newSingleThreadExecutor();
    
    static
    {
        registerSystemCompoments();
    }
    
    public static void main(String[] args)
    {
        //扩展我的工具箱
        //Siri.expandTaskLibrary("com.lnwazg.kit.task.async.impl");
        
        //after doing something mess...
        //I want to do a system async call
        //to process my task
        
        //siri, execute my order!
        Siri.run("createTxtFile", "c:\\1.txt", "Hi,this is created by siri!");
//        Siri.run("createTxtFile", "d:\\1.txt", "Hi,this is created by siri!");
//        Siri.run("createTxtFile", "e:\\1.txt", "Hi,this is created by siri!");
    }
    
    /**
     * 听我的命令，去执行<br>
     * 声明式编程，就是把事情丢给别人做，组件化、模块化，是一键调用去完成各种复杂的事情<br>
     * 发指令，在后台默默执行并完成，无声无息、高效给力<br>
     * 将系统内的复杂功能模块化，然后用siri去调用，会显得高效爽快！
     * @author nan.li
     * @param taskName
     * @param params
     */
    public static void run(String taskName, Object... params)
    {
        SiriTask siriTask = findTaskByTypeNameInSystemRegistry(taskName);
        if (siriTask != null)
        {
            singleExec.execute(() -> {
                //Logs.i("begin to execute siri task: " + taskName);
                Logs.i("开始执行siri任务: " + taskName);
                siriTask.execute(params);
            });
        }
    }
    
    /**
     * 查表
     * @author nan.li
     * @param taskName
     * @return
     */
    private static SiriTask findTaskByTypeNameInSystemRegistry(String taskName)
    {
        taskName = taskName.toLowerCase();
        if (siriTaskRegistry.containsKey(taskName))
        {
            return siriTaskRegistry.get(taskName);
        }
        else
        {
            Logs.w(taskName + " was not registered in siriTaskRegistry!");
            return null;
        }
    }
    
    /**
     * 注册系统命令
     * @author nan.li
     */
    private static void registerSystemCompoments()
    {
        //包扫描的方式去动态扩展指令库
        packageSearch2Load("com.lnwazg.kit.task.async.impl");
    }
    
    /**
     * 用户扩充自己的任务库
     * @author nan.li
     * @param packageName
     */
    public static void expandTaskLibrary(String packageName)
    {
        packageSearch2Load(packageName);
    }
    
    /**
     * 包扫描并加载类<br>
     * 将类的全小写形式注册到任务库中
     * @author nan.li
     * @param string
     */
    private static void packageSearch2Load(String packageName)
    {
        Logs.i("begin to packageSearch2Load: " + packageName);
        List<Class<?>> classes = ClassKit.getPackageAllClasses(packageName);
        for (Class<?> clazz : classes)
        {
            
            String classSimpleName = clazz.getSimpleName();
            if (classSimpleName.indexOf("$") == -1)  //去除子类
            {
                //只有当这个clazz继承自或实现自SiriTask类时
                //这样可以规避掉那些出现在包扫描中的杂七杂八的类，防止这些非任务的类被误注册到注册表！
                if (SiriTask.class.isAssignableFrom(clazz))
                {
                    @SuppressWarnings("unchecked")
                    Class<SiriTask> siriTaskClass = (Class<SiriTask>)clazz;
                    
                    if (siriTaskRegistry.containsKey(classSimpleName.toLowerCase()))
                    {
                        Logs.w("Task library: " + classSimpleName.toLowerCase() + " exists already!");
                    }
                    
                    //siriTaskRegistry.put("createTxtFile", B.g(CreateTxtFile.class));
                    Logs.i("begin to register task lib: " + classSimpleName.toLowerCase());
                    siriTaskRegistry.put(classSimpleName.toLowerCase(), B.g(siriTaskClass));
                }
            }
        }
    }
}
