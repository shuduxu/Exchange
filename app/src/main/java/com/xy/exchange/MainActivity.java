package com.xy.exchange;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "MainActivity";

    Handler handler;
    SharedPreferences sharedPreferences;
    EditText input_money;
    TextView exchange_rate;
    Button btn_dollar;
    Button btn_euro;
    Button btn_won;
    String str;
    String updateDate;
    String todayString;
    float money;
    float dollarRate;
    float euroRate;
    float wonRate;

    public void run() {
        URL url;
        String urlString = "https://www.usd-cny.com";
        String html = "";
        try {
            url = new URL(urlString);
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            html = inputStream2String(in);
            Log.i(TAG, "run: html = " + html);
        } catch (MalformedURLException e) {
            Log.i(TAG, "run: MalformedURLException " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.i(TAG, "run: IOException " + e);
            e.printStackTrace();
        }


        Document document =  Jsoup.parse(html);
        String[][] storage = getTable(document);

        Bundle bundle = new Bundle();

        for (String[] row : storage) {
            switch (row[0]) {
                case "美元":
                    dollarRate = 100 / Float.parseFloat(row[1]);
                    bundle.putFloat("dollar_rate", dollarRate);
                    break;
                case "欧元":
                    euroRate = 100 / Float.parseFloat(row[1]);
                    bundle.putFloat("euro_rate", euroRate);
                    break;
                case "韩币":
                    wonRate = 100 / Float.parseFloat(row[1]);
                    bundle.putFloat("won_rate", wonRate);
                    break;
            }
        }

        Message msg = handler.obtainMessage(5);
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //用于存取数据
        sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        todayString = format.format(today);
        updateDate = sharedPreferences.getString("update_date", "");
        Log.i(TAG, "onCreate: 不需要更新, 上次更新日期" + updateDate);

        if (!updateDate.equals(todayString)) {
            Log.i(TAG, "onCreate: 需要更新, 上次更新日期" + updateDate);

            Thread t = new Thread(MainActivity.this);
            t.start();

            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 5) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Bundle bundle = (Bundle) msg.obj;
                        dollarRate = bundle.getFloat("dollar_rate");
                        euroRate = bundle.getFloat("euro_rate");
                        wonRate = bundle.getFloat("won_rate");
                        editor.putFloat("dollar_rate", dollarRate);
                        editor.putFloat("euro_rate", euroRate);
                        editor.putFloat("won_rate", wonRate);
                        editor.apply();
                        //至此从字线程中获得数据并写入xml文件完成,之后从xml中取出最新的数据
                    }
                }
            };
        }



//      重新从xml页面中获得数据
        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate", 0.0f);
        wonRate = sharedPreferences.getFloat("won_rate", 0.0f);
        Log.i(TAG, "handleMessage: 22222222222222222222222222" + dollarRate);


//        第一次调用(或数值全0)则设置为原本的汇率设置
        if (dollarRate == 0 && euroRate == 0 && wonRate == 0) {
            dollarRate = 0.1477f;
            euroRate = 0.1256f;
            wonRate = 171.3422f;
            Log.i(TAG, "handleMessage: 11111111111111111");
        }
        money = 0.0f;

        input_money = findViewById(R.id.input_money);
        exchange_rate = findViewById(R.id.exchange_rate);
        btn_dollar = findViewById(R.id.btn_dollar);
        btn_euro = findViewById(R.id.btn_euro);
        btn_won = findViewById(R.id.btn_won);

    }

    public void onConfig(View view) {
        Intent intent = new Intent(MainActivity.this,Save.class);
        //使用Bundle一体化的传输数据
        Bundle bundle = new Bundle();

        bundle.putFloat("dollarRate", dollarRate);
        bundle.putFloat("euroRate", euroRate);
        bundle.putFloat("wonRate", wonRate);

        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (1 == requestCode && 2 == resultCode) {
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("dollarRate", 0.0f);
            euroRate = bundle.getFloat("euroRate", 0.0f);
            wonRate = bundle.getFloat("wonRate", 0.0f);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        str = input_money.getText().toString();
        if (str.equals("") || str.equals(R.string.message)) {
            Toast.makeText(MainActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
        } else {
            money = Float.parseFloat(str);
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


    public static String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }


    public static String[][] getTable(Document document) {
        //由于第一行没有相应的元素所以长度进行了手动调整
        int i;
        Elements trs = document.select("table").select("tr");
        //第一行元素没有td元素
        Elements tds =trs.get(1).select("td");
        //tds = trs.get(1).select("td");

        String[][] storages = new String[trs.size() - 1][2];

        for (i = 0; i < trs.size() - 1; i++) {
            tds = trs.get(i + 1).select("td");
            storages[i][0] = tds.get(0).text();
            storages[i][1] = tds.get(4).text();
            Log.i(TAG, "getTable: " + storages[i][0] + "" + storages[i][1]);
        }
        return storages;
    }
}