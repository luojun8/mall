package com.bjtu.item.controller;

import com.bjtu.item.db.SessionContext;
import com.bjtu.item.db.SessionFactory;
import com.bjtu.item.db.mapper.SampleMapper;
import com.bjtu.item.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
	private Logger logger = LoggerFactory.getLogger(SampleController.class);

	@Autowired
	private SessionFactory sessionFactory;

	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String test() {
		System.out.println("1111111");
		long time = System.currentTimeMillis();
		SessionContext context = sessionFactory.getSessionContext();
		try {
			context.getMapper(SampleMapper.class).add(time);
			context.commit();
			return "OK";
		} catch (Exception e) {
			LoggerUtil.error(logger, e);
			return "ERROR";
		} finally {
			SessionContext.closeSilently(context);
		}
	}
}
