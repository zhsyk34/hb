package com.cat.test;

import com.cat.entity.User;
import org.hibernate.*;

import java.util.List;

public class CacheTest {

	public static void main(String[] args) {
		Session session = HibernateUtil.session();
		Transaction transaction = session.beginTransaction();
		User user = new User();
		user.setName("abc");

		session.save(user);
		LockMode lockMode = session.getCurrentLockMode(user);
		System.out.println(lockMode);
		System.out.println("-------------");

		User user2 = (User) session.get(User.class, user.getId());
		System.out.println(user2.getName());

		String hql = "from User";
		Query query = session.createQuery(hql);
		List<User> list = query.list();
		list.forEach(System.out::println);
		transaction.commit();
		session.close();

		session.lock(user, LockMode.NONE);
		query.setLockMode("my", LockMode.WRITE);
		Criteria criteria = session.createCriteria(User.class);
		criteria.setLockMode("you", LockMode.READ);
	}
}
