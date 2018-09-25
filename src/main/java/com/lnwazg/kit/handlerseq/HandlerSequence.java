package com.lnwazg.kit.handlerseq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lnwazg.kit.log.Logs;
import com.lnwazg.kit.reflect.ClassKit;

/**
 * 一个通用的处理序列
 * 
 * @author Administrator
 * @version 2016年4月15日
 */
public class HandlerSequence {
	private static HandlerSequence INSTANCE = new HandlerSequence();

	/**
	 * 单线程的线程池
	 */
	public static ExecutorService singleExec = Executors.newSingleThreadExecutor();

	public static HandlerSequence getInstance() {
		return INSTANCE;
	}

	/**
	 * 处理序列表
	 */
	private List<IHandler> handlers = new LinkedList<>();

	/**
	 * 往启动序列中追加启动器，并执行
	 * 
	 * @author Administrator
	 * @param handler
	 * @return
	 */
	public HandlerSequence exec(IHandler handler) {
		singleExec.execute(() -> {
			handler.handle();
		});
		return this;
	}

	/**
	 * 增加处理器任务
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerSequence addHandler(IHandler handler) {
		handlers.add(handler);
		return this;
	}

	/**
	 * 开始执行任务
	 */
	public void execAll() {
		Logs.i("开始执行所有任务...");
		for (IHandler iHandler : handlers) {
			singleExec.execute(() -> {
				iHandler.handle();
			});
		}
		// 处理完成后，将任务表清空
		handlers.clear();
		Logs.i("任务拉起完毕。");
	}

	/**
	 * 根据类的全路径执行任务，异步执行
	 * 
	 * @author nan.li
	 * @param classPath
	 */
	public static void executeTasksByTaskClazzPathAsync(String classPath, Object... args) {
		Logs.i("开始执行包：" + classPath + "下面的启动任务...");
		Class<?> taskClass = ClassKit.loadClass(classPath);
		Object taskObj = ClassKit.newInstance(classPath);
		Method[] methods = taskClass.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Task.class)) {
				singleExec.execute(() -> {
					try {
						Logs.i("开始调用任务：" + method.getName() + "...");
						method.setAccessible(true);
						method.invoke(taskObj, args);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				});
			}
		}
	}

	/**
	 * 根据类的全路径执行任务，同步执行
	 * 
	 * @param string
	 * @param appConfigs
	 */
	public static void executeTasksByTaskClazzPathSync(String classPath, Object... args) {
		Logs.i("开始执行包：" + classPath + "下面的启动任务...");
		Class<?> taskClass = ClassKit.loadClass(classPath);
		Object taskObj = ClassKit.newInstance(classPath);
		Method[] methods = taskClass.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Task.class)) {
				try {
					Logs.i("开始调用任务：" + method.getName() + "...");
					method.setAccessible(true);
					method.invoke(taskObj, args);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
