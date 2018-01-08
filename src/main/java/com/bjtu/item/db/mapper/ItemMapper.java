package com.bjtu.item.db.mapper;

import com.bjtu.item.entity.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;
import java.util.Map;

public interface ItemMapper {


    @Insert("insert into t_item(status, name, price, stock, `desc`, create_time, images) values (#{status}, #{name}, #{price},#{stock}, #{desc},#{createTime}, #{images})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int addItem(Item item);

    @Select("select id, name, status, price, stock, `desc`,images, create_time createTime, update_time updateTime from t_item")
    List<Item> selectAll();

    int update(Map<String, Item> item);
}