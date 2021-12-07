package com.sprobotics.model.loginresponse;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("customer_email")
	private String customerEmail;

	@SerializedName("customer_contact_no")
	private String customerContactNo;

	@SerializedName("customer_name")
	private String customerName;

	@SerializedName("customer_id")
	private String customerId;

	public String getCustomerEmail(){
		return customerEmail;
	}

	public String getCustomerContactNo(){
		return customerContactNo;
	}

	public String getCustomerName(){
		return customerName;
	}

	public String getCustomerId(){
		return customerId;
	}
}