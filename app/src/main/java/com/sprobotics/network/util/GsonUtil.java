package com.sprobotics.network.util;

import com.google.gson.Gson;

public class GsonUtil {

    public static Object toObject(String data, Class classType) {
        Object object = new Gson().fromJson(data, classType);
        return object;
    }

    public static String toJsonString(Class classType) {
        String data = new Gson().toJson(classType);
        return data;
    }


}
