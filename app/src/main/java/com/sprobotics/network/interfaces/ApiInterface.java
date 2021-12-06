package com.sprobotics.network.interfaces;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET()
    Call<String> getRequest(@Url String url,@Header("tag") String tag);

    @POST()
    Call<String> postRequest(@Url String url, @FieldMap HashMap<String, String> params,@Header("tag") String tag);

    @Multipart
    @POST()
    Call<String> uploadMultiPart(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part MultipartBody.Part file, @Header("tag") String tag);

    @Multipart
    @POST()
    Call<String> uploadListMultiPart(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part List<MultipartBody.Part> file, @Header("tag") String tag);

}
