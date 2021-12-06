package com.sprobotics.network.interfaces;

public interface OnCallBackListner {

    void OnCallBackSuccess(String tag, String response);
    void OnCallBackError(String tag, String error, int i);
}
