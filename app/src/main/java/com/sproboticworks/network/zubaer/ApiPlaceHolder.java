package com.sproboticworks.network.zubaer;


import com.sproboticworks.model.AddressModel;
import com.sproboticworks.model.AllCoursesModel;
import com.sproboticworks.model.EmailCheckingModel;
import com.sproboticworks.model.EnquiryModel;
import com.sproboticworks.model.MobileCheckingModel;
import com.sproboticworks.model.NotificationModel;
import com.sproboticworks.model.OrderHistoryModel;
import com.sproboticworks.model.ProfileEditModel;
import com.sproboticworks.model.StateCityModel;
import com.sproboticworks.network.util.Constant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiPlaceHolder {

    @FormUrlEncoded
    @POST(Constant.profileUpdate)
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
    @POST(Constant.profileUpdate)
    Call<ProfileEditModel> getProfile(
            @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST(Constant.customerAddressSave)
    Call<AddressModel> getAddress(
            @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST(Constant.customerAddressSave)
    Call<AddressModel> setAddress(
            @Field("customer_id") String customerId,
            @Field("address") String address,
            @Field("city") String city,
            @Field("state") String state,
            @Field("postal_code") String postalCode,
            @Field("contact_no") String contactNo
    );

    @POST(Constant.state)
    Call<StateCityModel> getStates();

    @FormUrlEncoded
    @POST(Constant.city)
    Call<StateCityModel> getCity(
            @Field("state_id") String stateId);

    @FormUrlEncoded
    @POST(Constant.orderHistory)
    Call<OrderHistoryModel> getOrderHistory(
            @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST(Constant.enquiry)
    Call<EnquiryModel> setEnquiry(
            @Field("customer_id") String customerId,
            @Field("product_id") String productId,
            @Field("message") String message);

    @FormUrlEncoded
    @POST(Constant.notification) //Notifications
    Call<NotificationModel> getNotification(
            @Field("customer_id") String customerId,
            @Field("product_id") String productId,
            @Field("message") String message);

    @FormUrlEncoded
    @POST(Constant.mobileChecking)
    Call<MobileCheckingModel> getMobileChecking(
            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(Constant.emailChecking)
    Call<EmailCheckingModel> getEmailChecking(
            @Field("email") String email);


    @POST(Constant.allCourse)
    Call<AllCoursesModel> getAllCourses();
}
