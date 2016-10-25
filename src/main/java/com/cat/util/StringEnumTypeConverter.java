package com.cat.util;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * @文件:StringEnumTypeConverter.java
 * @说明:枚举类型转化为字符串
 * @版权：醉品春秋 版权所有 (c) 2016
 * @作者:cjj
 * @创建日期：2016年2月17日
 */
public class StringEnumTypeConverter extends StrutsTypeConverter {
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		String value = values[0];
		Object[] constants = toClass.getEnumConstants();
		for (Object constant : constants) {
			if (value.equals(constant.toString()))
				return constant;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		return o.toString();
	}
	
}
