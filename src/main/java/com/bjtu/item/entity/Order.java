package com.bjtu.item.entity;

import com.bjtu.item.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class Order {

	private Long id;
	private Long userId;
	private Double price;
	private Map<Long, Integer> itemIdQty;
	private Long createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Map<Long, Integer> getItemIdQty() {
		return itemIdQty;
	}

	public void setItemIdQty(Map<Long, Integer> itemIdQty) {
		this.itemIdQty = itemIdQty;
	}

	public void setJsonItemIdQty(String itemIdQty){
		this.itemIdQty = JsonUtil.fromJson(itemIdQty,new TypeToken<Map<Long, Integer>>() {
		}.getType());
	}

	public String getJsonItemIdQty(){
		return JsonUtil.toJson(itemIdQty);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", userId=" + userId +
				", price=" + price +
				", itemIdQty=" + itemIdQty +
				'}';
	}
}