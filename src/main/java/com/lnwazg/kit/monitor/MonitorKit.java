package com.lnwazg.kit.monitor;

import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.lnwazg.kit.gson.GsonKit;
import com.lnwazg.kit.map.Maps;

/**
 * 监控工具，便于实时观察系统状态<br>
 * @author nan.li
 * @version 2017年9月7日
 */
public class MonitorKit
{
    public static void printSystemStatus()
    {
        //MemoryMXBean
        MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
        MemoryUsage usage = memorymbean.getHeapMemoryUsage();
        System.out.println("INIT HEAP: " + FileUtils.byteCountToDisplaySize(usage.getInit()));
        System.out.println("MAX HEAP: " + FileUtils.byteCountToDisplaySize(usage.getMax()));
        System.out.println("USE HEAP: " + FileUtils.byteCountToDisplaySize(usage.getUsed()));
        
        System.out.println("\nFull Information:");
        System.out.println("Heap Memory Usage: " + memorymbean.getHeapMemoryUsage());
        System.out.println("Non-Heap Memory Usage: " + memorymbean.getNonHeapMemoryUsage());
        
        //获取运行时信息 
        System.out.println("=======================RuntimeMXBean============================ ");
        RuntimeMXBean rmb = (RuntimeMXBean)ManagementFactory.getRuntimeMXBean();
        System.out.println("getClassPath " + rmb.getClassPath());
        System.out.println("getLibraryPath " + rmb.getLibraryPath());
        System.out.println("getVmVersion " + rmb.getVmVersion());
        
        List<String> inputArguments = rmb.getInputArguments();
        System.out.println("===================java options=============== ");
        System.out.println(inputArguments);
        
        System.out.println("=======================通过java来获取相关系统状态============================ ");
        System.out.println("总的内存量：" + FileUtils.byteCountToDisplaySize(Runtime.getRuntime().totalMemory()));
        System.out.println("空闲内存量：" + FileUtils.byteCountToDisplaySize(Runtime.getRuntime().freeMemory()));
        System.out.println("最大内存量：" + FileUtils.byteCountToDisplaySize(Runtime.getRuntime().maxMemory()));
        
        System.out.println("=======================OperatingSystemMXBean============================ ");
        OperatingSystemMXBean osm = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        
        //获取操作系统相关信息 
        System.out.println("osm.getArch() " + osm.getArch());
        System.out.println("osm.getAvailableProcessors() " + osm.getAvailableProcessors());
        System.out.println("osm.getName() " + osm.getName());
        System.out.println("osm.getVersion() " + osm.getVersion());
        
        //获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况 
        System.out.println("=======================ThreadMXBean============================ ");
        ThreadMXBean tm = (ThreadMXBean)ManagementFactory.getThreadMXBean();
        System.out.println("getThreadCount " + tm.getThreadCount());
        System.out.println("getPeakThreadCount " + tm.getPeakThreadCount());
        System.out.println("getCurrentThreadCpuTime " + tm.getCurrentThreadCpuTime());
        System.out.println("getDaemonThreadCount " + tm.getDaemonThreadCount());
        System.out.println("getCurrentThreadUserTime " + tm.getCurrentThreadUserTime());
        
        //当前编译器情况 
        System.out.println("=======================CompilationMXBean============================ ");
        CompilationMXBean gm = (CompilationMXBean)ManagementFactory.getCompilationMXBean();
        System.out.println("getName " + gm.getName());
        System.out.println("getTotalCompilationTime " + gm.getTotalCompilationTime());
        
        //获取多个内存池的使用情况 
        System.out.println("=======================MemoryPoolMXBean============================ ");
        List<MemoryPoolMXBean> mpmList = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mpm : mpmList)
        {
            System.out.println("getUsage " + mpm.getUsage());
            System.out.println("getMemoryManagerNames " + mpm.getMemoryManagerNames().toString());
        }
        
        //获取GC的次数以及花费时间之类的信息 
        System.out.println("=======================MemoryPoolMXBean============================ ");
        List<GarbageCollectorMXBean> gcmList = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcm : gcmList)
        {
            System.out.println("getName " + gcm.getName());
            System.out.println("getMemoryPoolNames " + gcm.getMemoryPoolNames());
        }
    }
    
    /**
     * 获取系统状态
     * @author nan.li
     * @return
     */
    public static Map<String, Object> getSystemStatus()
    {
        MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memorymbean.getHeapMemoryUsage();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
        Runtime runtime = Runtime.getRuntime();
        
        return Maps.asLinkedHashMap("HeapMemoryUsage",
            Maps.asLinkedHashMap("Init",
                FileUtils.byteCountToDisplaySize(heapMemoryUsage.getInit()),
                "Max",
                FileUtils.byteCountToDisplaySize(heapMemoryUsage.getMax()),
                "Used",
                FileUtils.byteCountToDisplaySize(heapMemoryUsage.getUsed()),
                "HeapMemoryUsage",
                memorymbean.getHeapMemoryUsage(),
                "NonHeapMemoryUsage",
                memorymbean.getNonHeapMemoryUsage()),
            "RuntimeInfo",
            Maps.asLinkedHashMap("ClassPath",
                runtimeMXBean.getClassPath(),
                "LibraryPath",
                runtimeMXBean.getLibraryPath(),
                "VmVersion",
                runtimeMXBean.getVmVersion(),
                "InputArguments",
                runtimeMXBean.getInputArguments()),
            "Memory",
            Maps.asLinkedHashMap("totalMemory",
                FileUtils.byteCountToDisplaySize(runtime.totalMemory()),
                "freeMemory",
                FileUtils.byteCountToDisplaySize(runtime.freeMemory()),
                "maxMemory",
                FileUtils.byteCountToDisplaySize(runtime.maxMemory())),
            "OSInfo",
            Maps.asLinkedHashMap("Arch",
                operatingSystemMXBean.getArch(),
                "AvailableProcessors",
                operatingSystemMXBean.getAvailableProcessors(),
                "Name",
                operatingSystemMXBean.getName(),
                "Version",
                operatingSystemMXBean.getVersion()),
            "Thread",
            Maps.asLinkedHashMap("ThreadCount",
                threadMXBean.getThreadCount(),
                "PeakThreadCount",
                threadMXBean.getPeakThreadCount(),
                "CurrentThreadCpuTime",
                threadMXBean.getCurrentThreadCpuTime(),
                "DaemonThreadCount",
                threadMXBean.getDaemonThreadCount(),
                "CurrentThreadUserTime",
                threadMXBean.getCurrentThreadUserTime()),
            "Compiler",
            Maps.asLinkedHashMap("Name",
                compilationMXBean.getName(),
                "TotalCompilationTime",
                compilationMXBean.getTotalCompilationTime()));
    }
    
    /**
     * 获取系统状态，以Json形式返回出来
     * @author nan.li
     * @return
     */
    public static String getSystemStatusAsJson()
    {
        return GsonKit.parseObject2PrettyString(getSystemStatus());
    }
    
    public static void main(String[] args)
    {
        System.out.println(getSystemStatusAsJson());
    }
}
