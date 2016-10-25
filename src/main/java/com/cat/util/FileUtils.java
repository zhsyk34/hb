package com.cat.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

/**
 * @author zengXinChao
 * @date 2016年3月9日 上午11:54:05
 * @Description:文件操作类
 */
public class FileUtils {
	/**
	 * @Title: 保存上传文件
	 * @Description: TODO
	 * @author: zengXinChao
	 * @date: 2016年3月31日 上午11:31:01
	 * @param file
	 *            文件
	 * @param path
	 *            文件夹路径
	 * @param fileName
	 *            文件名
	 * @throws IOException
	 * @return void
	 */
	public static void upload(File file, String path, String fileName) throws IOException {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		FileInputStream fis = new FileInputStream(file);
		path = path.replace("/", "\\");
		path = path.replace("\\\\", "\\");
		FileOutputStream fos = new FileOutputStream(path + fileName);
		byte[] buffer = new byte[1024];
		int length = fis.read(buffer);
		
		while (length > 0) {
			fos.write(buffer, 0, length);
			length = fis.read(buffer);
		}
		
		fis.close();
		fos.flush();
		fos.close();
	}
	
	/**
	 * @功能描述：图片上传功能
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年4月15日下午2:51:31
	 * @param file
	 * @param path
	 * @throws IOException
	 */
	public static void upload(File file, String path) throws IOException {
		byte[] buffer = new byte[1024];
		
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(path);
		
		int length = fis.read(buffer);
		while (length > 0) {
			fos.write(buffer, 0, length);
			length = fis.read(buffer);
		}
		
		fis.close();
		fos.flush();
		fos.close();
	}
	
	/**
	 * @功能描述：通过输入流保存文件
	 * @作者 : lenary
	 * @创建时间 : 2016年7月28日下午9:29:22
	 * @param inputStream
	 * @param path
	 * @throws IOException
	 */
	public static void upload(BufferedInputStream inputStream, String path, String destFile) throws IOException {
		int v;
		File _file = new File(path);
		FileOutputStream outputStream = new FileOutputStream(_file);
		byte[] bytes = new byte[1024];
		while ((v = inputStream.read(bytes)) > 0) {
			outputStream.write(bytes, 0, v);
		}
		outputStream.close();
		inputStream.close();
		
		Thumbnails.of(path).size(100, 70).toFile(destFile);
	}
	
	/**
	 * @功能描述：通过输入流保存文件
	 * @作者 : lenary
	 * @创建时间 : 2016年7月28日下午9:29:22
	 * @param inputStream
	 * @param path
	 * @throws IOException
	 */
	public static void upload(BufferedInputStream inputStream, String path) throws IOException {
		int v;
		FileOutputStream outputStream = new FileOutputStream(new File(path));
		byte[] bytes = new byte[1024];
		while ((v = inputStream.read(bytes)) > 0) {
			outputStream.write(bytes, 0, v);
		}
		outputStream.close();
		inputStream.close();
	}
	
	/**
	 * @Title: remove
	 * @Description: 删除文件
	 * @author: zengXinChao
	 * @date: 2016年3月9日 上午11:56:03
	 * @param path
	 * @return void
	 */
	public static void remove(String path) {
		File file = new File(path);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}
}
