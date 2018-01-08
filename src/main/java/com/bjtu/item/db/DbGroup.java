package com.bjtu.item.db;

import java.util.List;


public class DbGroup {
	static final String defaultGroup = "default";
	private String name;
	private List<DbProp> list;

	public String getName() {
		return null == this.name ? defaultGroup : this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DbProp> getList() {
		return list;
	}

	public void setList(List<DbProp> list) {
		this.list = list;
	}
}
