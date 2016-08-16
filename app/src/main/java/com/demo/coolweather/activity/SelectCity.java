package com.demo.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.coolweather.R;

public class SelectCity extends Activity {
    private EditText editText;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        editText= (EditText) findViewById(R.id.editText);
        button= (Button) findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName=editText.getText().toString();
                if (!TextUtils.isEmpty(cityName)){
                    Intent intent=new Intent();
                    intent.putExtra("city_name",cityName);
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    Toast.makeText(SelectCity.this, "请输入城市名", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
