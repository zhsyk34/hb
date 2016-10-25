package com.cat.util;

import net.sf.json.JSONObject;

import com.zuipin.action.BaseAction;

/**
 * @功能说明：提示信息基类
 */
public class MessageUtil {
	// 错误提示字符串名称
	public static final String	ERROR_CODE	= "error_code";
	// 错误信息提示字符串
	public static final String	ERROR		= "error";
	
	public static final String	CODE[]		= { "00", "01", "02", "03", "04" };
	public static final String	ERROR_MSG[]	= { "操作成功", "请求的参数有误", "数据库异常", "参数方法（method）不能为空", "自己写提示信息" };
	
	/**
	 * (0)操作成功 (1)请求的参数有误 (2)数据库异常
	 */
	public static JSONObject errorMsg(JSONObject json, int i) {
		json.put(BaseAction.SUCCESS, false);
		if (i > CODE.length) {
			json.put(ERROR_CODE, "10000");
			json.put(ERROR, "没有找到对应的错误信息");
			return json;
		}
		json.put(BaseAction.SUCCESS, false);
		json.put(ERROR_CODE, CODE[i]);
		json.put(ERROR, ERROR_MSG[i]);
		
		return json;
	}
	
	/**
	 * (0)操作成功 (1)请求的参数有误 (2)数据库异常 str: 自定义的提示信息
	 */
	public static JSONObject errorMsg(JSONObject json, int i, String str) {
		json.put(BaseAction.SUCCESS, false);
		if (i > CODE.length) {
			json.put(ERROR_CODE, "10000");
			json.put(ERROR, "没有找到对应的错误信息");
			return json;
		}
		json.put(ERROR_CODE, CODE[i]);
		json.put(ERROR, str);
		return json;
	}
	
}
