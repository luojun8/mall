package com.bjtu.item.db.mapper;

import com.bjtu.item.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;
import java.util.Map;

public interface OrderMapper {


    @Insert("insert into t_order (price, json_item_id_qty, create_time,user_id) values (#{price}, #{jsonItemIdQty}, #{createTime}, #{userId})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int addOrder(Order order);


    @Select("select id, user_id userId, price, json_item_id_qty jsonItemIdQty, create_time createTime, update_time updateTime from t_order ")
    List<Order> selectAll();

    @Select("call PROC_QUERY_ORDER_BY_ID(#{0})")
    List<Map<String, Object>> selectByUid(Long userId);

    @Insert("insert into t_every_order(item_id, qty,create_time) values (#{0}, #{1}, unix_timestamp()*1000)")
    int addEveryOrder(Long itemId, Integer qty);
}