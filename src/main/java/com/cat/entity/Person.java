package com.cat.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {
	private long id;
	private String name;
	private Card card;

	public Person(String name, Card card) {
		this.name = name;
		this.card = card;
	}
}
