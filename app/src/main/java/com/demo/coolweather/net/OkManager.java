package com.demo.coolweather.net;

import android.os.Handler;
import android.os.Looper;

import com.demo.coolweather.net.callback.CallBack;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    public void asyncPost(String url, Map<String, String> params, final CallBack callBack) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody RequestBody = formBuilder.build();
        Request request = new Request.Builder().url(url).post(RequestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Object object = callBack.parseResponse(response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResponse(object);
                    }
                });
            }
        });
    }

    public void asyncGet(String url, final CallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Object object = callBack.parseResponse(response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResponse(object);
                    }
                });
            }
        });
    }
}
