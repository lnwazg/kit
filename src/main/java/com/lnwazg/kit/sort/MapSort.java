package com.lnwazg.kit.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 对map进行排序的工具
 * @author nan.li
 * @version 2017年7月28日
 */
public class MapSort
{
    /**
     * 对某个map进行排序<br>
     * 既可以对键排序，也可以对值排序，全在于你写的entryComparator对象
     * @author nan.li
     * @param map  待排序的map
     * @param entryComparator   排序器
     * @return  排序完返回的map
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map sort(Map map, Comparator entryComparator)
    {
        List<Map.Entry> arrayList = new ArrayList<>(map.entrySet());
        //对列表排序
        Collections.sort(arrayList, entryComparator);
        
        //有序的map
        Map sortedMap = new LinkedHashMap<>();
        for (Entry entry : arrayList)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    
}
