package com.sprobotics.model.cartrespone;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("product_local_price")
	private String productLocalPrice;

	@SerializedName("image")
	private List<String> image;

	@SerializedName("item_id")
	private String itemId;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("product_name")
	private List<String> productName;

	@SerializedName("product_quantity")
	private String productQuantity;

	public String getProductLocalPrice(){
		return productLocalPrice;
	}

	public List<String> getImage(){
		return image;
	}

	public String getItemId(){
		return itemId;
	}

	public String getProductId(){
		return productId;
	}

	public List<String> getProductName(){
		return productName;
	}

	public String getProductQuantity(){
		return productQuantity;
	}
}