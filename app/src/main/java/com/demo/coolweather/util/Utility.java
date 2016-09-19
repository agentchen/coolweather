package com.demo.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.demo.coolweather.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    public static void saveWeatherInfo(Context context, Weather weather) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", weather.getCityName());
        String time = weather.getTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(time.charAt(i));
        }
        editor.putString("time", sb.toString());
        editor.putString("date", weather.getDate());
        editor.putString("info", weather.getInfo());
        editor.putString("temperature", weather.getTemperature() + "℃");
        editor.putString("pm25", weather.getPm25() + "  " + weather.getQuality());

        editor.putString("date2", "明天");
        editor.putString("date3", "周" + weather.getDate3());
        editor.putString("date4", "周" + weather.getDate4());
        editor.putString("date5", "周" + weather.getDate5());

        editor.putString("info2", weather.getInfo2());
        editor.putString("info3", weather.getInfo3());
        editor.putString("info4", weather.getInfo4());
        editor.putString("info5", weather.getInfo5());

        editor.putString("temperature2", weather.getTemperature2() + "℃");
        editor.putString("temperature3", weather.getTemperature3() + "℃");
        editor.putString("temperature4", weather.getTemperature4() + "℃");
        editor.putString("temperature5", weather.getTemperature5() + "℃");
        editor.apply();
    }

    public static Weather parseJson(JSONObject jsonObject) throws Exception {
        Weather weather = new Weather();
        int errorCode;
        try {
            errorCode = jsonObject.getInt("error_code");
            if (errorCode == 0) {
                JSONObject data = jsonObject.getJSONObject("result").getJSONObject("data");
                JSONObject todayJson = data.getJSONObject("realtime");
                weather.setCityName(todayJson.getString("city_name"));
                weather.setDate(todayJson.getString("date"));
                weather.setTime(todayJson.getString("time"));
                weather.setInfo(todayJson.getJSONObject("weather").getString("info"));
                weather.setTemperature(todayJson.getJSONObject("weather").getString("temperature"));

                if (!data.get("pm25").toString().equals("[]")) {
                    weather.setPm25(data.getJSONObject("pm25").getJSONObject("pm25").getString("pm25"));
                    weather.setQuality(data.getJSONObject("pm25").getJSONObject("pm25").getString("quality"));
                }

                weather.setDate2(data.getJSONArray("weather").getJSONObject(1).getString("week"));
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
            } else {
                String errorInfo = jsonObject.getString("reason");
                throw new Exception(errorInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;
    }
}
