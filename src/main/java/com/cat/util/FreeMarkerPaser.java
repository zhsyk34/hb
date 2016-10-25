package com.cat.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.zuipin.utils.FreeMarkerUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @文件:FreeMarkerPaser.java
 * @说明:freemarker解析器
 * @版权：醉品春秋 版权所有 (c) 2016
 * @作者:Eminem_chan 陈亚强
 * @创建日期：2016年2月17日
 */
public final class FreeMarkerPaser {
	
	private static final Log	log	= LogFactory.getLog(FreeMarkerPaser.class);
	/*
	 * freemarker data model 通过putData方法设置模板中的值
	 */
	private Map<String, Object>	data;
	/*
	 * 页面所在文件夹
	 */
	private String				pageFolder;
	
	// 文件名
	private String				pageName;
	
	public FreeMarkerPaser() {
		data = new HashMap<String, Object>();
		this.pageFolder = null;
	}
	
	public FreeMarkerPaser(String pageFolder) {
		this.pageFolder = pageFolder;
		data = new HashMap<String, Object>();
	}
	
	private Configuration getCfg() throws IOException {
		Configuration cfg = FreeMarkerUtil.getCfg();
		if (pageFolder != null) {
			cfg.setServletContextForTemplateLoading(ServletActionContext.getServletContext(), "/" + pageFolder);
		}
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(java.util.Locale.CHINA);
		cfg.setEncoding(java.util.Locale.CHINA, "UTF-8");
		return cfg;
	}
	
	public void putData(String key, Object value) {
		if (key != null && value != null)
			data.put(key, value);
	}
	
	public void putData(Map map) {
		if (map != null)
			data.putAll(map);
	}
	
	public Object getData(String key) {
		if (key == null)
			return null;
		
		return data.get(key);
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	/**
	 * 功能描述：解析
	 * 
	 * @author : Eminem_chan 陈亚强
	 * @param expressId
	 * @createdTime : 2014年10月29日上午10:04:00
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String proessPageContent() {
		try {
			Configuration cfg = this.getCfg();
			Template temp = null;
			if (pageName != null) {
				temp = cfg.getTemplate(pageName + ".html");
			} else {
				return "failed";
			}
			// ByteOutputStream stream = new ByteOutputStream();
			// ByteArrayOutputStream baops = new ByteArrayOutputStream();
			// Writer out = new OutputStreamWriter(baops);
			StringWriter sw = new StringWriter();
			temp.process(data, sw);
			sw.flush();
			String content = sw.toString();
			return content;
		} catch (Exception e) {
			log.error("template", e);
			e.printStackTrace();
		}
		return "processor error";
		
	}
	
	public String getPageFolder() {
		return pageFolder;
	}
	
	public void setPageFolder(String pageFolder) {
		this.pageFolder = pageFolder;
	}
	
	public String getPageName() {
		return pageName;
	}
	
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
}
