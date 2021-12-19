package com.sproboticworks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllCoursesModel {

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

        @SerializedName("product_id")
        @Expose
        private Integer productId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("short_description")
        @Expose
        private String shortDescription;
        @SerializedName("price")
        @Expose
        private List<String> price = null;
        @SerializedName("mobile_app_image")
        @Expose
        private String mobileAppImage;
        @SerializedName("mobile_app_video")
        @Expose
        private String mobileAppVideo;

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public List<String> getPrice() {
            return price;
        }

        public void setPrice(List<String> price) {
            this.price = price;
        }

        public String getMobileAppImage() {
            return mobileAppImage;
        }

        public void setMobileAppImage(String mobileAppImage) {
            this.mobileAppImage = mobileAppImage;
        }

        public String getMobileAppVideo() {
            return mobileAppVideo;
        }

        public void setMobileAppVideo(String mobileAppVideo) {
            this.mobileAppVideo = mobileAppVideo;
        }

    }

}