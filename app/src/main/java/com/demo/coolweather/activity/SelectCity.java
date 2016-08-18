package com.demo.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.demo.coolweather.R;

public class SelectCity extends Activity {
    private EditText editText;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        SharedPreferences sp = getSharedPreferences("setInfo", MODE_PRIVATE);
        editText = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.btn_ok);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        boolean isChecked = sp.getBoolean("isChecked", false);
        checkBox.setChecked(isChecked);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = checkBox.isChecked();
                SharedPreferences.Editor editor = getSharedPreferences("setInfo", MODE_PRIVATE).edit();
                editor.putBoolean("isChecked", isChecked);
                editor.apply();
                String cityName = editText.getText().toString();
                if (!TextUtils.isEmpty(cityName)) {
                    Intent intent = new Intent();
                    intent.putExtra("city_name", cityName);
                    setResult(RESULT_OK, intent);
                }else {
                    if (isChecked) {
                        Intent intent=new Intent();
                        intent.putExtra("isChecked",true);
                        setResult(RESULT_FIRST_USER,intent);
                        finish();
                    }
                }
                finish();
            }
        });
    }
}
