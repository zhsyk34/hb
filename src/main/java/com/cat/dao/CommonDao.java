package com.cat.dao;

import org.springframework.orm.hibernate4.HibernateTemplate;

import java.lang.reflect.ParameterizedType;

public class CommonDao<T> {

	private Class<T> clazz;

	private HibernateTemplate hibernateTemplate;

	public CommonDao() {
		clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void save(T t) {
		hibernateTemplate.save(t);
	}

	public T find(long id) {
		return (T) hibernateTemplate.get(clazz, id);
	}
}
