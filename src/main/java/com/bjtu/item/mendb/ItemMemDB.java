package com.bjtu.item.mendb;

import com.bjtu.item.db.SessionContext;
import com.bjtu.item.db.SessionFactory;
import com.bjtu.item.db.mapper.ItemMapper;
import com.bjtu.item.entity.Item;
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
public class ItemMemDB {

    private static final Logger log = LoggerFactory.getLogger(ItemMemDB.class);

    private static SessionFactory sessionFactory;
    public static Map<Long, Item> itemMap = new ConcurrentHashMap<>();

    @Autowired
    private void inject(SessionFactory sessionFactory) {
        ItemMemDB.sessionFactory = sessionFactory;
        init();
    }

    private static void init() {
        SessionContext context = null;
        try {
            context = sessionFactory.getSessionContext();
            List<Item> items = context.getMapper(ItemMapper.class).selectAll();
            items.forEach(u -> {
                itemMap.put(u.getId(), u);
            });
        } catch (Exception e) {
            LoggerUtil.error(log, e);
        } finally {
            SessionContext.closeSilently(context);
        }
    }

    public static synchronized void addItem(Item item) {
        itemMap.put(item.getId(), item);
    }
}