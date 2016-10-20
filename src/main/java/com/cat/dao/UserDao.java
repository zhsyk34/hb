package com.cat.dao;

import com.cat.entity.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UserDao extends BaseDao<User> {

	public User find(String name) {
		Session session = super.session();
		Criteria criteria = session.createCriteria(User.class);
		Criterion criterion = Restrictions.eq("name", name);
		criteria.add(criterion);

		return (User) criteria.uniqueResult();
	}

	public List<User> findList(String name) {
		Session session = super.session();
		Criteria criteria = session.createCriteria(User.class);
		Criterion criterion = Restrictions.like("name", "%" + name + "%");
		criteria.add(criterion);

		return criteria.list();
	}
}
