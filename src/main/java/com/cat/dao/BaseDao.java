package com.cat.dao;

import com.cat.test.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public class BaseDao<T> {

	private Class<T> clazz;

	protected Session session() {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.openSession();
		return session;
	}

	public BaseDao() {
		clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void save(T t) {
		Session session = session();
		session.beginTransaction();
		session.save(t);
		session.getTransaction().commit();
	}

	public void saves(Collection<T> collection) {
		Session session = session();
		session.beginTransaction();
		collection.forEach(t -> session.save(t));
		session.getTransaction().commit();
	}

	public void delete(long id) {
		Session session = session();
		session.beginTransaction();
		session.delete(session.get(clazz, id));
		session.getTransaction().commit();
	}

	public T find(long id) {
		return (T) session().get(clazz, id);
	}

	public List<T> findList() {
		return (List<T>) session().get(clazz, null);
	}
}
