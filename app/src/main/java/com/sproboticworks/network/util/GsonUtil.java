package com.sproboticworks.network.util;

import com.google.gson.Gson;

public class GsonUtil {

    public static Object toObject(String data, Class classType) {
        Object object = new Gson().fromJson(data, classType);
        return object;
    }

    public static String toJsonString(Object classType) {
        String data = new Gson().toJson(classType);
        return data;
    }


}
