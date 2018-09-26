package com.lnwazg.kit.cache.memcache;

/**
 * memcache的客户端<br>
 * 采用xmemcached的实现（基于Java NIO实现）<br>
 * 1.Xmemcached支持所有的memcached协议
 * 2.Memcached的分布只能通过客户端来实现，XMemcached实现了此功能，并且提供了一致性哈希(consistent hash)算法的实现。
 * 3.XMemcached允许通过设置节点的权重来调节memcached的负载，设置的权重越高，该memcached节点存储的数据将越多，所承受的负载越大。
 * 4.XMemcached允许通过JMX或者代码编程实现节点的动态添加或者移除，方便用户扩展和替换节点等。
 * 5.XMemcached通过JMX暴露的一些接口，支持client本身的监控和调整，允许动态设置调优参数、查看统计数据、动态增删节点等。
 * 
 * @author nan.li
 * @version 2018年9月26日
 */
public class MemcacheClient
{
    
    
    
}
