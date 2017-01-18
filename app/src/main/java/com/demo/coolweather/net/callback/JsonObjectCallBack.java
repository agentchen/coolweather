package com.demo.coolweather.net.callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by agentchen on 2017/1/18.
 * Email agentchen97@gmail.com
 */

public abstract class JsonObjectCallBack implements CallBack<JSONObject> {
    @Override
    public JSONObject parseResponse(Response response) {
        if (response != null && response.isSuccessful()) {
            try {
                return new JSONObject(response.body().string());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
