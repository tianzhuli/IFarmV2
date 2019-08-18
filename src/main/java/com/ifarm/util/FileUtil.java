package com.ifarm.util;

import java.io.File;
import java.util.UUID;

public class FileUtil {

	public static final String COMMON_FILE_PREFIX = "ifarm_file";

	public static final String COMMON_CHAR = "_";
	/**
	 * 生成特点的文件格式
	 * 
	 * @param filename
	 * @return
	 */
	public static String makeFileName(String filename, String type) {
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return COMMON_FILE_PREFIX + COMMON_CHAR + type + COMMON_CHAR + filename;
	}

	/**
	 * 生成UUID码之后生成的hash地址
	 * 
	 * @param filename
	 * @param savePath
	 * @return
	 */
	public static String makePath(String filename, String savePath, String userId) {
		// 得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4; // 0-15
		// 构造新的保存目录
		String dir = savePath + "\\" + userId + "\\" + dir1 + "\\" + dir2; // upload\2\3
		// upload\3\5
		// File既可以代表文件也可以代表目录
		File file = new File(dir);
		// 如果目录不存在
		if (!file.exists()) {
			// 创建目录
			file.mkdirs();
		}
		return dir;
	}

	public static String makeRealPath(String savePath, String fileName, String userId) {
		String realSavePath = makePath(fileName, savePath, userId);
		String savaRealPath = realSavePath + "\\" + fileName;
		File saveFile = new File(savaRealPath);
		if (!saveFile.exists()) {
			saveFile.mkdirs();
		}
		return savaRealPath;
	}

	public static String makePathUrl(String filename, String savePath, String userId) {
		// 得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4; // 0-15
		// 构造新的保存目录
		String dir = savePath + "/" + userId + "/" + dir1 + "/" + dir2; // upload\2\3
		// upload\3\5
		// File既可以代表文件也可以代表目录
		File file = new File(dir);
		// 如果目录不存在
		if (!file.exists()) {
			// 创建目录
			file.mkdirs();
		}
		return dir;
	}

	public static String makeRealPathUrl(String savePath, String fileName, String userId) {
		String realSavePath = makePathUrl(fileName, savePath, userId);
		return realSavePath + "/" + fileName;
	}
}
