package com.sproboticworks.network.zubaer;


import com.sproboticworks.model.AddressModel;
import com.sproboticworks.model.AllCoursesModel;
import com.sproboticworks.model.EnquiryModel;
import com.sproboticworks.model.OrderHistoryModel;
import com.sproboticworks.model.ProfileEditModel;
import com.sproboticworks.model.StateCityModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiPlaceHolder {

    @FormUrlEncoded
    @POST(Api.profileUpdate)
    Call<ProfileEditModel> setProfile(
            @Field("customer_id") String customerId,
            @Field("customer_name") String customerName,
            @Field("customer_email") String customerEmail,
            @Field("customer_contact_no") String customerContactNo,
            @Field("student_name") String studentName,
            @Field("student_email_id") String studentEmailId,
            @Field("student_contact_no") String studentContactNo,
            @Field("dob") String dob,
            @Field("gender") String gender);

    @FormUrlEncoded
    @POST(Api.profileUpdate)
    Call<ProfileEditModel> getProfile(
            @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST(Api.customerAddressSave)
    Call<AddressModel> getAddress(
            @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST(Api.customerAddressSave)
    Call<AddressModel> setAddress(
            @Field("customer_id") String customerId,
            @Field("address") String address,
            @Field("city") String city,
            @Field("state") String state,
            @Field("postal_code") String postalCode,
            @Field("contact_no") String contactNo
    );

    @POST(Api.state)
    Call<StateCityModel> getStates();

    @FormUrlEncoded
    @POST(Api.city)
    Call<StateCityModel> getCity(
            @Field("state_id") String stateId);

    @FormUrlEncoded
    @POST(Api.orderHistory)
    Call<OrderHistoryModel> getOrderHistory(
            @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST(Api.enquiry)
    Call<EnquiryModel> setEnquiry(
            @Field("customer_id") String customerId,
            @Field("product_id") String productId,
            @Field("message") String message);


    @POST(Api.allCourse)
    Call<AllCoursesModel> getAllCourses();
}
