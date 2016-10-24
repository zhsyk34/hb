package com.cat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class Role {
	//	@Id
//	@GeneratedValue
	private long id;

//	@ManyToOne
//	@JoinColumn(name = "userId")

	private String name;

	private User user;

	public Role(String name, User user) {
		this.name = name;
		this.user = user;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id + ", " +
				"name=" + name +
				'}';
	}
}
