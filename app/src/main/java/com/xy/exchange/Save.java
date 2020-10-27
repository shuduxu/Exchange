package com.xy.exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Save extends AppCompatActivity {
    private static final String TAG = "Save";

    EditText dollarEdit;
    EditText euroEdit;
    EditText wonEdit;
    float dollarRate2;
    float euroRate2;
    float wonRate2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        dollarRate2 = bundle.getFloat("dollarRate", 0.0f);
        euroRate2 = bundle.getFloat("euroRate", 0.0f);
        wonRate2 = bundle.getFloat("wonRate", 0.0f);

        Log.i(TAG, "onCreate: dollarRate2=" + dollarRate2);
        Log.i(TAG, "onCreate: euroRate2=" + euroRate2);
        Log.i(TAG, "onCreate: wonRate2=" + wonRate2);

        dollarEdit = findViewById(R.id.dollarEdit);
        euroEdit = findViewById(R.id.euroEdit);
        wonEdit = findViewById(R.id.wonEdit);

        dollarEdit.setText(dollarRate2+"");
        euroEdit.setText(euroRate2+"");
        wonEdit.setText(wonRate2+"");

    }

    public void onSave(View view) {
        float newDollarRate = Float.parseFloat(dollarEdit.getText().toString());
        float newEuroRate = Float.parseFloat(euroEdit.getText().toString());
        float newWonRate = Float.parseFloat(wonEdit.getText().toString());

        Intent intent = getIntent();
        Bundle bundle = new Bundle();

        bundle.putFloat("dollarRate", newDollarRate);
        bundle.putFloat("euroRate", newEuroRate);
        bundle.putFloat("wonRate", newWonRate);
        intent.putExtras(bundle);

        setResult(2, intent);
//        此处更新后的数据传递给主页面
//        同时将改变后的数据保存到文件中

        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate", newDollarRate);
        editor.putFloat("euro_rate", newEuroRate);
        editor.putFloat("won_rate", newWonRate);
        editor.apply();

        finish();
    }
}