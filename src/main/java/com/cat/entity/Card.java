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
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String no;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "personId", referencedColumnName = "id", unique = true, foreignKey = @ForeignKey(name = "person_card"))
//	@org.hibernate.annotations.ForeignKey(name = "person_card")
	private Person person;

	@Override
	public String toString() {
		return "Card{" +
				"id=" + id +
				", no='" + no + '\'' +
				'}';
	}
}
