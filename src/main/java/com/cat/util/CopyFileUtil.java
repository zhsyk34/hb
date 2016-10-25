package com.cat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 功能描述： 复制文件到指定盘
 * 
 * @author : Teny_lu 刘鹰
 * @ProjectName : zuipinerp
 * @FileName : CopyFileUtil.java
 * @E_Mail : liuying5590@163.com
 * @CreatedTime : 2014年12月4日-下午5:48:32
 */
public class CopyFileUtil {
	
	/**
	 * 功能描述： 复制文件到指定盘
	 * 
	 * @author : Teny_lu 刘鹰
	 * @E_Mail : liuying5590@163.com
	 * @CreatedTime : 2014年12月4日-下午5:47:30
	 * @return 复制单个文件
	 * @param srcFileName
	 *            待复制的文件名(完整的路径名称)
	 * @param destFileName
	 *            目标文件名(完整的路径名称)
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
		// 判断原文件是否存在
		File srcFile = new File(srcFileName);
		if (!srcFile.exists()) {
			PrintContral.OutPrint("复制文件失败：原文件" + srcFileName + "不存在！");
			return false;
		} else if (!srcFile.isFile()) {
			PrintContral.OutPrint("复制文件失败：" + srcFileName + "不是一个文件！");
			return false;
		}
		// 判断目标文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists()) {
			// 如果目标文件存在，而且复制时允许覆盖。
			if (overlay) {
				// 删除已存在的目标文件，无论目标文件是目录还是单个文件
				PrintContral.OutPrint("目标文件已存在，准备删除它！");
				destFile.delete();
			} else {
				PrintContral.OutPrint("复制文件失败：目标文件" + destFileName + "已存在！");
				return false;
			}
		} else {
			if (!destFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				PrintContral.OutPrint("目标文件所在的目录不存在，准备创建它！");
				if (!destFile.getParentFile().mkdirs()) {
					PrintContral.OutPrint("复制文件失败：创建目标文件所在的目录失败！");
					return false;
				}
			}
		}
		// 准备复制文件
		int byteread = 0;// 读取的位数
		InputStream in = null;
		OutputStream out = null;
		try {
			// 打开原文件
			in = new FileInputStream(srcFile);
			// 打开连接到目标文件的输出流
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			// 一次读取1024个字节，当byteread为-1时表示文件已经读完
			while ((byteread = in.read(buffer)) != -1) {
				// 将读取的字节写入输出流
				out.write(buffer, 0, byteread);
			}
			PrintContral.OutPrint("复制单个文件" + srcFileName + "至" + destFileName + "成功！");
			return true;
		} catch (Exception e) {
			PrintContral.OutPrint("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，注意先关闭输出流，再关闭输入流
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
