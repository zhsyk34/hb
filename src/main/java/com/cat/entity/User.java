package com.cat.entity;

import lombok.*;

import java.util.Set;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class User {

	//	@Id
//	@GeneratedValue
	private long id;

	//	@Column(unique = true, nullable = false)
	private String name;

	//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Role> roles;

	public User(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
