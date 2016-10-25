package com.cat.test;

import com.cat.dao.RoleDao;
import com.cat.dao.UserDao;
import com.cat.entity.Role;
import com.cat.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class OneToMany {

	static UserDao userDao = new UserDao();
	static RoleDao roleDao = new RoleDao();

	public static void main(String[] args) {
//		find();
		save();
//		delete();
//		findList();
//		update();
	}

	private static void update() {
		User user = userDao.find(1);
		user.setName("go");
		user.setRoles(null);
		Set<Role> set = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			Role role = new Role("a" + i, user);
			set.add(role);
		}
//		user.setRoles(set);

		userDao.update(user);
	}

	private static void delete() {
		userDao.delete(1);
	}

	private static void findList() {
		List<User> list = userDao.findList("zsy");
		list.forEach(System.out::println);
		System.out.println(list.size());
	}

	private static void find() {

		User user = userDao.find(1);
		System.out.println(user);
		System.out.println(user.getRoles());

	}

	private static void save() {
		User user = new User(0, "zsy" + new Random().nextInt(100), null);

		Set<Role> roleList = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			Role role = new Role("role" + i, user);
			roleList.add(role);
		}
		user.setRoles(roleList);

		userDao.save(user);
	}

	private static void save1() {
		Role role = new Role();
		role.setName("haha");
		User user = new User(0, "cyy" + new Random().nextInt(100), null);
		role.setUser(user);

		roleDao.save(role);

	}
}
