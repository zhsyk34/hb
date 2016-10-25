package com.cat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zuipin.exception.BussniseException;
import com.zuipin.util.MD5;
import com.zuipin.util.PropertiesManager;
import com.zuipin.util.RandomUtil;
import com.zuipin.util.SpringContextHolder;
import com.zuipin.util.StringUtils;
import com.zuipin.util.XStream;

/**
 * @author zengXinChao
 * @date 2016年3月14日 下午1:52:54
 * @Description: 微信 工具
 */
public class WechatUtil {
	
	/** 获取code的URL */
	public static final String		URL_FOR_CODE;
	
	/** scope参数snsapi_base */
	public static final String		SNSAPI_BASE;
	
	/** scope参数snsapi_userinfo */
	public static final String		SNSAPI_USERINFO;
	
	/** 获取access_token的URL */
	public static final String		URL_FOR_ACCESS_TOKEN;
	
	/** 获取userinfo的URL */
	public static final String		URL_FOR_USERINFO;
	
	/** refresh_token URL */
	public static final String		URL_FOR_REFRESH_TOKEN;
	
	/** 获取jssdk的ACCESS_TOKEN URL */
	public static final String		URL_FOR_JSSDK_ACCESS_TOKEN;
	
	/** 检查access_token的URL */
	public static final String		URL_FOR_CHECK_TOKEN;
	
	/** 获取Jsapi Ticket的URL */
	public static final String		URL_FOR_JSAPITICKET;
	/**
	 * 微信 统一下单接口
	 */
	public static final String		URL_FOR_UNIFIED_ORDER;
	
	public static final String		URL_FOR_JSTICKET;
	/**
	 * propertiesManager
	 */
	public static PropertiesManager	propertiesManager;
	
	public static Logger			log	= Logger.getLogger(WechatUtil.class);
	
	static {
		String temp1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
		String temp2 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		String temp3 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
		String temp4 = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		
		propertiesManager = (PropertiesManager) SpringContextHolder.getApplicationContext().getBean("propertiesManager");
		String appid = propertiesManager.getString("wechat_appid");
		String secret = propertiesManager.getString("wechat_secret");
		
		URL_FOR_CODE = temp1.replace("APPID", appid);
		URL_FOR_ACCESS_TOKEN = temp2.replace("APPID", appid).replace("SECRET", secret);
		URL_FOR_REFRESH_TOKEN = temp3.replace("APPID", appid);
		URL_FOR_JSSDK_ACCESS_TOKEN = temp4.replace("APPID", appid).replace("APPSECRET", secret);
		
		URL_FOR_JSTICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		URL_FOR_USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		URL_FOR_CHECK_TOKEN = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
		URL_FOR_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		URL_FOR_JSAPITICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		SNSAPI_BASE = "snsapi_base";
		SNSAPI_USERINFO = "snsapi_userinfo";
	}
	
	/**
	 * @Title: comeFromWechat
	 * @Description: 判断请求是否来自微信浏览器
	 * @author: zengXinChao
	 * @date: 2016年3月14日 下午1:53:21
	 * @param request
	 * @return
	 * @return boolean
	 */
	public static boolean comeFromWechat(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		// 添加小写获取
		if (userAgent == null) {
			userAgent = request.getHeader("user-agent");
		}
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
		}
		// 添加判空
		if (userAgent != null && userAgent.indexOf("micromessenger") >= 0) {
			return true;
		}
		return false;
	}
	
	public static JSONObject getJsApiTicket(String accessToken) {
		String urlForJsTicket = WechatUtil.URL_FOR_JSAPITICKET.replace("ACCESS_TOKEN", accessToken.toString());
		return RequestUtil.doHttpsRequest(urlForJsTicket, "GET", null);
	}
	
	/**
	 * @Title: generateUrlForBase
	 * @Description: 生成获取code的url
	 * @author: zengXinChao
	 * @date: 2016年3月21日 下午1:55:12
	 * @param request
	 * @param scope
	 *            snsapi_base或snsapi_userinfo
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return String
	 */
	public static String generateUrlForCode(HttpServletRequest request, String scope) throws UnsupportedEncodingException {
		StringBuffer url = new StringBuffer();
		url.append(propertiesManager.getString("itempath"));
		url.append(request.getServletPath());
		
		String queryStr = request.getQueryString();
		if (StringUtils.isNotBlank(queryStr)) {
			url.append("?").append(queryStr);
		}
		
		String encodeUrl = URLEncoder.encode(url.toString(), "utf-8");
		
		return WechatUtil.URL_FOR_CODE.replace("REDIRECT_URI", encodeUrl).replace("SCOPE", scope);
	}
	
	/**
	 * @Title: getOpenid
	 * @Description: 根据code获取openid
	 * @author: zengXinChao
	 * @date: 2016年3月15日 上午10:10:46
	 * @param code
	 * @return
	 * @return String
	 */
	public static String getOpenid(String code) {
		JSONObject accessToken = WechatUtil.getAccessToken(code);
		
		if (accessToken != null && accessToken.containsKey("openid")) {
			return accessToken.getString("openid");
		}
		return null;
	}
	
	/**
	 * @Title: checkAccessToken
	 * @Description: 检查accessToken是否可用
	 * @author: zengXinChao
	 * @date: 2016年3月22日 下午2:57:30
	 * @param accessToken
	 * @param openid
	 * @return
	 * @return boolean
	 */
	public static boolean checkAccessToken(String accessToken, String openid) {
		String urlForCheck = WechatUtil.URL_FOR_CHECK_TOKEN.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
		JSONObject result = RequestUtil.doHttpsRequest(urlForCheck, "GET", null);
		
		if (result.containsKey("errcode") && result.getString("errcode").equals("0")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * @Title: getAccessToken
	 * @Description: 获取access_token
	 * @author: zengXinChao
	 * @date: 2016年3月21日 下午5:06:47
	 * @param code
	 * @return
	 * @return JSONObject
	 */
	public static JSONObject getAccessToken(String code) {
		String urlForAccessToken = WechatUtil.URL_FOR_ACCESS_TOKEN.replace("CODE", code);
		return RequestUtil.doHttpsRequest(urlForAccessToken, "GET", null);
	}
	
	/**
	 * @功能描述：获取jssdk 的accessToken
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年7月18日上午10:15:52
	 * @return
	 */
	public static JSONObject getJSSDKAccessToken() {
		String urlForAccessToken = WechatUtil.URL_FOR_JSSDK_ACCESS_TOKEN;
		return RequestUtil.doHttpsRequest(urlForAccessToken, "GET", null);
	}
	
	/**
	 * @Title: refreshToken
	 * @Description: 刷新refresh_token
	 * @author: zengXinChao
	 * @date: 2016年3月21日 下午5:36:23
	 * @param refreshToken
	 * @return
	 * @return JSONObject
	 */
	public static JSONObject refreshToken(String refreshToken) {
		String urlForRefreshToken = WechatUtil.URL_FOR_REFRESH_TOKEN.replace("REFRESH_TOKEN", refreshToken);
		return RequestUtil.doHttpsRequest(urlForRefreshToken, "GET", null);
	}
	
	/**
	 * @Title: getUserinfo
	 * @Description: 根据access_token和openid获取userinfo
	 * @author: zengXinChao
	 * @date: 2016年3月21日 下午5:31:40
	 * @param accessToken
	 * @param openid
	 * @return
	 * @return JSONObject
	 */
	public static JSONObject getUserinfo(String accessToken, String openid) {
		String urlForUserinfo = WechatUtil.URL_FOR_USERINFO.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
		return RequestUtil.doHttpsRequest(urlForUserinfo, "GET", null);
	}
	
	/**
	 * @功能描述：获取js签名
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年7月18日上午9:40:02
	 * @param accessToken
	 * @return
	 */
	public static JSONObject getJsapiTicket(String accessToken) {
		String urlForJsapiticket = WechatUtil.URL_FOR_JSAPITICKET.replace("ACCESS_TOKEN", accessToken);
		return RequestUtil.doHttpsRequest(urlForJsapiticket, "GET", null);
	}
	
	/**
	 * 调用微信统一支付接口
	 * 
	 * @Title: callUnifiedOrder
	 * @author joy.zhang
	 * @date 2016年3月15日 下午4:26:35
	 * @param payAmount
	 *            支付金额 单位元
	 * @param orderNo
	 *            订单号
	 * @param notifyUrlkey
	 *            回调url key
	 * @param openId
	 *            微信用户标识
	 * @param createIp
	 *            微信用户Ip
	 * @param proDes
	 *            商品描述
	 * @return
	 * @return JSONObject
	 * @throws Exception
	 */
	public static JSONObject callUnifiedOrder(Double payAmount, String orderNo, String notifyUrlkey, String openId, String createIp, String proDes) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String appid = propertiesManager.getString("wechat_appid");
		// appid 公众号Id
		map.put("appid", appid);
		// mch_id 商户号
		map.put("mch_id", propertiesManager.getString("wechat_mchid"));
		// 随机字符串 32位以内
		map.put("nonce_str", RandomUtil.createRandom(30));
		// openid 微信客户openId
		map.put("openid", openId);
		// 回调url
		try {
			map.put("notify_url", propertiesManager.getString(notifyUrlkey));
		} catch (Exception e) {
		}
		
		// orderNo 订单编号
		map.put("out_trade_no", orderNo);
		
		// total_fee 金额 单位 转成 分
		map.put("total_fee", Double.valueOf((payAmount * 100)).intValue());
		
		// createIp 请求客户端Id
		map.put("spbill_create_ip", createIp);
		// body 简要描述
		if (proDes != null) {
			if (proDes.length() > 128) {
				map.put("body", proDes.substring(0, 128));
				
			} else {
				map.put("body", proDes);
			}
		} else {
			// 必填 不能为空
			map.put("body", "order");
		}
		
		// trade_type 交易类型
		map.put("trade_type", "JSAPI");
		
		// 公众号 后台 支付设置的秘钥
		String key = propertiesManager.getString("wechat_pay_key");
		// 将参数内容组装成xml
		StringBuffer xml = new StringBuffer();
		xml.append("<xml>");
		for (Entry<String, Object> ent : map.entrySet()) {
			xml.append("<" + ent.getKey() + ">" + ent.getValue() + "</" + ent.getKey() + ">");
		}
		// 生成签名
		xml.append("<sign>" + createSign(map, key) + "</sign>");
		xml.append("</xml>");
		// 获取返回的xml
		String resutl = RequestUtil.doRequest(URL_FOR_UNIFIED_ORDER, "POST", xml.toString(), null);
		JSONObject jsOb = null;
		if (resutl != null) {
			jsOb = JSONObject.fromObject(XStream.xml2JSON(resutl));
			String str = jsOb.getString("return_code");
			if (str != null && "SUCCESS".equals(str.trim())) {
				str = jsOb.getString("result_code");
				if (str != null && "SUCCESS".equals(str.trim())) {
					String prepayId = jsOb.getString("prepay_id");// 获得预支付Id
					if (prepayId != null && !"".equals(prepayId.trim())) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put("appId", appid);// 公众号ID
						resultMap.put("timeStamp", Long.toString(new Date().getTime()));// 时间戳
						resultMap.put("nonceStr", RandomUtil.createRandom(30));// 随机数 32位以内
						resultMap.put("package", "prepay_id=" + prepayId + "");// 预支付Id
						resultMap.put("signType", "MD5");// 加密算法 目前只支持MD5
						resultMap.put("paySign", createSign(resultMap, key));// 生成签名
						resultMap.put("orderNo", orderNo);// 返回订单编号
						resultMap.put("payAmount", payAmount); // 返回支付金额
						log.debug("resultMap:" + resultMap);
						return JSONObject.fromObject(resultMap);
					}
				}
			}
		}
		// 打印请求和返回值
		log.error("request url:" + URL_FOR_UNIFIED_ORDER);
		log.error("request params:" + xml.toString());
		log.error("response resutl:" + resutl);
		throw new BussniseException("创建订单失败 !");
		
	}
	
	/**
	 * 将参数 加key 后生成 签名
	 * 
	 * @Title: createSign
	 * @author joy.zhang
	 * @date 2016年3月16日 上午9:49:37
	 * @param map
	 *            参数
	 * @param key
	 * @return
	 * @return String 微信签名
	 */
	public static String createSign(Map<String, Object> map, String key) {
		
		// 通过ArrayList构造函数把map.entrySet()转换成list
		List<Entry<String, Object>> mappingList = new ArrayList<Entry<String, Object>>(map.entrySet());
		
		// 通过比较器实现比较 排序
		Collections.sort(mappingList, new Comparator<Entry<String, Object>>() {
			public int compare(Entry<String, Object> mapping1, Entry<String, Object> mapping2) {
				return mapping1.getKey().compareTo(mapping2.getKey());
			}
		});
		// 组装成字符串
		String str = "";
		for (Entry<String, Object> ent : mappingList) {
			str += ent.getKey() + "=" + ent.getValue() + "&";
		}
		// MD5 加密生成签名
		String sign = MD5.caiBeiMD5(str + "key=" + key);
		return sign;
	}
}
