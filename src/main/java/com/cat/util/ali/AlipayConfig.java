package com.cat.util.ali;

import com.zuipin.util.PropertiesManager;
import com.zuipin.util.SpringContextHolder;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	private static PropertiesManager	propertiesManager;
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "2088911156050662";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;

	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM0LKIZvpAXV5zuBBEYsR1JjJdRupUphUuzTPSzv2IYeCZvIJ/rXyM+AgaaivZ6CJeBzpCy8KUrCupkdfuZgXDt6iwe8uJZUYiY2s/W8yjPRi1p0E69rX33k9QMtemjlROqMQjMNmYTgvpKEvaz4Qg3x2oWQebnHTlwWbHyK9vt9AgMBAAECgYByjMpOGK60x+SP3KsLnQ8XWua2gD39iNOwFO83XKNFgiByFGcpf0XjjvJfqsludV09wL02dMRIzPM8OTs87xZ276Fo+IzgQDchXUrcJEToZXQmrU9lpSmVJdJsCSwCkHNVvOhpdg7xpX14GDJUcFHcfYSUYU6Bp8PRFlDYQV0ybQJBAPRDPTrmfKvAsYRFccsAnZeME/YAFF7rJ+FOe3JpcfjwOx9Tp+ikKFb8C3bNtrTrGhK/dQbi9ZQDDAEpQ+k7L58CQQDW5Xi6dRflZVyyg5fpsoImXBKtm6hGET4SFcnp0JcvTSdLqZ7LtYTkqajkLYePcjAgWRsVD2hUdEX28g2xLs9jAkEAg4tKiFPkRCLL9u3nMGoudJTtkwfL8Ko3mfDILn9275eAX4iP3ou5Zc7q5Zo3kQsjyYDuJz1G/+T8LAvrFMnZBQJAcxSACpoaKeaWNaLy/1txB0nXVtE81Aj3zIeOkX/69oHCwWocd8rFWA7jOw6GwfLMgwY00pplrJR3wz7sxi3jMQJAEet2Y29KdHV2y/qoapjqZyBJ/pTOkX6PyF4UY0L2XACVmWCwv9BPqRuiRWfQRLwdF5M+bcQCOlCyFtsmw2QtCQ==";
	
	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url;

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url;

	// 签名方式
	public static String sign_type = "RSA";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "C:\\";
		
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
		
	// 支付类型 ，无需修改
	public static String payment_type = "1";
		
	// 调用的接口名，无需修改
	public static String service = "create_direct_pay_by_user";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
//↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	// 防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数
	public static String anti_phishing_key = "";
	
	// 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
	public static String exter_invoke_ip = "";
		
//↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
	static {
		propertiesManager = (PropertiesManager) SpringContextHolder.getApplicationContext().getBean("propertiesManager");
		notify_url = propertiesManager.getString("ali_notify_url");
		return_url = propertiesManager.getString("ali_return_url");
	}
	
}

