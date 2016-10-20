package com.cat.test;

import com.cat.dao.RoleDao;
import com.cat.dao.UserDao;
import com.cat.entity.Role;
import com.cat.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simple {

	static UserDao userDao = new UserDao();
	static RoleDao roleDao = new RoleDao();

	public static void main(String[] args) {
//		find();
//		save();
//		System.out.println(userDao.find("zsy32"));

//		userDao.delete(20);
		List<User> list = userDao.findList("zsy");
		list.forEach(System.out::println);
		System.out.println(list.size());
	}

	private static void find() {
		System.out.println(userDao.find(20));
	}

	private static void save() {
		User user = new User(0, "zsy" + new Random().nextInt(100), null);

		List<Role> roleList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Role role = new Role(0, user, "role" + i);
			roleList.add(role);
		}
		user.setRoleList(roleList);

		userDao.save(user);
	}
}
