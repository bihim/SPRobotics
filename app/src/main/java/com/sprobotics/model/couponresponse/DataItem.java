package com.sprobotics.model.couponresponse;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("coupon_tax_type")
	private String couponTaxType;

	@SerializedName("value_type")
	private String valueType;

	@SerializedName("code")
	private String code;

	@SerializedName("coupon_id")
	private int couponId;

	@SerializedName("local_value")
	private String localValue;

	@SerializedName("currency_id")
	private int currencyId;

	@SerializedName("coupon_name")
	private String couponName;

	@SerializedName("coupon_type")
	private String couponType;

	public String getCouponTaxType(){
		return couponTaxType;
	}

	public String getValueType(){
		return valueType;
	}

	public String getCode(){
		return code;
	}

	public int getCouponId(){
		return couponId;
	}

	public String getLocalValue(){
		return localValue;
	}

	public int getCurrencyId(){
		return currencyId;
	}

	public String getCouponName(){
		return couponName;
	}

	public String getCouponType(){
		return couponType;
	}
}