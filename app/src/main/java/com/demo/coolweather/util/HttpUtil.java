package com.demo.coolweather.util;

public class HttpUtil {
    private static final String URL = "http://op.juhe.cn/onebox/weather/query";
    private static final String API_KEY = "34f9f113eab5ee8fa3861353507f8313";

    public static String getUrl(String cityName) {
        return URL + "?" + "cityname=" + cityName + "&key=" + API_KEY;
    }

}
