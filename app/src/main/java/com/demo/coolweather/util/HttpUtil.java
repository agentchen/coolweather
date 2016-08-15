package com.demo.coolweather.util;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.demo.coolweather.MyApplication;

public class HttpUtil {
    public static void sendHttpRequest(String address, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET, address, listener, errorListener);
        stringRequest.setTag("stringGet");
        MyApplication.getVolleyQueue().add(stringRequest);
    }
}
