package com.demo.coolweather.net.callback;

import com.demo.coolweather.model.Weather;
import com.demo.coolweather.util.IOUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by agentchen on 2017/1/18.
 * Email agentchen97@gmail.com
 */

public abstract class WeatherCallBack implements CallBack<Weather> {
    @Override
    public Weather parseResponse(Response response) {
        if (response != null && response.isSuccessful()) {
            try {
                return IOUtil.parseJson(new JSONObject(response.body().string()));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
