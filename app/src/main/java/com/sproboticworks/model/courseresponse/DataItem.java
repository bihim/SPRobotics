package com.sproboticworks.model.courseresponse;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem implements Serializable {

	@SerializedName("short_description")
	private Object shortDescription;

	@SerializedName("iamge")
	private String iamge;

	@SerializedName("mobile_app_video")
	private String mobile_app_video;

	@SerializedName("mobile_app_image")
	private String mobile_app_image;

	@SerializedName("price")
	private List<String> price;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("name")
	private String name;

	@SerializedName("age_group")
	private String age_group;

	@SerializedName("description")
	private String description;

	@SerializedName("age_category")
	private List<String> ageCategory;

	@SerializedName("slug")
	private String slug;

	public String getAge_group() {
		return age_group;
	}

	public void setAge_group(String age_group) {
		this.age_group = age_group;
	}

	public String getMobile_app_video() {
		return mobile_app_video;
	}

	public void setMobile_app_video(String mobile_app_video) {
		this.mobile_app_video = mobile_app_video;
	}

	public String getMobile_app_image() {
		return mobile_app_image;
	}

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