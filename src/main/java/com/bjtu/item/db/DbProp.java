package com.bjtu.item.db;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class DbProp {

	private static final AtomicInteger idx = new AtomicInteger(0);
	private String environment;
	private String host;
	private Integer port = Integer.valueOf(3306);
	private String name;
	private String user;
	private String password;
	private Integer poolMaximumActiveConnections;
	private Integer poolMaximumIdleConnections;
	private List<String> mappers;

	public String getEnvironment() {
		if(null == this.environment) {
			this.environment = "dev-" + idx.incrementAndGet();
		}
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPoolMaximumActiveConnections() {
		return poolMaximumActiveConnections;
	}

	public void setPoolMaximumActiveConnections(Integer poolMaximumActiveConnections) {
		this.poolMaximumActiveConnections = poolMaximumActiveConnections;
	}

	public Integer getPoolMaximumIdleConnections() {
		return poolMaximumIdleConnections;
	}

	public void setPoolMaximumIdleConnections(Integer poolMaximumIdleConnections) {
		this.poolMaximumIdleConnections = poolMaximumIdleConnections;
	}

	public List<String> getMappers() {
		return mappers;
	}

	public void setMappers(List<String> mappers) {
		this.mappers = mappers;
	}

	@Override
	public String toString() {
		return "DbProp{" +
				"environment='" + environment + '\'' +
				", host='" + host + '\'' +
				", port=" + port +
				", name='" + name + '\'' +
				", user='" + user + '\'' +
				", password='" + password + '\'' +
				", poolMaximumActiveConnections=" + poolMaximumActiveConnections +
				", poolMaximumIdleConnections=" + poolMaximumIdleConnections +
				", mappers=" + mappers +
				'}';
	}
}
