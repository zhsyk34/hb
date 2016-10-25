package com.cat.util;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;

/**
 * @文件:ZPMySQLDialect.java
 * @说明:醉品自定义方言
 * @版权：醉品春秋 版权所有 (c) 2016
 * @作者:cjj
 * @创建日期：2016年2月17日
 */
public class ZPMySQLDialect extends MySQLDialect {
	public ZPMySQLDialect() {
		super();
		registerHibernateType(Types.DECIMAL, Hibernate.BIG_DECIMAL.getName());
		registerHibernateType(-1, Hibernate.STRING.getName());
	}
}