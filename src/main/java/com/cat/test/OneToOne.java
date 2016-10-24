package com.cat.test;

import com.cat.dao.PersonDao;
import com.cat.entity.Card;
import com.cat.entity.Person;

public class OneToOne {

	private static PersonDao personDao = new PersonDao();

	public static void main(String[] args) {
		Card card = new Card(0, "353302");
		Person person = new Person("yes", card);

		personDao.save(person);

	}
}
