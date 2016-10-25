package com.cat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;
import com.zuipin.util.StringUtils;

public class DateTypeConverter extends StrutsTypeConverter {
	
	private static SimpleDateFormat	sf	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat	sdf	= new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		try {
			Date date = null;
			if (values != null && values.length > 0) {
				if (StringUtils.isNotBlank(values[0])) {
					try {
						date = sf.parse(values[0]);
					} catch (Exception e) {
						date = sdf.parse(values[0]);
					}
				}
			}
			return date;
		} catch (ParseException e) {
			throw new TypeConversionException(e.getMessage() + "[" + values + " - class: " + toClass + "]");
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		return sf.format(o);
	}
	
}
