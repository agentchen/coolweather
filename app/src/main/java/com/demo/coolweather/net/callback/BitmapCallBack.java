package com.demo.coolweather.net.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by agentchen on 2017/1/18.
 * Email agentchen97@gmail.com
 */

public abstract class BitmapCallBack implements CallBack<Bitmap> {
    @Override
    public Bitmap parseResponse(Response response) {
        if (response != null && response.isSuccessful()) {
            try {
                byte[] bytes = response.body().bytes();
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
