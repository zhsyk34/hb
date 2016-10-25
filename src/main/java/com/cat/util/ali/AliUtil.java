package com.cat.util.ali;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zengXinChao
 * @date 2016年7月27日 上午11:18:16
 * @Description:支付宝相关接口
 */
public class AliUtil {	
	public static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	
	/**
	 * 
	 * @Title: getRedirectStr
	 * @Description: 收集支付参数 ，建立请求html
	 * @author: zengXinChao
	 * @date: 2016年7月27日 下午3:53:41
	 * @param out_trade_no商户订单号，商户网站订单系统中唯一订单号，必填
	 * @param subject订单名称，必填
	 * @param total_fee付款金额，必填
	 * @param body商品描述，可空
	 * @return void
	 */
	public static String getRedirectStr(String out_trade_no, String subject, String total_fee, String body){
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
		sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url);
		sParaTemp.put("return_url", AlipayConfig.return_url);
		sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
        //如sParaTemp.put("参数名","参数值");
		
		//建立请求
		String sHtmlText = AliUtil.buildRequest(sParaTemp, "get", "确认");
		
		return sHtmlText;
	}

	/**
	 * 
	 * @Title: buildRequest
	 * @Description: 根据参数建立请求html
	 * @author: zengXinChao
	 * @date: 2016年7月27日 下午3:58:40
	 * @param sParaTemp
	 * @param strMethod
	 * @param strButtonName
	 * @return
	 * @return String
	 */
	public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName) {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form style=\"display:none;\" id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW
                      + "_input_charset=" + AlipayConfig.input_charset + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input id=\"alipaysubmitBtn\" type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");

        return sbHtml.toString();
    }
	
	/**
	 * 
	 * @Title: buildRequestPara
	 * @Description: 处理请求参数
	 * @author: zengXinChao
	 * @date: 2016年7月27日 下午3:59:06
	 * @param sParaTemp
	 * @return
	 * @return Map<String,String>
	 */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayConfig.sign_type);

        return sPara;
    }
	
	public static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(AlipayConfig.sign_type.equals("RSA") ){
        	mysign = RSA.sign(prestr, AlipayConfig.private_key, AlipayConfig.input_charset);
        }
        return mysign;
    }
}
