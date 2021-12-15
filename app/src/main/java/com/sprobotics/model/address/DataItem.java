package com.sprobotics.model.address;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("city_name")
	private List<String> cityName;

	@SerializedName("address")
	private String address;

	@SerializedName("state_name")
	private List<String> stateName;

	@SerializedName("contact_no")
	private String contactNo;

	@SerializedName("id")
	private String id;

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("postal_code")
	private String postalCode;

	@SerializedName("country_id")
	private String countryId;

	@SerializedName("city_id")
	private String cityId;

	public List<String> getCityName(){
		return cityName;
	}

	public String getAddress(){
		return address;
	}

	public List<String> getStateName(){
		return stateName;
	}

	public String getContactNo(){
		return contactNo;
	}

	public String getId(){
		return id;
	}

	public String getStateId(){
		return stateId;
	}

	public String getPostalCode(){
		return postalCode;
	}

	public String getCountryId(){
		return countryId;
	}

	public String getCityId(){
		return cityId;
	}
}