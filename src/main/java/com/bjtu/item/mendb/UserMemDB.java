package com.bjtu.item.mendb;

import com.bjtu.item.db.SessionContext;
import com.bjtu.item.db.SessionFactory;
import com.bjtu.item.db.mapper.UserMapper;
import com.bjtu.item.entity.User;
import com.bjtu.item.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserMemDB {
	private static final Logger log = LoggerFactory.getLogger(UserMemDB.class);

	private static SessionFactory sessionFactory;
	public static Map<Long, User> userMap = new ConcurrentHashMap<>();
	public static Map<String, User> userNameMap = new ConcurrentHashMap<>();
	public static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

	@Autowired
	private void inject(SessionFactory sessionFactory) {
		UserMemDB.sessionFactory = sessionFactory;
		init();
	}

	private static void init() {
		SessionContext context = null;
		try {
			context = sessionFactory.getSessionContext();
			List<User> users = context.getMapper(UserMapper.class).selectAll();
			users.forEach(u -> {
				userMap.put(u.getId(), u);
				userNameMap.put(u.getName(), u);
			});
		} catch (Exception e) {
			LoggerUtil.error(log, e);
		} finally {
			SessionContext.closeSilently(context);
		}
	}

	public static synchronized void addUses(User user) {
		userMap.put(user.getId(), user);
		userNameMap.put(user.getName(), user);

	}
}