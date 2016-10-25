package com.cat.util;

import freemarker.template.Configuration;

/**
 * @文件:FreeMarkerUtil.java
 * @说明:静态模版类
 * @版权：醉品春秋 版权所有 (c) 2016
 * @作者:cjj
 * @创建日期：2016年2月17日
 */
public class FreeMarkerUtil {
	
	public static Configuration getCfg() {
		
		Configuration cfg = new Configuration();
		cfg.setTemplateUpdateDelay(6000);
		cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
		return cfg;
	}
	
}
