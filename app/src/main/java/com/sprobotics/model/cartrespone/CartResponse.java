package com.sprobotics.model.cartrespone;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.sprobotics.util.MethodClass;

public class CartResponse{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("response")
	private boolean response;

	@SerializedName("data1")
	private Data1 data1;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	public List<DataItem> getData(){
		return MethodClass.defaultWhenNull(data,new ArrayList<>());
	}

	public boolean isResponse(){
		return response;
	}

	public Data1 getData1(){
		return data1;
	}

	public String getMessage(){
		return message;
	}

	public String getError(){
		return error;
	}
}