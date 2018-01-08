package com.bjtu.item.db;


import com.bjtu.item.utils.LoggerUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SessionContext {
	private static Logger log = LoggerFactory.getLogger(SessionContext.class);
	private SqlSession session;

	public SessionContext(SqlSession s) {
		session = s;
		setUtf8mb4();
	}

	private void setUtf8mb4() {
		Connection c = this.session.getConnection();
		PreparedStatement ps = null;

		try {
			ps = c.prepareStatement("SET NAMES utf8mb4");
			ps.executeUpdate();
		} catch (SQLException var12) {
			LoggerUtil.error(log, "set utf8mb4 error");
		} finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException var11) {
					LoggerUtil.error(log, "closing statement for \"set utf8mb4\" error");
				}
			}

		}
	}

	public static void closeSilently(SessionContext ctx) {
		if(ctx != null) {
			rollbackSilently(ctx);
			try {
				ctx.close();
			} catch (Throwable var2) {
				LoggerUtil.error(log, "closeSilently", var2);
			}

		}
	}

	public static void rollbackSilently(SessionContext ctx) {
		if(ctx != null) {
			try {
				ctx.rollback();
			} catch (Throwable var2) {
				LoggerUtil.error(log, "rollbackSilently", var2);
			}

		}
	}

	public void commit() {
		if(session != null) {
			session.commit();
		}

	}

	public void rollback() {
		if(session != null) {
			session.rollback();
		}

	}

	public void close() {
		if(session != null) {
			session.close();
			session = null;
		}

	}

	public <T> T getMapper(Class<T> clazz) throws Exception {
		return session.getMapper(clazz);
	}
}
