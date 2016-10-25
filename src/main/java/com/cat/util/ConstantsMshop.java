package com.cat.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.zuipin.util.HttpClientUtil;
import com.zuipin.util.PropertiesManager;
import com.zuipin.util.SpringContextHolder;

import net.sf.json.JSONObject;

/**
 * @文件:ConstantsMshop.java
 * @说明:系统常量类
 * @版权：醉品春秋 版权所有 (c) 2016
 * @作者:cjj
 * @创建日期：2016年2月17日
 */
public class ConstantsMshop {
	
	/**
	 * 商品文件路径
	 */
	public static final String	PRODUCT_IMG_FILE				= "/product/";
	
	/**
	 * cbt前置url
	 */
	public static final String	PRE_URL							= "preurl";
	
	/**
	 * 属性管理器的bean名称
	 */
	public static final String	PROPERTIES_MANAGE_BEAN			= "propertiesManager";
	
	/**
	 * 短信开关配置文件属性名
	 */
	public static final String	SEND_MSG_FLAG					= "sendMsg";							// 获取短信开关属性名
	/**
	 * 调试打印总开关
	 */
	public static final String	DEBUGGER_PRINT_FLAG				= "isDebuggerPrint";
	/**
	 * 调试打印,是否打印类名
	 */
	public static final String	DEBUGGER_CLAZZ_PRINT			= "clazzPrint";
	/**
	 * 调试打印,是否打印方法名
	 */
	public static final String	DEBUGGER_METHOD_PRINT			= "methodPrint";
	/**
	 * 调试打印,是否打印日期
	 */
	public static final String	DEBUGGER_DATE_PRINT				= "datePrint";
	/**
	 * 调试打印,是否打印行号
	 */
	public static final String	DEBUGGER_LINE_NUMBER_PRINT		= "lineNumberPrint";
	
	/**
	 * 系统名称
	 */
	public static final String	SYSTEM_NAME						= "system_name";
	/**
	 * 茶帮通接口地址
	 */
	public static final String	SYSTEM_URL						= "system_url";
	/**
	 * 店铺Id 名称
	 */
	public static final String	SHOP_ID							= "currentShopId";
	/**
	 * 默认店铺ID 开发用
	 */
	public static final Long	DEFAULT_SHOP_ID					= 1L;
	/**
	 * 默认获得卡号后几位
	 */
	public static final Integer	DEFAULT_CARD_NUM				= 4;
	/**
	 * 页面显示的订单状态列表
	 */
	public static final String	ORDER_STATUS					= "orderStatusList";
	/**
	 * 获取storeId key
	 */
	public static final String	STORE_KEY						= "store.id";
	/**
	 * 醉品商城分销平台 默认ID
	 */
	public static final Integer	DEFAULT_ZUIPIN_MS_STORE_ID		= 17;
	/**
	 * erp 系统url
	 */
	public static final String	ERP_URL_KEY						= "erp_url";
	/**
	 * 分销系统 调用 erp key
	 */
	public static final String	MSHOP_ERP_KEY					= "mshop_erp_";
	/**
	 * 创建订单API key
	 */
	public static final String	CREATE_ORDER_KEY				= "create_order";
	/**
	 * 更新订单状态API KEY
	 */
	public static final String	UPDATE_ORDER_STATUS_KEY			= "update_order_status";
	/**
	 * 查询订单物流信息
	 */
	public static final String	QUERY_ORDER_DELIVER_KEY			= "query_order_deliver";
	/**
	 * 商品Id key
	 */
	public static final String	PRODUCT_ID_KEY					= "productId";
	/**
	 * http client 接口调用默认超时时间 5分钟
	 */
	public static final Integer	DEFAULT_HTTP_CLIENT_TIME_OUT	= 300000;
	/**
	 * 订单 识别码(SP)+日期(yyyyMMdd)+5位流水号
	 */
	public static final String	ORDER_UDID_FIX					= "PE";
	
	// 快递公司
	public static String		ST								= "24";								// 申通
	public static String		SF								= "15";								// 顺丰
	public static String		DEPPON							= "4";									// 德邦物流
	public static String		HAOTONG							= "5";									// 联昊通速递
																										
	/***
	 * 转账单号
	 */
	public static final String	OUT_NO_FIX						= "OU";
	/**
	 * 子订单 识别码(SP)+日期(yyyyMMdd)+5位流水号
	 */
	public static final String	SUB_ORDER_UDID_FIX				= "SPE";
	/**
	 * 仓库作业单 识别码(SP)+日期(yyyyMMdd)+5位流水号
	 */
	public static final String	REPERTORY_WORK_UDID_FIX			= "RW";
	/**
	 * 检验单 识别码(SP)+日期(yyyyMMdd)+5位流水号
	 */
	public static final String	REPERTORY_CHECK_PRO_UDID_FIX	= "C";
	/**
	 * 发货单 识别码(SP)+日期(yyyyMMdd)+5位流水号
	 */
	public static final String	REPERTORY_SEND_PRO_UDID_FIX		= "RSP";
	
	/**
	 * 销售订单 识别码(GC)+店铺Id+日期(yyyyMMdd)+5位流水号
	 */
	public static final String	GET_CASH_UDID_FIX				= "GC";
	/**
	 * 默认编码
	 */
	public static final String	UTF8							= "UTF-8";
	/**
	 * joson 请求
	 */
	public static final String	JSON_APPLICATION_TYPE			= "application/json";
	/**
	 * form 表单
	 */
	public static final String	FORM_APPLICATION_TYPE			= "application/x-www-form-urlencoded";
	/**
	 * 微信支付回调url
	 */
	public static final String	NOTIFY_URL_KEY					= "wechat_pay_notify_url";
	/**
	 * 支付模式
	 */
	public static final String	PAY_IS_TEST						= "wechat_pay_pay_is_test";
	/**
	 * 订单Id
	 */
	public static final String	ORDER_ID						= "orderId";
	/**
	 * 数据展示默认长度
	 */
	public static final Integer	DATE_VIEW_LENGTH				= 19;
	
	/**
	 * @功能描述：获取短信开关
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月17日下午2:19:21
	 * @return
	 */
	public static String getSendMsgFlag() {
		PropertiesManager properties = SpringContextHolder.getBean(PROPERTIES_MANAGE_BEAN);
		return properties.getString(SEND_MSG_FLAG);
	};
	
	/**
	 * @功能描述：根据属性名称获取配置文件中的属性值
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年3月2日下午3:17:47
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyValue(String propertyName) {
		PropertiesManager properties = SpringContextHolder.getBean(PROPERTIES_MANAGE_BEAN);
		return properties.getString(propertyName);
	};
	
	/**
	 * 格式化金额
	 * 
	 * @Title: formatDouble
	 * @author joy.zhang
	 * @date 2016年3月4日 上午9:49:15
	 * @param d
	 * @return
	 * @return String
	 */
	public static String formatDouble(Double d) {
		if (d != null) {
			DecimalFormat fmt = new DecimalFormat("¥###,###,##0.00");
			
			return fmt.format(d);
		}
		return "¥0.00";
		
	}
	
	/**
	 * 默认日期格式日期 yyyy-MM-dd HH:mm:ss
	 */
	private static String	dateRegx			= "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 提成比例
	 */
	public static String	DEDUCT_A_PERCENTAGE	= "deductAPercentage";
	
	/**
	 * 格式化日期
	 * 
	 * @Title: formateDate
	 * @author joy.zhang
	 * @date 2016年3月7日 下午3:31:48
	 * @param str
	 * @return
	 * @return Date
	 */
	public static Date formateDate(String str, String regx) {
		
		try {
			if (str != null) {
				if (regx == null) {
					regx = dateRegx;
				}
				SimpleDateFormat sf = new SimpleDateFormat(regx);
				return sf.parse(str);
			}
		} catch (ParseException e) {
		}
		return null;
	}
	
	/**
	 * @功能描述：调用接口并获取map类型的回参
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年6月26日下午4:32:42
	 * @param name
	 *            系统常量名称
	 * @param interfaceName
	 *            接口常量名称
	 * @param url
	 *            请求地址常量名称
	 * @param param
	 *            接口请求参数
	 * @return
	 */
	public static Map<String, Object> doPost(String name, String interfaceName, String url, String param, int timeOut) {
		// 系统名称
		String systemName = ConstantsMshop.getPropertyValue(name);
		// 商品分类接口名
		String interfaceProductCat = interfaceName;
		// 茶帮通链接地址
		String systemUrl = ConstantsMshop.getPropertyValue(url);
		// 商品分类接口入参
		String catParam = param;
		return doPostInterface(systemName, interfaceProductCat, systemUrl, catParam, timeOut);
	}
	
	/**
	 * @功能描述：调用接口并获取map类型的回参
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年3月2日下午4:35:12
	 * @param systemName系统名称
	 * @param interfaceProductCat接口名称
	 * @param cbtUrl接口所在服务器地址
	 * @param param入参
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> doPostInterface(String systemName, String interfaceProductCat, String cbtUrl, String param, int timeOut) {
		// 接口名称
		String interfaceName = systemName + interfaceProductCat;
		// 接口链接地址
		String url = cbtUrl + ConstantsMshop.getPropertyValue(interfaceName);
		// post访问接口
		String str = HttpClientUtil.post(url, "requestData", param, timeOut);
		// 返回参数
		JSONObject returnParam = JSONObject.fromObject(str);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (returnParam != null) {
			// 转换为map对象
			returnMap = (Map<String, Object>) returnParam;
		}
		return returnMap;
	}
	
	/**
	 * 将object 转换成json 串
	 * 
	 * @Title: debugInfo
	 * @author joy.zhang
	 * @date 2016年3月8日 下午7:33:01
	 * @param ob
	 *            参数
	 * @return
	 * @return String
	 */
	public static String objectToJsonString(Object ob) {
		HibernatePropertyFilter filter = new HibernatePropertyFilter();
		SerializeWriter sw = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(sw);
		serializer.getPropertyFilters().add(filter);
		serializer.write(ob);
		return sw.toString();
		
	}
	
	/**
	 * 调用 API
	 * 
	 * @param parameters
	 * @return
	 */
	public static String post(String url, String parameters, String contentType) {
		
		HttpPost post = new HttpPost(url);
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			StringEntity s = new StringEntity(parameters);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				return result;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return "{}";
	}
	
	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100 用户真实IP为： 192.168.1.110
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 将vo 实体类转成 stirng 类型参数
	 * 
	 * @Title: toParams
	 * @author joy.zhang
	 * @date 2016年3月15日 下午2:07:08
	 * @param ob
	 *            需要转换的实体
	 * @param key
	 *            可以参数
	 * @param isChange
	 *            结果 是否转成成下划线格式 true 转换 false 不转
	 * @return String
	 */
	public static String toParams(Object ob, String key, boolean isChange) {
		
		Field[] fields = ob.getClass().getDeclaredFields();
		String[] st = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			st[i] = fields[i].getName();
		}
		// 排序
		Arrays.sort(st);
		
		String params = "";
		try {
			for (int i = 0; i < st.length; i++) {
				String s = st[i];
				if (s != null && s.length() > 1) {
					s = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
				} else if (s != null) {
					s = s.toUpperCase();
				}
				// 获得方法
				Method m;
				m = ob.getClass().getDeclaredMethod("get" + s);
				// 执行方法
				Object o = m.invoke(ob);
				if (o != null) {
					if (isChange) {
						params += camel4underline(st[i]) + "=" + o + "&";
					} else {
						params += st[i] + "=" + o + "&";
					}
				}
				
			}
		} catch (Exception e) {
		}
		
		if (key != null) {
			params += "key=" + key;
		} else {
			if (params != null && params.endsWith("&")) {
				params = params.substring(0, params.length() - 1);
			}
		}
		return params;
	}
	
	/**
	 * 驼峰 转下划线 appId ==> app_id
	 * 
	 * @Title: camel4underline
	 * @author joy.zhang
	 * @date 2016年3月15日 下午1:35:10
	 * @param param
	 * @return
	 * @return String
	 */
	public static String camel4underline(String param) {
		Pattern p = Pattern.compile("[A-Z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher mc = p.matcher(param);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
			i++;
		}
		
		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}
	
	/**
	 * sku属性缓存 List<ProductProperty>
	 */
	public static final String	CACHE_PROPERTIES				= "cache_properties";
	
	/**
	 * sku所属商品的属性
	 */
	public static final String	CACHE_PROPERTIES_FOR_PRODUCT	= "cache_properties_for_product";
	
	/**
	 * sku属性缓存 Map<String, List<ProductProperty>>
	 */
	public static final String	SKU_PURPOSE_PROPERTIES			= "sku_purpose_properties";
	/**
	 * 字典类型缓存 List<Dictionary>
	 */
	public static final String	CACHE_DICTIONARY				= "cache_dictionary";
	/**
	 * 过期时间缓存 List<ProductUsefulLife>
	 */
	public static final String	CACHE_USE_FUL_LIFE				= "cache_use_ful_life";
	
	/**
	 * 字典数据集 Map<Long, List<DictionaryData>>
	 */
	public static final String	CACHE_DICTIONARY_VALUE			= "cache_dictionary_value";
	
	/**
	 * 字典数据值Map<Long, DictionaryData>
	 */
	public static final String	CACHE_DICTIONARY_DATA			= "cache_dictionary_data";
	/**
	 * 商品属性缓存
	 */
	public static final String	CACHE_PRODUCT_PROPERTIES		= "cache_proProperties";
	/**
	 * sku属性值缓存 Map<String, ProductPropertyValue> key的值 skuId+"_"+dictId
	 */
	public static final String	CACHE_SKU_PROPERTITY_VALUE		= "cache_sku_propertity_value";
	
	/**
	 * sku所属图片
	 */
	public static final String	CACHE_SKU_IMAGE					= "CACHE_SKU_IMAGE";
	
	/**
	 * sku缓存
	 */
	public static final String	CACHE_SKU						= "CACHE_SKU";
	
	/**
	 * 红包使用配置
	 */
	public static final String	CACHE_REDPACKET_MANAGE			= "cache_redpacket_manage";
	/**
	 * 红包规则
	 */
	public static final String	CACHE_REDPACKET_RULE			= "cache_redpacket_rule";
	/**
	 * 规格属性主键
	 */
	public static final Long	DICTIONARY_SPAC					= 12l;
	/**
	 * 整散属性主键
	 */
	public static final Long	DICTIONARY_WHOLE				= 10l;
	/**
	 * 规格属性年份
	 */
	public static final Long	DICTIONARY_YEAR					= 3l;
	/**
	 * 整散属性 批次
	 */
	public static final Long	DICTIONARY_PATCH				= 4l;
	/**
	 * 生熟
	 */
	public static final Long	DICTIONARY_DEPT					= 11L;
	
	/**
	 * 茶帮通 创建红包口地址
	 */
	public static final String	CBT_CREATE_RED_PACKET_URL		= "_create_red_packet_url";
	
	/**
	 * @函数名: formatAmount
	 * @功能描述: 格式化金额
	 * @作者 : wuyicheng
	 * @创建时间 : 2016年9月13日上午9:40:57
	 * @return String
	 */
	public static String formatAmount(Double d) {
		if (d != null) {
			DecimalFormat fmt = new DecimalFormat("########0.00");
			
			return fmt.format(d);
		}
		return "0.00";
		
	}
}
