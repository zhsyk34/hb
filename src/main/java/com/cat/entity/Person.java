package com.cat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Person {
	@Id
	@GeneratedValue
	private long id;
	private String name;

	@OneToOne(mappedBy = "person", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Card card;

	public Person(String name, Card card) {
		this.name = name;
		this.card = card;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
