package com.cat.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

/**
 * @文件:ChineseCharToEn.java
 * @说明:取得给定汉字串的首字母串,即声母串 Title: ChineseCharToEn
 * @版权：醉品春秋 版权所有 (c) 2016
 * @作者:cjj
 * @创建日期：2016年2月17日
 */
public final class ChineseCharToEn {
	private final static int[]		li_SecPosValue	= { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558,
			4684, 4925, 5249, 5590					};
	private final static String[]	lc_FirstLetter	= { "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "w", "x", "y", "z" };
	
	/**
	 * @功能描述：取得给定汉字串的首字母串,即声母串
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月17日下午2:09:04
	 * @param str
	 * @return
	 */
	public static String getAllFirstLetter(String str) {
		String _str = "";
		if (StringUtils.isNotBlank(str)) {
			for (int i = 0; i < str.length(); i++) {
				_str = _str + getFirstLetter(str.substring(i, i + 1));
			}
		}
		return _str;
	}
	
	/**
	 * 取得给定汉字的首字母,即声母
	 * 
	 * @param chinese
	 *            给定的汉字
	 * @return 给定汉字的声母
	 */
	public static String getFirstLetter(String chinese) {
		if (chinese == null || chinese.trim().length() == 0) {
			return "";
		}
		chinese = conversionStr(chinese, "GB2312", "ISO8859-1");
		
		if (chinese.length() > 1) // 判断是不是汉字
		{
			int li_SectorCode = (int) chinese.charAt(0); // 汉字区码
			int li_PositionCode = (int) chinese.charAt(1); // 汉字位码
			li_SectorCode = li_SectorCode - 160;
			li_PositionCode = li_PositionCode - 160;
			int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 汉字区位码
			if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
				for (int i = 0; i < 23; i++) {
					if (li_SecPosCode >= li_SecPosValue[i] && li_SecPosCode < li_SecPosValue[i + 1]) {
						chinese = lc_FirstLetter[i];
						break;
					}
				}
			} else // 非汉字字符,如图形符号或ASCII码
			{
				chinese = conversionStr(chinese, "ISO8859-1", "GB2312");
				chinese = chinese.substring(0, 1);
			}
		}
		
		return chinese;
	}
	
	/**
	 * @功能描述：字符串编码转换
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年3月19日下午5:35:40
	 * @param str
	 *            :要转换编码的字符串
	 * @param charsetName
	 *            :原来的编码
	 * @param toCharsetName
	 *            :经过编码转换后的字符串
	 * @return
	 */
	public static String conversionStr(String str, String charsetName, String toCharsetName) {
		try {
			str = new String(str.getBytes(charsetName), toCharsetName);
		} catch (UnsupportedEncodingException ex) {
			PrintContral.OutPrint("字符串编码转换异常：" + ex.getMessage());
		}
		return str;
	}
	
	public static void main(String[] args) {
		ChineseCharToEn cte = new ChineseCharToEn();
		PrintContral.OutPrint("获取拼音首字母：" + cte.getAllFirstLetter("北京联席办"));
	}
	
}
