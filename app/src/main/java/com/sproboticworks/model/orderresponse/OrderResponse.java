package com.sproboticworks.model.orderresponse;

import com.google.gson.annotations.SerializedName;

public class OrderResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("response")
	private boolean response;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	public Data getData(){
		return data;
	}

	public boolean isResponse(){
		return response;
	}

	public String getMessage(){
		return message;
	}

	public String getError(){
		return error;
	}
}