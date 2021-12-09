package com.sprobotics.model.cartrespone;

import com.google.gson.annotations.SerializedName;

public class Data1{

	@SerializedName("cart_id")
	private String cartId;

	@SerializedName("product_total_price")
	private String productTotalPrice;

	public String getCartId(){
		return cartId;
	}

	public String getProductTotalPrice(){
		return productTotalPrice;
	}
}