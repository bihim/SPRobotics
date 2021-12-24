package com.sproboticworks.network.util;


import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;


import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.sproboticworks.network.interfaces.ApiInterface;
import com.sproboticworks.network.interfaces.OnCallBackListner;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApiRequest {
    private Context context;
    private OnCallBackListner listener;
    private ProgressDialog progressDialog;


    public ApiRequest(Context context, OnCallBackListner listener) {
        this.context = context;
        this.listener = listener;

    }

    private void loader() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


    }


    public void errorAlert(String title, String message) {
        if (context == null) return;
        new LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
                .setTitle(title)
                .setDescription(message)
                .setPositiveText("OK")
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(@NonNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();


                    }
                }).build().show();
    }


    public void callGetRequest(String url, String tag) {
        if (NetWorkChecker.check(context)) {

            loader();
            if (progressDialog != null) {
                progressDialog.show();
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> callMethod = apiInterface.getRequest(url, tag);
            callMethod.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    onCallBackFailed(call, t);
                }
            });
        }

    }


    public void postRequest(String url, HashMap<String, String> param, String tag) {
        if (NetWorkChecker.check(context)) {

            loader();
            if (progressDialog != null) {
                progressDialog.show();
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> callMethod = apiInterface.postRequest(url, param, tag);
            callMethod.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    onCallBackFailed(call, t);
                }
            });
        }

    }

    public void postRequestJsonBody(String url, HashMap<String, String> param, String tag) {
        if (NetWorkChecker.check(context)) {

            loader();
            if (progressDialog != null) {
                progressDialog.show();
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> callMethod = apiInterface.postRequestJson(url, param, tag);
            callMethod.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    onCallBackFailed(call, t);
                }
            });
        }

    }


    public void multipartData(String url, @NonNull HashMap<String, String> param, @NonNull PART part, String tag) {

        if (NetWorkChecker.check(context)) {
            loader();
            if (progressDialog != null) {
                progressDialog.show();
            }

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            Call<String> stringCall = service.uploadMultiPart(url, getParam(param), Params.createMultiPart(part), tag);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                    onCallBackFailed(call, t);

                }
            });
        }

    }

    public void multipartList(String url, @NonNull HashMap<String, String> param, @NonNull List<PART> part, String tag) {

        if (NetWorkChecker.check(context)) {
            loader();
            if (progressDialog != null) {
                progressDialog.show();
            }

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            Call<String> stringCall = service.uploadListMultiPart(url, getParam(param), Params.createPartList(part), tag);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                    onCallBackSuccess(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                    onCallBackFailed(call, t);

                }
            });
        }

    }


    public void onCallBackSuccess(Call<String> call, Response<String> response) {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }


        if (response.isSuccessful()) {


            try {

                JSONObject object = new JSONObject(response.body());


                if (object.has("response")) {

                    if (!object.getBoolean("response"))
                        errorAlert(object.getString("response"), object.getString("message"));
                    else
                        listener.OnCallBackSuccess(call.request().header("tag"), response.body());
                } else listener.OnCallBackSuccess(call.request().header("tag"), response.body());

            } catch (Exception e) {
                //listener.OnCallBackError(call.request().header("tag"), e.getMessage(), -1);
                //errorAlert("OPPS", e.getMessage());
                e.printStackTrace();
            }
        } //else errorAlert("OPPS", "Something went wrong");

      /*  Log.d("onCallBackSuccess", "URL-> " + call.request().url());
        Log.d("onCallBackSuccess", "Response-> " + response.body().toString());*/


    }


    public void onCallBackFailed(Call<String> call, Throwable t) {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            //errorAlert("OPPS", "Something went wrong");
        } catch (Exception e) {
            //errorAlert("OPPS", "Something went wrong");
        }


    }


    private HashMap<String, RequestBody> getParam(HashMap<String, String> param) {
        HashMap<String, RequestBody> tempParam = new HashMap<>();
        for (String key : param.keySet()) {
            tempParam.put(key, toRequestBody(param.get(key)));
        }

        return tempParam;
    }


    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }
}

