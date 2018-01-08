package com.bjtu.item.db.mapper;

import com.bjtu.item.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

public interface UserMapper {

	@Insert("insert into t_user(name,password,role) values (#{name},#{password},#{role})")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
	int addUser(User user);

	@Select("select id, name,password,role from t_user_view")
	List<User> selectAll();
}