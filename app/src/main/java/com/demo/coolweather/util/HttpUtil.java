package com.demo.coolweather.util;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demo.coolweather.MyApplication;

import org.json.JSONObject;

public class HttpUtil {
    public static final String URL = "http://op.juhe.cn/onebox/weather/query";
    public static final String API_KEY = "34f9f113eab5ee8fa3861353507f8313";

    public static String getUrl(String cityName) {
        return URL + "?" + "cityname=" + cityName + "&key=" + API_KEY;
    }

    public static void sendJsonRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, listener, errorListener);
        MyApplication.getVolleyQueue().add(request);
    }
}
