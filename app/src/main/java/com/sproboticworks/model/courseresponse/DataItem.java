package com.sproboticworks.model.courseresponse;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem implements Serializable {

	@SerializedName("short_description")
	private Object shortDescription;

	@SerializedName("iamge")
	private String iamge;

	@SerializedName("price")
	private List<String> price;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("age_category")
	private List<String> ageCategory;

	@SerializedName("slug")
	private String slug;

	public Object getShortDescription(){
		return shortDescription;
	}

	public String getIamge(){
		return iamge;
	}

	public List<String> getPrice(){
		return price;
	}

	public int getProductId(){
		return productId;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public List<String> getAgeCategory(){
		return ageCategory;
	}

	public String getSlug(){
		return slug;
	}
}