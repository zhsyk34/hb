package com.cat.util;

import java.util.Date;

import com.zuipin.util.PropertiesManager;
import com.zuipin.util.SpringContextHolder;
import com.zuipin.util.StringUtils;
import com.zuipin.util.TimeUtils;

/**
 * @文件:PrintContral.java
 * @说明:统一调试代码控制
 * @版权：醉品春秋 版权所有 (c) 2016
 * @作者:cjj
 * @创建日期：2016年2月14日
 */
public class PrintContral {
	/**
	 * @功能描述：
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月14日上午10:50:49
	 */
	public static void OutPrint(String outString) {
		// 获取文件读取bean
		PropertiesManager properties = SpringContextHolder.getBean(ConstantsMshop.PROPERTIES_MANAGE_BEAN);
		
		// 查询调试打印开关是否打开
		String flag = properties.getString(ConstantsMshop.DEBUGGER_PRINT_FLAG);
		// 是否打印类名
		String clazzPrint = properties.getString(ConstantsMshop.DEBUGGER_CLAZZ_PRINT);
		// 是否打印方法名
		String methodPrint = properties.getString(ConstantsMshop.DEBUGGER_METHOD_PRINT);
		// 是否打印日期
		String datePrint = properties.getString(ConstantsMshop.DEBUGGER_DATE_PRINT);
		// 是否打印行号
		String linePrint = properties.getString(ConstantsMshop.DEBUGGER_LINE_NUMBER_PRINT);
		// 如果短信开关已开
		if (StringUtils.isNotBlank(flag) && flag.equals("true")) {
			PrintContral.OutPrint("/#");
			if (StringUtils.isNotBlank(clazzPrint) && clazzPrint.equals("true")) {
				// 类名
				String clazz = Thread.currentThread().getStackTrace()[1].getClassName();
				PrintContral.OutPrint("#类:" + clazz);
			}
			if (StringUtils.isNotBlank(methodPrint) && methodPrint.equals("true")) {
				// 方法名
				String method = Thread.currentThread().getStackTrace()[1].getMethodName();
				PrintContral.OutPrint("#方法:" + method);
			}
			if (StringUtils.isNotBlank(datePrint) && datePrint.equals("true")) {
				// 日期-时间
				String date = TimeUtils.formatDateTime(new Date());
				PrintContral.OutPrint("#时间:" + date);
			}
			if (StringUtils.isNotBlank(linePrint) && linePrint.equals("true")) {
				// 行号
				Integer lineNumber = Thread.currentThread().getStackTrace()[1].getLineNumber();
				PrintContral.OutPrint("#行号:" + lineNumber);
			}
			PrintContral.OutPrint("#内容:" + outString);
			PrintContral.OutPrint("#/");
		}
		
	}
}
