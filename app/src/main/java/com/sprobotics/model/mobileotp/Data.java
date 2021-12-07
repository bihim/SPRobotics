package com.sprobotics.model.mobileotp;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("otp")
	private String otp;

	public String getOtp(){
		return otp;
	}
}