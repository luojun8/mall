package com.bjtu.item.db;

import com.bjtu.item.utils.LoggerUtil;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionFactory {
	private static Logger logger = LoggerFactory.getLogger(SessionFactory.class);
	private Map<String, GroupInfo> sessionGroups;

	public SessionFactory(DbConfig config) {
		if(config.getDbGroups() != null) {
			sessionGroups = new HashMap<>();
			Iterator<DbGroup> dbGroupIterator = config.getDbGroups().iterator();
			while (true) {
				DbGroup g;
				do {
					if(!dbGroupIterator.hasNext()) {
						return;
					}
					g = dbGroupIterator.next();
				} while (null == g.getList());

				List<SqlSessionFactory> factories = new LinkedList<>();
				Iterator<DbProp> dbPropIterator = g.getList().iterator();

				while (dbPropIterator.hasNext()) {
					factories.add(createSessionFactory(dbPropIterator.next()));
				}
				sessionGroups.put(g.getName(), new SessionFactory.GroupInfo(g.getName(), factories));
			}
		} else
			LoggerUtil.error(logger, "config.getDbGroups() is null");
	}

	private static SqlSessionFactory createSessionFactory(DbProp dbProp) {
		PooledDataSource dataSource = new PooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://" + dbProp.getHost() + ":" + dbProp.getPort() + "/" + dbProp.getName() + "?userUnicode=true&characterEncoding=utf8&useAffectedRows=true", dbProp.getUser(), dbProp.getPassword());
		if (null != dbProp.getPoolMaximumActiveConnections())
			dataSource.setPoolMaximumActiveConnections(dbProp.getPoolMaximumActiveConnections());
		if (null != dbProp.getPoolMaximumIdleConnections()) {
			dataSource.setPoolMaximumIdleConnections(dbProp.getPoolMaximumIdleConnections());
		}

		dataSource.setPoolPingEnabled(true);
		dataSource.setPoolPingQuery("select 1");
		dataSource.setPoolPingConnectionsNotUsedFor(300000);
		dataSource.setPoolTimeToWait(5000);//获取连接超时就重新获取
		TransactionFactory transactionFactory = new JdbcTransactionFactory();//事务处理
		Environment environment = new Environment(dbProp.getEnvironment(), transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		dbProp.getMappers().forEach(m -> configuration.addMappers(m));
		return new SqlSessionFactoryBuilder().build(configuration);
	}

	public SessionContext getSessionContext() {
		return getSessionContext(null);
	}

	public SessionContext getSessionContext(String name) {
		SessionFactory.GroupInfo g = sessionGroups.get(name == null ? DbGroup.defaultGroup : name);
		return g == null ? null : new SessionContext(getOneSlaveSession(g));
	}

	private SqlSession getOneSlaveSession(GroupInfo g) {
		return g.factories.isEmpty() ? null : g.factories.get(Math.abs(g.seq.getAndIncrement()) % g.factories.size()).openSession();
	}

	private static class GroupInfo {
		AtomicInteger seq = new AtomicInteger(0);
		String name;
		List<SqlSessionFactory> factories;
		GroupInfo(String name, List<SqlSessionFactory> factories) {
			this.factories = factories;
			this.name = name;
		}
	}
}
