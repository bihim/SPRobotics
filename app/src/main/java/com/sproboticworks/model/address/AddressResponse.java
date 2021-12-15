package com.sproboticworks.model.address;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AddressResponse{

	@SerializedName("data")
	private List<com.sprobotics.model.address.DataItem> data;

	@SerializedName("response")
	private boolean response;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	public List<com.sprobotics.model.address.DataItem> getData(){
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