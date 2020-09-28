package com.xy.exchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    EditText input_money;
    TextView exchange_rate;
    Button btn_dollar;
    Button btn_euro;
    Button btn_won;
    String str;
    double money;
    double dollarRate;
    double euroRate;
    double wonRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        money = 0.0;
        dollarRate = 0.1477;
        euroRate = 0.1256;
        wonRate = 171.3421;

        input_money = findViewById(R.id.input_money);
        exchange_rate = findViewById(R.id.exchange_rate);
        btn_dollar = findViewById(R.id.btn_dollar);
        btn_euro = findViewById(R.id.btn_euro);
        btn_won = findViewById(R.id.btn_won);
    }
    public void onConfig(View view) {
        Intent intent = new Intent(MainActivity.this, Save.class);
        //使用Bundle一体化的传输数据
        Bundle bundle = new Bundle();

        bundle.putDouble("dollarRate", dollarRate);
        bundle.putDouble("euroRate", euroRate);
        bundle.putDouble("wonRate", wonRate);

        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (1 == requestCode && 2 == resultCode) {
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getDouble("dollarRate", 0.0f);
            euroRate = bundle.getDouble("euroRate", 0.0f);
            wonRate = bundle.getDouble("wonRate", 0.0f);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View view) {
        str = input_money.getText().toString();
        if ( str == null || str.equals("") || str.equals(R.string.message)) {
            Toast.makeText(MainActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
        } else {
            money = Double.parseDouble(str);
            DecimalFormat money_format = new DecimalFormat("##.###");
            switch (view.getId()) {
                case R.id.btn_dollar:
                    exchange_rate.setText(money_format.format(dollarRate * money) + " $");
                    break;
                case R.id.btn_euro:
                    str = 0.1252 * money + "";
                    exchange_rate.setText(money_format.format(euroRate * money) + " €");
                    break;
                case R.id.btn_won:
                    str = 171.4292 * money + "";
                    exchange_rate.setText(money_format.format(wonRate * money) + " ₩");
                    break;
                default:
            }
        }
    }

    @Override//启用菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);tr
    }

    @Override//处理菜单事件
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu1){

        }
        return super.onOptionsItemSelected(item);
    }
}