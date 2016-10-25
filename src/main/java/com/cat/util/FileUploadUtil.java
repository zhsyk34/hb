/**
 * 
 */
package com.cat.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.zuipin.util.Constants;
import com.zuipin.util.CopyFileUtil;

/**
 * @author: Santy
 * @date: 2015年5月28日
 */
public class FileUploadUtil {
	
	private static final Logger	log	= Logger.getLogger(FileUploadUtil.class);
	
	/**
	 * 图片上传
	 * 
	 * @author: Santy
	 * @date: 2015年5月28日
	 * @param imgFile
	 *            图片文件
	 * @param imgFileName
	 *            图片文件名称
	 * @param folderURL
	 *            文件保存路径——相对项目的路径
	 * @return 【成功、图片存储地址】 或者 【 失败、失败原因】
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	public static Map<String, Object> imgUpload(File imgFile, String imgFileName, HttpServletRequest request, String folderURL) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 判断是否为空
		if (imgFileName != null) {
			File basePath = new File(request.getRealPath("/") + folderURL); // File.separator + "Upload" + File.separator + "Sample");
			if (!basePath.exists()) {
				basePath.mkdirs();
			}
			try {
				// 建立唯一地址，获取到文件的绝对路径
				String path = imgFile.getAbsolutePath();
				String newPath = "";
				if (imgFileName.contains(".jpg")) {
					newPath = StringUtils.replace(path, ".tmp", ".jpg");
				} else if (imgFileName.contains(".jpeg")) {
					newPath = StringUtils.replace(path, ".tmp", ".jpeg");
				} else if (imgFileName.contains(".png")) {
					newPath = StringUtils.replace(path, ".tmp", ".png");
				} else if (imgFileName.contains(".bmp")) {
					newPath = StringUtils.replace(path, ".tmp", ".bmp");
				} else if (imgFileName.contains(".gif")) {
					newPath = StringUtils.replace(path, ".tmp", ".gif");
				}
				// 定义以图片格式为后缀名的文件格式
				File newUriFile = new File(new StringBuilder().append(newPath).toString());
				// 文件重命名
				imgFile.renameTo(newUriFile);
				// 得到文件名字
				String sysFileName = Constants.getCurrTime() + "." + StringUtils.substringAfter(imgFileName, ".");
				// 获取到文件绝对路径
				String origiNewPath = newUriFile.getPath();
				// 复制文件到指定盘
				CopyFileUtil.copyFile(origiNewPath, basePath + "/" + sysFileName, true);
				
				map.put("success", true);
				map.put("address", folderURL + sysFileName);
			} catch (NullPointerException e) {
				e.printStackTrace();
				log.error("图片上传失败! msg = " + e.getMessage());
				map.put("msg", "请选择图片!");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("图片上传失败! msg = " + e.getMessage());
				map.put("msg", "图片上传失败! msg = " + e.getMessage());
			}
		} else {
			map.put("msg", "图片上传失败! ");
		}
		
		return map;
	}
}
