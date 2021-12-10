package com.sprobotics.network.zubaer;



import com.sprobotics.model.ProfileEditModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

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
}
