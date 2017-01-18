package com.demo.coolweather.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.demo.coolweather.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utility {


    public static void getWeatherInfo(final Context context, final IOCallBack IOCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Weather weather;
                File file = new File(context.getFilesDir(), "weather");
                if (file.exists()) {
                    ObjectInputStream ois = null;
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        ois = new ObjectInputStream(fis);
                        weather = (Weather) ois.readObject();
                        goToUiThread(weather, IOCallBack);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (ois != null) {
                                ois.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private static void goToUiThread(final Weather weather, final IOCallBack IOCallBack) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                IOCallBack.onFinish(weather);
            }
        });
    }

    public static void saveWeatherInfo(Context context, Weather weather) {
        File file = new File(context.getFilesDir(), "weather");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(weather);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Weather parseJson(JSONObject jsonObject) throws JSONException {
        Weather weather = null;
        int errorCode;
        errorCode = jsonObject.getInt("error_code");
        if (errorCode == 0) {
            weather = new Weather();
            JSONObject data = jsonObject.getJSONObject("result").getJSONObject("data");
            JSONObject todayJson = data.getJSONObject("realtime");
            weather.setCityName(todayJson.getString("city_name"));
            weather.setDate(todayJson.getString("date"));
            weather.setTime(todayJson.getString("time").substring(0, 5));
            weather.setInfo(todayJson.getJSONObject("weather").getString("info"));
            weather.setTemperature(todayJson.getJSONObject("weather").getString("temperature"));

            weather.setDate3(data.getJSONArray("weather").getJSONObject(2).getString("week"));
            weather.setDate4(data.getJSONArray("weather").getJSONObject(3).getString("week"));
            weather.setDate5(data.getJSONArray("weather").getJSONObject(4).getString("week"));

            weather.setInfo2(data.getJSONArray("weather").getJSONObject(1).getJSONObject("info").getJSONArray("day").getString(1));
            weather.setInfo3(data.getJSONArray("weather").getJSONObject(2).getJSONObject("info").getJSONArray("day").getString(1));
            weather.setInfo4(data.getJSONArray("weather").getJSONObject(3).getJSONObject("info").getJSONArray("day").getString(1));
            weather.setInfo5(data.getJSONArray("weather").getJSONObject(4).getJSONObject("info").getJSONArray("day").getString(1));

            weather.setTemperature2(data.getJSONArray("weather").getJSONObject(1).getJSONObject("info").getJSONArray("day").getString(2));
            weather.setTemperature3(data.getJSONArray("weather").getJSONObject(2).getJSONObject("info").getJSONArray("day").getString(2));
            weather.setTemperature4(data.getJSONArray("weather").getJSONObject(3).getJSONObject("info").getJSONArray("day").getString(2));
            weather.setTemperature5(data.getJSONArray("weather").getJSONObject(4).getJSONObject("info").getJSONArray("day").getString(2));
            if (!data.get("pm25").toString().equals("[]")) {
                weather.setPm25(data.getJSONObject("pm25").getJSONObject("pm25").getString("pm25"));
                weather.setQuality(data.getJSONObject("pm25").getJSONObject("pm25").getString("quality"));
            }
        }
        return weather;
    }

    public interface IOCallBack {
        void onFinish(Weather weather);
    }
}
