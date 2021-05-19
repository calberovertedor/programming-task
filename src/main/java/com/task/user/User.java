package com.task.user;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@XmlRootElement(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private Integer id;

	private String name;

	private String username;

	private String email;

	private Address address;

	private String phone;

	private String website;

	private Company company;

}
