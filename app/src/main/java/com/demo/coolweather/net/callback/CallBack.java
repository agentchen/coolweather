package com.demo.coolweather.net.callback;

import okhttp3.Response;

/**
 * Created by agentchen on 2017/1/18.
 * Email agentchen97@gmail.com
 */

public interface CallBack<T> {
    public T parseResponse(Response response);

    public void onResponse(T response);

    public void onError(Exception e);
}
