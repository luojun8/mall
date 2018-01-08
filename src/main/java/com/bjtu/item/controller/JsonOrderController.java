package com.bjtu.item.controller;

import com.bjtu.item.db.SessionContext;
import com.bjtu.item.db.SessionFactory;
import com.bjtu.item.db.mapper.OrderMapper;
import com.bjtu.item.entity.Item;
import com.bjtu.item.entity.Order;
import com.bjtu.item.mendb.ItemMemDB;
import com.bjtu.item.mendb.OrderMemDB;
import com.bjtu.item.mendb.UserMemDB;
import com.bjtu.item.utils.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
public class JsonOrderController {

    private Logger logger = LoggerFactory.getLogger(JsonOrderController.class);

    @Autowired
    private SessionFactory sessionFactory;


    @RequestMapping(value = "/api/json/order/add", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String register(HttpServletRequest request,
                           @RequestParam(value = "userId") Long userId,
                           @RequestParam(value = "price") Double price,
                           @RequestParam(value = "jsonOrders",defaultValue = "1") String jsonOrders) {


        HttpSession session = UserMemDB.sessionMap.get(request.getSession().getId());
        if (session == null) {
            return ApiResult.errorStr(MessageUtil.CODE_DB_ERROR, MessageUtil.get(MessageUtil.CODE_DB_ERROR));
        }

        Map<Long, Integer> map = JsonUtil.fromJson(jsonOrders, new TypeToken<Map<Long, Integer>>() {}.getType());

        Order order = new Order();
        order.setItemIdQty(map);
        order.setPrice(price);
        order.setUserId(userId);
        order.setCreateTime(System.currentTimeMillis());
        SessionContext context = sessionFactory.getSessionContext();
        try {
            OrderMapper orderMapper = context.getMapper(OrderMapper.class);
            map.keySet().forEach(l -> {
                orderMapper.addEveryOrder(l, map.get(l));
            });
            int row = orderMapper.addOrder(order);
            context.commit();
            if (row > 0) {
                OrderMemDB.addOrder(order);
                map.keySet().forEach(l->{
                    Item item = ItemMemDB.itemMap.get(l);
                    if(item.getStock()>=map.get(l))
                        item.setStock(item.getStock() - map.get(l));
                });
            }
            return ApiResult.succStr();
        } catch (Exception e) {
            LoggerUtil.error(logger, e);
            return ApiResult.errorStr(MessageUtil.CODE_UNKNOWN_ERROR,MessageUtil.get(MessageUtil.CODE_UNKNOWN_ERROR));
        } finally {
            SessionContext.closeSilently(context);
        }
    }


    @RequestMapping(value = "/api/json/order/query", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String register(HttpServletRequest request,
                           @RequestParam(value = "userId") Long userId) {


        HttpSession session = UserMemDB.sessionMap.get(request.getSession().getId());
        if (session == null) {
            return ApiResult.errorStr(MessageUtil.CODE_DB_ERROR, MessageUtil.get(MessageUtil.CODE_DB_ERROR));
        }


        SessionContext context = sessionFactory.getSessionContext();
        try {
            List<Map<String, Object>> orders= context.getMapper(OrderMapper.class).selectByUid(userId);
            if (orders.size()>0) {
                orders.forEach(o -> {
                    Map<Long, Integer> map = JsonUtil.fromJson(o.get("jsonItemIdQty").toString(), new TypeToken<Map<Long, Integer>>() {}.getType());
                    map.keySet().forEach(l->{
                        Item item = ItemMemDB.itemMap.get(l);
                        o.put("item", item);
                        o.put("qty", map.get(l));
                    });

                });

            }
            return ApiResult.succStr(orders);
        } catch (Exception e) {
            LoggerUtil.error(logger, e);
            return ApiResult.errorStr(MessageUtil.CODE_UNKNOWN_ERROR,MessageUtil.get(MessageUtil.CODE_UNKNOWN_ERROR));
        } finally {
            SessionContext.closeSilently(context);
        }
    }

}