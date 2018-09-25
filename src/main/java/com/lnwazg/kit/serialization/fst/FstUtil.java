package com.lnwazg.kit.serialization.fst;

import org.nustaq.serialization.FSTConfiguration;

/**
 * up to 10 times faster 100% JDK Serialization compatible drop-in replacement<br>
 * jdk序列化的一种有效可靠替代
 * @author linan
 */
public class FstUtil {

	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

	/**
	 * 序列化任意一个对象<br>
	 * 对象必须实现序列化接口<br>
	 * object must be serializable
	 * @param obj
	 * @return
	 */
	public static <T> byte[] serialize(T obj) {
		return conf.asByteArray(obj);
	}

	/**
	 * 将字节信息反序列化成对象
	 * @param data
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] data, Class<T> clazz) {
		return (T) conf.asObject(data);
	}
}
