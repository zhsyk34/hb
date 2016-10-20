package com.cat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class Role {
	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	private String name;

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id + ", " +
				"name=" + name +
				'}';
	}
}
