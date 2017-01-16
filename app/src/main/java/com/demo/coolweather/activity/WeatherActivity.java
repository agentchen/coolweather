package com.demo.coolweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.demo.coolweather.R;
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

    private TextView info2;
    private TextView info3;
    private TextView info4;
    private TextView info5;

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
    private AMapLocationClient mlocationClient;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    cityName = data.getStringExtra("city_name");
                    getDataFromInternet();
                    break;
                case RESULT_FIRST_USER:
                    autoLocationWeather();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        initView();
        showWeather();
        SharedPreferences sp = getSharedPreferences("setInfo", MODE_PRIVATE);
        boolean isAutoLocation = sp.getBoolean("isChecked", false);
        if (isAutoLocation) {
            autoLocationWeather();
        }
        ibLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, SelectCity.class);
                startActivityForResult(intent, 1);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                publishText.setText("更新中...");
                getDataFromInternet();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private void showWeather() {
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

        info2.setText(prefs.getString("info2", ""));
        info3.setText(prefs.getString("info3", ""));
        info4.setText(prefs.getString("info4", ""));
        info5.setText(prefs.getString("info5", ""));

        setImage(prefs.getString("info2", ""), image2);
        setImage(prefs.getString("info3", ""), image3);
        setImage(prefs.getString("info4", ""), image4);
        setImage(prefs.getString("info5", ""), image5);

        temperature2.setText(prefs.getString("temperature2", ""));
        temperature3.setText(prefs.getString("temperature3", ""));
        temperature4.setText(prefs.getString("temperature4", ""));
        temperature5.setText(prefs.getString("temperature5", ""));
    }

    private void getDataFromInternet() {
        if (cityName != null && !TextUtils.isEmpty(cityName)) {
            String address = HttpUtil.getUrl(cityName);
            Log.d(TAG, "getDataFromInternet: ");
            HttpUtil.sendJsonRequest(address, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: " + response.toString());
                    Weather weather;
                    try {
                        weather = Utility.parseJson(response);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
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
                    swipeRefreshLayout.setRefreshing(false);
                    publishText.setText("更新失败");
                    error.printStackTrace();
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo == null || !networkInfo.isConnected()) {
                        Toast.makeText(WeatherActivity.this, "没有网络连接", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(WeatherActivity.this, "请选择城市", Toast.LENGTH_SHORT).show();
            publishText.setText("更新失败");
        }
    }

    private void autoLocationWeather() {
        Log.d(TAG, "autoLocationWeather(): ");
        autoLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                String mCityName;
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        mCityName = aMapLocation.getCity();//城市信息
                        if (mCityName.length() != 0) {
                            cityName = mCityName;
                            Log.d(TAG, "onLocationChanged: " + cityName);
                            getDataFromInternet();
                        }
                    } else {
                        publishText.setText("更新失败");
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
                mlocationClient.onDestroy();
            }
        });
    }

    private void autoLocation(AMapLocationListener aMapLocationListener) {
        mlocationClient = new AMapLocationClient(this);
        Log.d(TAG, "AMapLocationClient" + mlocationClient.toString());
//初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
//设置定位监听
        mlocationClient.setLocationListener(aMapLocationListener);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(3000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();
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

        info2 = (TextView) findViewById(R.id.info2);
        info3 = (TextView) findViewById(R.id.info3);
        info4 = (TextView) findViewById(R.id.info4);
        info5 = (TextView) findViewById(R.id.info5);

        temperature2 = (TextView) findViewById(R.id.temperature2);
        temperature3 = (TextView) findViewById(R.id.temperature3);
        temperature4 = (TextView) findViewById(R.id.temperature4);
        temperature5 = (TextView) findViewById(R.id.temperature5);

        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
    }

    private void setImage(String info, ImageView imageView) {
//        现在只有四种图片
        if (info.equals("晴")) {
            imageView.setImageResource(R.drawable.a1);
        } else if (info.equals("多云")) {
            imageView.setImageResource(R.drawable.a2);
        } else if (info.equals("阴")) {
            imageView.setImageResource(R.drawable.a3);
        } else if (info.contains("雨")) {
            imageView.setImageResource(R.drawable.a4);
        }
    }
}