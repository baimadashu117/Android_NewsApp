package com.example.newstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.newstest.Beans.News;
import com.example.newstest.Tasks.GetNewsTask;
import com.example.newstest.Utils.HTMLUtil;
import com.example.newstest.Utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String NewsURL = "https://api.jisuapi.com/news/get";
    private static final String APIkey = "8a9ec006acc43dd8";
    private List<News> newsList;
    private long exitTime = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            String response = (String) message.obj;
            Log.d(TAG, "response length = " + response.length());
            try {
                JSONObject obj = new JSONObject(response);
                String status = obj.getString("status");
                Log.d(TAG, "status=" + status);
                String msg = obj.getString("msg");
                Log.d(TAG, "msg=" + msg);
                if (status.equals("0") && msg.equals("ok")) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            newsList = JSONUtil.parseJSON(response);
            getNewsContent(newsList);
        }
    };

    private void getNewsContent(List<News> newsList) {
        Intent intent = new Intent(MainActivity.this, Drawer_V1.class);
        Bundle bundle = new Bundle();
        bundle.putString("category","头条");
        bundle.putParcelableArrayList("newsList", (ArrayList<? extends Parcelable>) newsList);
        intent.putExtra("data",bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button get = findViewById(R.id.get);
        get.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.get) {
            sendRequestWithHttp();
//            finish();
        }

    }

    private void sendRequestWithHttp() {
        String requestURL = String.format(NewsURL + "?channel=%s&start=0&num=15&appkey=%s", "头条", APIkey);
        Log.d(TAG, "requestURL=" + requestURL);
        GetNewsTask task = new GetNewsTask(handler);
        //task.setListener(listener);
        task.execute(requestURL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "Press again to exit APP", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}