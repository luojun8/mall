package com.bjtu.item.db.mapper;

import org.apache.ibatis.annotations.Insert;


public interface SampleMapper {

	@Insert("insert into t_test(create_time) values (#{0})")
	int add(long time);
}
