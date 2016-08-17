package com.demo.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.demo.coolweather.R;
import com.demo.coolweather.model.MyException;
import com.demo.coolweather.model.Weather;
import com.demo.coolweather.util.HttpUtil;
import com.demo.coolweather.util.Utility;

import org.json.JSONObject;

public class WeatherActivity extends Activity {
    private TextView cityNametext;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView tempText;
    private TextView currentDateText;
    private TextView pm25;

    private TextView date2;
    private TextView date3;
    private TextView date4;
    private TextView date5;

//    private TextView info2;
//    private TextView info3;
//    private TextView info4;
//    private TextView info5;

    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;

    private TextView temperature2;
    private TextView temperature3;
    private TextView temperature4;
    private TextView temperature5;

    private ImageButton ibLeft;
    private static final String TAG = "WeatherActivity";
    private String cityName;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cityName = data.getStringExtra("city_name");
                    getDataFromInternet();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        initView();
        showWeather();
        getDataFromInternet();
        ibLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, SelectCity.class);
                startActivityForResult(intent, 1);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromInternet();
                publishText.setText("更新中...");
            }
        });
    }

    private void showWeather() {
        Log.d(TAG, "showWeather: ");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityName = prefs.getString("city_name", "");

        cityNametext.setText(prefs.getString("city_name", ""));
        publishText.setText("今天" + prefs.getString("time", "") + "发布");
        currentDateText.setText(prefs.getString("date", ""));
        weatherDespText.setText(prefs.getString("info", ""));
        tempText.setText(prefs.getString("temperature", ""));
        pm25.setText(prefs.getString("pm25", ""));

        date2.setText(prefs.getString("date2", ""));
        date3.setText(prefs.getString("date3", ""));
        date4.setText(prefs.getString("date4", ""));
        date5.setText(prefs.getString("date5", ""));

//        info2.setText(prefs.getString("info2", ""));
//        info3.setText(prefs.getString("info3", ""));
//        info4.setText(prefs.getString("info4", ""));
//        info5.setText(prefs.getString("info5", ""));

        setImage(prefs.getString("info2", ""), 2);
        setImage(prefs.getString("info3", ""), 3);
        setImage(prefs.getString("info4", ""), 4);
        setImage(prefs.getString("info5", ""), 5);

        temperature2.setText(prefs.getString("temperature2", ""));
        temperature3.setText(prefs.getString("temperature3", ""));
        temperature4.setText(prefs.getString("temperature4", ""));
        temperature5.setText(prefs.getString("temperature5", ""));
    }

    private void getDataFromInternet() {
        if (cityName != null && !TextUtils.isEmpty(cityName)) {
            String address = HttpUtil.getUrl(cityName);
            Log.d(TAG, "getDataFromInternet: " + address);
            HttpUtil.sendJsonRequest(address, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: " + response.toString());
                    Weather weather = null;
                    try {
                        weather = Utility.parseJson(response);
                    } catch (MyException e) {
                        Toast.makeText(WeatherActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        cityName = cityNametext.getText().toString();
                        return;
                    }
                    Utility.saveWeatherInfo(WeatherActivity.this, weather);
                    showWeather();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    publishText.setText("更新失败");
                    error.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            Toast.makeText(WeatherActivity.this, "请选择城市", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initView() {
        ibLeft = (ImageButton) findViewById(R.id.ib_left);
        cityNametext = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        tempText = (TextView) findViewById(R.id.temp);
        currentDateText = (TextView) findViewById(R.id.current_date);

        pm25 = (TextView) findViewById(R.id.pm25);
        date2 = (TextView) findViewById(R.id.data2);
        date3 = (TextView) findViewById(R.id.data3);
        date4 = (TextView) findViewById(R.id.data4);
        date5 = (TextView) findViewById(R.id.data5);

//        info2 = (TextView) findViewById(R.id.info2);
//        info3 = (TextView) findViewById(R.id.info3);
//        info4 = (TextView) findViewById(R.id.info4);
//        info5 = (TextView) findViewById(R.id.info5);

        temperature2 = (TextView) findViewById(R.id.temperature2);
        temperature3 = (TextView) findViewById(R.id.temperature3);
        temperature4 = (TextView) findViewById(R.id.temperature4);
        temperature5 = (TextView) findViewById(R.id.temperature5);

        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
    }

    private void setImage(String info, int num) {
        if (info.equals("晴")) {
            switch (num) {
                case 2:
                    image2.setImageResource(R.drawable.a1);
                    break;
                case 3:
                    image3.setImageResource(R.drawable.a1);
                    break;
                case 4:
                    image4.setImageResource(R.drawable.a1);
                    break;
                case 5:
                    image5.setImageResource(R.drawable.a1);
                    break;
            }
        } else if (info.equals("多云")) {
            switch (num) {
                case 2:
                    image2.setImageResource(R.drawable.a2);
                    break;
                case 3:
                    image3.setImageResource(R.drawable.a2);
                    break;
                case 4:
                    image4.setImageResource(R.drawable.a2);
                    break;
                case 5:
                    image5.setImageResource(R.drawable.a2);
                    break;
            }
        } else if (info.equals("阴")) {
            switch (num) {
                case 2:
                    image2.setImageResource(R.drawable.a3);
                    break;
                case 3:
                    image3.setImageResource(R.drawable.a3);
                    break;
                case 4:
                    image4.setImageResource(R.drawable.a3);
                    break;
                case 5:
                    image5.setImageResource(R.drawable.a3);
                    break;
            }
        } else if (info.contains("雨")) {
            switch (num) {
                case 2:
                    image2.setImageResource(R.drawable.a4);
                    break;
                case 3:
                    image3.setImageResource(R.drawable.a4);
                    break;
                case 4:
                    image4.setImageResource(R.drawable.a4);
                    break;
                case 5:
                    image5.setImageResource(R.drawable.a4);
                    break;
            }
        }
    }
}