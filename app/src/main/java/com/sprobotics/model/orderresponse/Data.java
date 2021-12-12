package com.sprobotics.model.orderresponse;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("order_id")
	private String orderId;

	public String getOrderId(){
		return orderId;
	}
}