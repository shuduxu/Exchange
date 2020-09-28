package com.xy.exchange;

import androidx.appcompat.app.AppCompatActivity;

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

    double dollarRate2;
    double euroRate2;
    double wonRate2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        dollarRate2 = bundle.getDouble("dollarRate", 0.0f);
        euroRate2 = bundle.getDouble("euroRate", 0.0f);
        wonRate2 = bundle.getDouble("wonRate", 0.0f);

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
        Double newDollarRate = Double.parseDouble(dollarEdit.getText().toString());
        Double newEuroRate = Double.parseDouble(euroEdit.getText().toString());
        Double newWonRate = Double.parseDouble(wonEdit.getText().toString());

        Intent intent = getIntent();
        Bundle bundle = new Bundle();

        bundle.putDouble("dollarRate", newDollarRate);
        bundle.putDouble("euroRate", newEuroRate);
        bundle.putDouble("wonRate", newWonRate);
        intent.putExtras(bundle);

        setResult(2, intent);

        finish();
    }
}