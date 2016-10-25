package com.cat.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * @author zengXinChao
 * @date 2016年3月15日 上午10:03:40
 * @Description:请求工具
 */
public class RequestUtil {
	public static Logger		log			= Logger.getLogger(RequestUtil.class);
	/**
	 * 默认超时时间 默认30 秒
	 */
	public static final Integer	TIME_OUT	= 30 * 1000;
	
	/**
	 * @Title: doHttpsRequest
	 * @Description: 创建请求，获取返回的json数据
	 * @author: zengXinChao
	 * @date: 2016年3月17日 下午4:26:49
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 * @return JSONObject
	 */
	public static JSONObject doHttpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			// conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("连接超时：", ce);
		} catch (Exception e) {
			log.error("https请求异常：", e);
		}
		return jsonObject;
	}
	
	/**
	 * 返回String 类型数据
	 * 
	 * @Title: doRequest
	 * @author joy.zhang
	 * @date 2016年3月15日 下午6:07:33
	 * @param requestUrl
	 *            调用url
	 * @param requestMethod
	 *            调用方法
	 * @param params
	 *            参数
	 * @param timeOut
	 *            超时时间 默认30秒
	 * @return
	 * @return String
	 */
	public static String doRequest(String requestUrl, String requestMethod, String params, Integer timeOut) {
		
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			if (timeOut == null) {
				timeOut = TIME_OUT;
			}
			conn.setConnectTimeout(timeOut);
			conn.setReadTimeout(timeOut);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			// conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != params) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(params.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			log.error("连接超时：", ce);
		} catch (Exception e) {
			log.error("https请求异常：", e);
		}
		return null;
	}
}
