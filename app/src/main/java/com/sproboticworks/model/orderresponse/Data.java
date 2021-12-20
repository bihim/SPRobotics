package com.sproboticworks.model.orderresponse;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("order_id")
	private String orderId;
	@SerializedName("order_no")
	private String orderNo;

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderId(){
		return orderId;
	}
}