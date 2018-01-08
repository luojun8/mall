package com.bjtu.item.entity;

public class User {
	private Long id;
	private String name;
	private String password;
	private Byte role;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Byte getRole() {
		return role;
	}

	public void setRole(Byte role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User{" +
				" id=" + id +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}