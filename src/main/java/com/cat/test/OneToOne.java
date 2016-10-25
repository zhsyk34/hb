package com.cat.test;

import com.cat.entity.Card;
import com.cat.entity.Person;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class OneToOne {

	private Session session;
	private Transaction transaction;

	@Before
	public void before() {
		session = HibernateUtil.session();
		transaction = session.beginTransaction();
	}

	@After
	public void after() {
		transaction.commit();
		session.close();
	}

	@Test
	public void save() {
		Card card = new Card();
		card.setNo("350524");

		Person person = new Person();
		person.setName("cjj");

		card.setPerson(person);
//		person.setCard(card);

		Long id = (Long) session.save(card);
		System.out.println(id);
	}

	@Test
	public void find1() {
		Person person = (Person) session.get(Person.class, 1L);
		System.out.println(person);
		System.out.println(person.getCard());
	}

	@Test
	public void find2() {
		Card card = (Card) session.get(Card.class, 1L);
		System.out.println(card);
		System.out.println(card.getPerson());
	}

	@Test
	public void find3() {
		String sql = "SELECT id, name FROM person";
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List<Map<String, Object>> list = query.list();
		list.forEach((map) -> map.forEach((k, v) -> System.out.println(k + "-" + v)));
	}

	@Test
	public void find4() {
		String sql = "SELECT * FROM person";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(Person.class);
		//query.setResultTransformer(Criteria.ROOT_ENTITY);

		List<Person> list = query.list();
		list.forEach(System.out::println);
	}
}
