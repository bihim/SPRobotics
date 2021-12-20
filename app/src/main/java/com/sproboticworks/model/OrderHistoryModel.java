package com.sproboticworks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistoryModel {

    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public class Datum {

        @SerializedName("order_id")
        @Expose
        private Integer orderId;
        @SerializedName("order_no")
        @Expose
        private String orderNo;
        @SerializedName("order_date")
        @Expose
        private String orderDate;
        @SerializedName("invoice_no")
        @Expose
        private Object invoiceNo;
        @SerializedName("order_item_array")
        @Expose
        private List<OrderItemArray> orderItemArray = null;

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public Object getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(Object invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public List<OrderItemArray> getOrderItemArray() {
            return orderItemArray;
        }

        public void setOrderItemArray(List<OrderItemArray> orderItemArray) {
            this.orderItemArray = orderItemArray;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

    }

    public class OrderItemArray {

        @SerializedName("order_item_id")
        @Expose
        private Integer orderItemId;
        @SerializedName("product_id")
        @Expose
        private Integer productId;
        @SerializedName("item_name")
        @Expose
        private String itemName;
        @SerializedName("mobile_app_image")
        @Expose
        private String mobileAppImage;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("sub_total")
        @Expose
        private String subTotal;
        @SerializedName("net_total")
        @Expose
        private String netTotal;
        @SerializedName("status")
        @Expose
        private String status;

        public String getMobileAppImage() {
            return mobileAppImage;
        }

        public void setMobileAppImage(String mobileAppImage) {
            this.mobileAppImage = mobileAppImage;
        }

        public Integer getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(Integer orderItemId) {
            this.orderItemId = orderItemId;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(String subTotal) {
            this.subTotal = subTotal;
        }

        public String getNetTotal() {
            return netTotal;
        }

        public void setNetTotal(String netTotal) {
            this.netTotal = netTotal;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}


