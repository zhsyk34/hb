package com.cat.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

	@Id
	@GeneratedValue
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Role> roleList;
}
