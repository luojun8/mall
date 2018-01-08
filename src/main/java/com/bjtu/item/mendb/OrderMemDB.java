package com.bjtu.item.mendb;

import com.bjtu.item.db.SessionContext;
import com.bjtu.item.db.SessionFactory;
import com.bjtu.item.db.mapper.OrderMapper;
import com.bjtu.item.entity.Order;
import com.bjtu.item.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class OrderMemDB {

    private static final Logger log = LoggerFactory.getLogger(OrderMemDB.class);

    private static SessionFactory sessionFactory;
    public static Map<Long, Order> orderMap = new ConcurrentHashMap<>();

    @Autowired
    private void inject(SessionFactory sessionFactory) {
        OrderMemDB.sessionFactory = sessionFactory;
        init();
    }

    private static void init() {
        SessionContext context = null;
        try {
            context = sessionFactory.getSessionContext();
            List<Order> orders = context.getMapper(OrderMapper.class).selectAll();
            orders.forEach(u -> {
                orderMap.put(u.getId(), u);
            });
        } catch (Exception e) {
            LoggerUtil.error(log, e);
        } finally {
            SessionContext.closeSilently(context);
        }
    }

    public static synchronized void addOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

}
