package com.cat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一文件上传对象 2013.7.8 BY YH
 * 
 * @author Administrator
 */
public class UploadImg {
	
	/**
	 * @param request
	 * @param file
	 * @param filename
	 * @param uri
	 * @return 返回字符串
	 */
	@SuppressWarnings("deprecation")
	public String getUploadImgSaveUrl123(HttpServletRequest request, File file, String filename, String uri) {
		
		// 文件保存目录路径
		String savePath = request.getRealPath("/") + uri + "/";
		// 文件保存目录URL
		String saveUrl = uri + "/";
		
		if (!new File(savePath).exists()) {
			if (filename.indexOf("/") < 0) {
				String[] paths = uri.split("/");
				String newpath = request.getRealPath("/") + "/";
				for (int i = 0; i < paths.length; i++) {
					newpath += paths[i] + "/";
					File saveDirFile = new File(newpath);
					if (!saveDirFile.exists()) {
						saveDirFile.mkdirs();
					}
				}
			} else {
				String newpath = request.getRealPath("/") + "Upload/" + uri + "/";
				File saveDirFile = new File(newpath);
				if (!saveDirFile.exists()) {
					saveDirFile.mkdirs();
				}
			}
		}
		
		try {
			
			File saveDirFile = new File(savePath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
			
			String fileExt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			savePath += newFileName;
			saveUrl += newFileName;
			
			FileOutputStream fos = new FileOutputStream(savePath);
			FileInputStream fis = new FileInputStream(file);
			
			byte[] buffer = new byte[1024];
			int len = 0;
			
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveUrl;
	}
	
	/**
	 * @param request
	 * @param file
	 * @param filename
	 * @param uri
	 * @return 返回字符串
	 */
	@SuppressWarnings("deprecation")
	public String getDoUploadImgSaveUrl123(HttpServletRequest request, File file, String filename, String uri) {
		
		// 文件保存目录路径
		String savePath = request.getRealPath("/") + "Upload/" + uri + "/";
		// 文件保存目录URL
		String saveUrl = "/Upload/" + uri + "/";
		
		if (!new File(savePath).exists()) {
			if (filename.indexOf("/") < 0) {
				String[] paths = uri.split("/");
				String newpath = request.getRealPath("/") + "Upload/" + "/";
				for (int i = 0; i < paths.length; i++) {
					newpath += paths[i] + "/";
					File saveDirFile = new File(newpath);
					if (!saveDirFile.exists()) {
						saveDirFile.mkdirs();
					}
				}
			} else {
				String newpath = request.getRealPath("/") + "Upload/" + uri + "/";
				File saveDirFile = new File(newpath);
				if (!saveDirFile.exists()) {
					saveDirFile.mkdirs();
				}
			}
		}
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			savePath += ymd + "/";
			saveUrl += ymd + "/";
			File saveDirFile = new File(savePath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
			
			String fileExt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			savePath += newFileName;
			saveUrl += newFileName;
			
			FileOutputStream fos = new FileOutputStream(savePath);
			FileInputStream fis = new FileInputStream(file);
			
			byte[] buffer = new byte[1024];
			int len = 0;
			
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
				
			}
			fos.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveUrl;
	}
	
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	public void copyFile(HttpServletRequest request, String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(request.getRealPath("/") + oldPath);
			if (oldfile.exists()) { // 文件存在时
				// 查看传入的目录是否存在 如果不存在就创建
				if (newPath.split("/").length > 0) {
					String[] paths = newPath.split("/");
					String path = request.getRealPath("/") + "Upload/";
					for (int i = 0; i < paths.length; i++) {
						path += paths[i] + "/";
						File saveDirFile = new File(path);
						if (!saveDirFile.exists()) {
							saveDirFile.mkdirs();
						}
					}
				}
				File file = new File(request.getRealPath("/") + "Upload/" + newPath + oldPath.substring(oldPath.lastIndexOf("/"), oldPath.length()));
				file.createNewFile();
				InputStream inStream = new FileInputStream(request.getRealPath("/") + oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(request.getRealPath("/") + "Upload/" + newPath + oldPath.substring(oldPath.lastIndexOf("/"), oldPath.length()));
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			PrintContral.OutPrint("复制单个文件操作出错");
			e.printStackTrace();
			
		}
	}
	
	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 */
	public void moveFile(HttpServletRequest request, String oldPath, String newPath) {
		copyFile(request, oldPath, newPath);
		delFile(request, oldPath);
		
	}
	
	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	public void delFile(HttpServletRequest request, String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myDelFile = new File(request.getRealPath("/") + filePath);
			myDelFile.delete();
			
		} catch (Exception e) {
			PrintContral.OutPrint("删除文件操作出错");
			e.printStackTrace();
			
		}
	}
}
