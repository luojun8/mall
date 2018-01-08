package com.bjtu.item.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SessionFactoryInit {

	@Bean
	public DbConfig dbConfig() {
		return new DbConfig();
	}

	@Bean
	public SessionFactory sessionFactory(DbConfig config) {
		return new SessionFactory(config);
	}
}
