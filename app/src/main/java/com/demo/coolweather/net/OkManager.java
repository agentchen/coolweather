package com.demo.coolweather.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.demo.coolweather.model.Weather;
import com.demo.coolweather.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by agentchen on 2017/1/18.
 * Email agentchen97@gmail.com
 */

public class OkManager {
    private static OkManager okManager;
    private OkHttpClient client;
    private Handler handler;

    private OkManager() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkManager getInstance() {
        if (okManager == null) {
            synchronized (OkManager.class) {
                if (okManager == null) {
                    okManager = new OkManager();
                }
            }
        }
        return okManager;
    }

    public void asyncStringGet(String url, final Func1 callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessStringMethod(response.body().string(), callBack);
                }
            }
        });
    }

    public void asyncJsonObjectGet(String url, final Func2 callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    try {
                        onSuccessJsonObjectMethod(new JSONObject(response.body().string()), callBack);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void asyncBytesGet(String url, final Func3 callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessBytesMethod(response.body().bytes(), callBack);
                }
            }
        });
    }

    public void asyncBitmapGet(String url, final Func4 callBack) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    byte[] bytes = response.body().bytes();
                    onSuccessBitmapMethod(BitmapFactory
                            .decodeByteArray(bytes, 0, bytes.length), callBack);
                }
            }
        });
    }

    public void asyncObjectGet(String url, final Fun5<Weather> callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    try {
                        onSuccessObjectMethod(Utility.parseJson
                                (new JSONObject(response.body().string())), callBack);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onSuccessStringMethod(final String result, final Func1 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(result);
                }
            }
        });
    }

    private void onSuccessJsonObjectMethod(final JSONObject jsonObject, final Func2 callBack) throws JSONException {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(jsonObject);
                }
            }
        });
    }

    private void onSuccessObjectMethod(final Weather weather, final Fun5<Weather> callBack) throws JSONException {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(weather);
                }
            }
        });
    }

    private void onSuccessBytesMethod(final byte[] bytes, final Func3 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(bytes);
                }
            }
        });
    }

    private void onSuccessBitmapMethod(final Bitmap bitmap, final Func4 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(bitmap);
                }
            }
        });
    }

    public interface Func1 {
        void onResponse(String result);
    }

    public interface Func2 {
        void onResponse(JSONObject jsonObject);
    }

    public interface Func3 {
        void onResponse(byte[] bytes);
    }

    public interface Func4 {
        void onResponse(Bitmap bitmap);
    }

    public interface Fun5<T extends Weather> {
        void onResponse(T weather);
    }
}
