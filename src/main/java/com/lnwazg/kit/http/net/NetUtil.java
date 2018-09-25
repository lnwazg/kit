package com.lnwazg.kit.http.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ServerSocketFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.InetAddresses;

/**
 * 关于网络的工具类<br>
 * 1. 获取本机IP地址与HostName<br>
 * 2. 查找空闲端口
 */
public class NetUtil
{
    private static Logger logger = LoggerFactory.getLogger(NetUtil.class);
    
    /**
     * 端口扫描起始端口号
     */
    public static final int PORT_RANGE_MIN = 1024;
    
    /**
     * 端口扫描结束端口号
     */
    public static final int PORT_RANGE_MAX = 65535;
    
    /**
     * 随机数工具类
     */
    private static Random random = new Random();
    
    /////// LocalAddress //////
    /**
     * 懒加载进行探测<br>
     * 使用内部类的方式实现单例模式（饿汉、懒汉模式外的第三种实践方式）
     */
    private static class LocalAddressHolder
    {
        static final LocalAddress INSTANCE = new LocalAddress();
    }
    
    /**
     * 获得本地地址
     */
    public static InetAddress getLocalAddress()
    {
        return LocalAddressHolder.INSTANCE.localInetAddress;
    }
    
    /**
     * 获得本地Ip地址
     */
    public static String getLocalHost()
    {
        return LocalAddressHolder.INSTANCE.localHost;
    }
    
    /**
     * 获得本地HostName
     */
    public static String getHostName()
    {
        return LocalAddressHolder.INSTANCE.hostName;
    }
    
    /**
     * 本地地址的内部类
     * @author nan.li
     * @version 2018年9月18日
     */
    private static class LocalAddress
    {
        private InetAddress localInetAddress;
        
        private String localHost;
        
        private String hostName;
        
        public LocalAddress()
        {
            initLocalAddress();
            // from Common Lang SystemUtils
            hostName = SystemUtils.IS_OS_WINDOWS ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
        }
        
        /**
         * 初始化本地地址
         */
        private void initLocalAddress()
        {
            NetworkInterface nic = null;
            // 根据命令行执行hostname获得本机hostname， 与/etc/hosts 中该hostname的第一条ip配置，获得ip地址
            try
            {
                localInetAddress = InetAddress.getLocalHost();
                nic = NetworkInterface.getByInetAddress(localInetAddress);
            }
            catch (Exception ignored)
            {
            }
            
            // 如果结果为空，或是一个loopback地址(127.0.0.1), 或是ipv6地址，再遍历网卡尝试获取
            if (localInetAddress == null || nic == null || localInetAddress.isLoopbackAddress()
                || localInetAddress instanceof Inet6Address)
            {
                InetAddress lookedUpAddr = findLocalAddressViaNetworkInterface();
                // 仍然不符合要求，只好使用127.0.0.1
                try
                {
                    localInetAddress = lookedUpAddr != null ? lookedUpAddr : InetAddress.getByName("127.0.0.1");
                }
                catch (UnknownHostException ignored)
                {
                }
            }
            localHost = InetAddresses.toAddrString(localInetAddress);
            logger.info("localhost is {}", localHost);
        }
        
        /**
         * 合并系统变量(-D)，环境变量 和默认值，以系统变量优先
         */
        private static String getString(String propertyName, String envName, String defaultValue)
        {
            String propertyValue = System.getProperty(propertyName);
            if (propertyValue != null)
            {
                return propertyValue;
            }
            else
            {
                propertyValue = System.getenv(envName);
                return propertyValue != null ? propertyValue : defaultValue;
            }
        }
        
        /**
         * 根据preferNamePrefix 与 defaultNicList的配置网卡，找出合适的网卡
         */
        private static InetAddress findLocalAddressViaNetworkInterface()
        {
            // 如果hostname +/etc/hosts 得到的是127.0.0.1, 则首选这块网卡
            String preferNamePrefix = getString("localhost.prefer.nic.prefix",
                "LOCALHOST_PREFER_NIC_PREFIX",
                "bond0.");
            // 如果hostname +/etc/hosts 得到的是127.0.0.1, 和首选网卡都不符合要求，则按顺序遍历下面的网卡
            String defaultNicList = getString("localhost.default.nic.list",
                "LOCALHOST_DEFAULT_NIC_LIST",
                "bond0,eth0,em0,br0");
                
            InetAddress resultAddress = null;
            Map<String, NetworkInterface> candidateInterfaces = new HashMap<>();
            // 遍历所有网卡，找出所有可用网卡，尝试找出符合prefer前缀的网卡
            try
            {
                for (Enumeration<NetworkInterface> allInterfaces = NetworkInterface
                    .getNetworkInterfaces(); allInterfaces.hasMoreElements();)
                {
                    NetworkInterface nic = allInterfaces.nextElement();
                    // 检查网卡可用并支持广播
                    try
                    {
                        if (!nic.isUp() || !nic.supportsMulticast())
                        {
                            continue;
                        }
                    }
                    catch (SocketException ignored)
                    {
                        continue;
                    }
                    // 检查是否符合prefer前缀
                    String name = nic.getName();
                    if (name.startsWith(preferNamePrefix))
                    {
                        // 检查有否非ipv6 非127.0.0.1的inetaddress
                        resultAddress = findAvailableInetAddress(nic);
                        if (resultAddress != null)
                        {
                            return resultAddress;
                        }
                    }
                    else
                    {
                        // 不是Prefer前缀，先放入可选列表
                        candidateInterfaces.put(name, nic);
                    }
                }
                
                for (String nifName : defaultNicList.split(","))
                {
                    NetworkInterface nic = candidateInterfaces.get(nifName);
                    if (nic != null)
                    {
                        resultAddress = findAvailableInetAddress(nic);
                        if (resultAddress != null)
                        {
                            return resultAddress;
                        }
                    }
                }
            }
            catch (SocketException e)
            {
                return null;
            }
            return null;
        }
        
        /**
         * 检查有否非ipv6，非127.0.0.1的inetaddress
         */
        private static InetAddress findAvailableInetAddress(NetworkInterface nic)
        {
            for (Enumeration<InetAddress> indetAddresses = nic.getInetAddresses(); indetAddresses.hasMoreElements();)
            {
                InetAddress inetAddress = indetAddresses.nextElement();
                if (!(inetAddress instanceof Inet6Address) && !inetAddress.isLoopbackAddress())
                {
                    return inetAddress;
                }
            }
            return null;
        }
    }
    
    /////////// 查找空闲端口 /////////
    
    /**
     * 测试端口是否空闲可用, from Spring SocketUtils
     */
    public static boolean isPortAvailable(int port)
    {
        try
        {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port,
                1,
                InetAddress.getByName("localhost"));
            serverSocket.close();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
    
    /**
     * 从1024到65535， 随机找一个空闲端口 from Spring SocketUtils
     */
    public static int findRandomAvailablePort()
    {
        return findRandomAvailablePort(PORT_RANGE_MIN, PORT_RANGE_MAX);
    }
    
    /**
     * 在范围里随机找一个空闲端口,from Spring SocketUtils.
     * @throws IllegalStateException 最多尝试(maxPort-minPort)次，如无空闲端口，抛出此异常.
     */
    public static int findRandomAvailablePort(int minPort, int maxPort)
    {
        int portRange = maxPort - minPort;
        int candidatePort;
        int searchCounter = 0;
        
        do
        {
            if (++searchCounter > portRange)
            {
                throw new IllegalStateException(
                    String.format("Could not find an available tcp port in the range [%d, %d] after %d attempts",
                        minPort,
                        maxPort,
                        searchCounter));
            }
            candidatePort = minPort + random.nextInt(portRange + 1);
        } while (!isPortAvailable(candidatePort));
        
        return candidatePort;
    }
    
    /**
     * 从某个端口开始，递增直到65535，找一个空闲端口.
     * @throws IllegalStateException 范围内如无空闲端口，抛出此异常
     */
    public static int findAvailablePortFrom(int minPort)
    {
        for (int port = minPort; port < PORT_RANGE_MAX; port++)
        {
            if (isPortAvailable(port))
            {
                return port;
            }
        }
        
        throw new IllegalStateException(
            String.format("Could not find an available tcp port in the range [%d, %d]", minPort, PORT_RANGE_MAX));
    }
    
    /**
     * 是否可以telnet
     * @param ip      远程地址
     * @param port    远程端口
     * @param timeout 连接超时
     * @return 是否可连接
     */
    public static boolean canTelnet(String ip, int port, int timeout) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            return socket.isConnected() && !socket.isClosed();
        } catch (Exception e) {
            return false;
        } finally {
            IOUtils.closeQuietly(socket);
        }
    }
}
