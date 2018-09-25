package com.lnwazg.kit.property;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

import com.lnwazg.kit.log.Logs;
import com.lnwazg.kit.platform.Platforms;

/**
 * 多属性文件<br>
 * 一个属性文件中包含了多个属性文件
 * @author linan
 */
public class MultiPropFile {

	public static void main(String[] args) {
		String multiFilePath = Platforms.USER_HOME + "/Desktop/secure.properties";
		Map<String, List<String>> fileMap = MultiPropFile.loadMultiPropFile(multiFilePath, CharEncoding.UTF_8);
		System.out.println("fileMap: \n" + fileMap);
	}

	/**
	 * 根据指定路径加载多属性文件
	 * @param multiFilePath
	 * @return key为子文件标题，value为子文件行列表
	 */
	public static Map<String, List<String>> loadMultiPropFile(String multiFilePath, String encoding) {
		if (StringUtils.isEmpty(multiFilePath)) {
			Logs.e("multiFilePath is empty!");
			return null;
		}
		File multiFile = new File(multiFilePath);
		if (!multiFile.exists()) {
			Logs.e("multiFile does not exist!");
			return null;
		}
		Map<String, List<String>> map = new LinkedHashMap<>();
		// 具体的文件以中括号开始，例如： [mystation]
		try {
			//总文字列表
			List<String> list = FileUtils.readLines(multiFile, encoding);
			
			// 子文件内容
			List<String> subFileList = new ArrayList<>();
			// 子文件标题
			String subFileTitle = null;

			for (String line : list) {
				//当前行
				String lineTrim = StringUtils.trim(line);
				//若空白则忽略
				if (StringUtils.isBlank(lineTrim)) {
					continue;
				}
				
				//若是子标题
				if (isSubFileTitle(lineTrim)) {
					//先保存上一个子文件
					if (StringUtils.isNotEmpty(subFileTitle)) {
						map.put(subFileTitle, subFileList);
					}
					//然后初始化当前的子文件内容
					subFileTitle = getSubFileTitle(lineTrim);
					subFileList = new ArrayList<>();
				} else {
					//若不是子标题，则保存到内容位置
					subFileList.add(lineTrim);
				}
			}
			//全部遍历完成之后，肯定还有最后一个子文件还没保存，此时需要保存下来
			map.put(subFileTitle, subFileList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取子文件标题内容
	 * @param lineTrim
	 * @return
	 */
	private static String getSubFileTitle(String lineTrim) {
		return lineTrim.substring(1, lineTrim.length() - 1).trim();
	}

	/**
	 * 是否子文件标题
	 * @param lineTrim
	 * @return
	 */
	private static boolean isSubFileTitle(String lineTrim) {
		if (lineTrim.startsWith("[") && lineTrim.endsWith("]")) {
			return true;
		}
		return false;
	}

}
