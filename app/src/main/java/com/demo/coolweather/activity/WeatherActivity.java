package com.demo.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.demo.coolweather.R;
import com.demo.coolweather.model.Weather;
import com.demo.coolweather.net.OkManager;
import com.demo.coolweather.util.HttpUtil;
import com.demo.coolweather.util.Utility;

public class WeatherActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private TextView cityNameText;
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
    private Weather nowWeather;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AMapLocationClient mlocationClient;
    private OkManager okManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        initView();
        SharedPreferences sp = getSharedPreferences("setInfo", MODE_PRIVATE);
        if (sp.getBoolean("isChecked", false)) {
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
        swipeRefreshLayout.setOnRefreshListener(this);
        showWeatherCatch();
    }

    @Override
    public void onRefresh() {
        publishText.setText("更新中...");
        getDataFromInternet();
    }

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
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private void showWeatherCatch() {
        Utility.getWeatherInfo(this, new Utility.CallBack() {
            @Override
            public void onFinish(Weather weather) {
                nowWeather = weather;
                showWeather();
            }
        });
    }

    private void showWeather() {
        if (nowWeather == null)
            return;
        cityName = nowWeather.getCityName();

        cityNameText.setText(nowWeather.getCityName());
        publishText.setText(nowWeather.getTime());
        currentDateText.setText(nowWeather.getDate());
        weatherDespText.setText(nowWeather.getInfo());
        tempText.setText(nowWeather.getTemperature());
        pm25.setText(nowWeather.getPm25() + " " + nowWeather.getQuality());

        date2.setText("明天");
        date3.setText(nowWeather.getDate3());
        date4.setText(nowWeather.getDate4());
        date5.setText(nowWeather.getDate5());

        info2.setText(nowWeather.getInfo2());
        info3.setText(nowWeather.getInfo3());
        info4.setText(nowWeather.getInfo4());
        info5.setText(nowWeather.getInfo5());

        setImage(nowWeather.getInfo2(), image2);
        setImage(nowWeather.getInfo3(), image3);
        setImage(nowWeather.getInfo4(), image4);
        setImage(nowWeather.getInfo5(), image5);

        temperature2.setText(nowWeather.getTemperature2());
        temperature3.setText(nowWeather.getTemperature3());
        temperature4.setText(nowWeather.getTemperature4());
        temperature5.setText(nowWeather.getTemperature5());
    }

    private void getDataFromInternet() {
        if (cityName != null && !cityName.isEmpty()) {
            String address = HttpUtil.getUrl(cityName);
            okManager = OkManager.getInstance();
            okManager.asyncObjectGet(address, new OkManager.Fun5<Weather>() {
                @Override
                public void onResponse(Weather weather) {
                    nowWeather = weather;
                    showWeather();
                    swipeRefreshLayout.setRefreshing(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Utility.saveWeatherInfo(WeatherActivity.this, nowWeather);
                        }
                    }).start();
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(WeatherActivity.this, "请选择城市", Toast.LENGTH_SHORT).show();
            publishText.setText("更新失败");
        }
    }

    private void autoLocationWeather() {
        autoLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                String mCityName;
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        mCityName = aMapLocation.getCity();//城市信息
                        if (mCityName.length() != 0) {
                            cityName = mCityName;
                            swipeRefreshLayout.setRefreshing(true);
                            onRefresh();
                        }
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        Snackbar.make(cityNameText, aMapLocation.getErrorInfo().split(" ")[0]
                                , Snackbar.LENGTH_SHORT).show();
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
        cityNameText = (TextView) findViewById(R.id.city_name);
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
        if (info == null || info.isEmpty()) {
            return;
        }
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