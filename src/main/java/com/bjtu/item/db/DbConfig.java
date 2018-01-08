package com.bjtu.item.db;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties
class DbConfig {
	private List<DbGroup> dbGroups;

	public List<DbGroup> getDbGroups() {
		return dbGroups;
	}

	public void setDbGroups(List<DbGroup> dbGroups) {
		this.dbGroups = dbGroups;
	}
}
