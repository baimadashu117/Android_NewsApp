package com.example.newstest.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newstest.Beans.News;
import com.example.newstest.Tasks.GetNewsTask;
import com.example.newstest.Utils.JSONUtil;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private static String TAG = "HomeViewModel";
    private MutableLiveData<List<News>> newsList;
    private String category;

    private Bundle bundle;
    private static final String NewsURL = "https://api.jisuapi.com/news/get";
    private static final String APIkey = "8a9ec006acc43dd8";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "thread running in " + Thread.currentThread().toString());
            List<News> news = JSONUtil.parseJSON((String) msg.obj);
            Log.d(TAG, String.valueOf(news.size()));
            newsList.setValue(news);
        }
    };

//    public HomeViewModel() {
//        Log.d(TAG, "empty constructor");
//    }

    public HomeViewModel(Bundle bundle) {
        this.bundle = bundle;
        Log.d(TAG, String.valueOf(this.bundle==null));
    }

    public LiveData<List<News>> getNews() {
        if (newsList == null) {
            newsList = new MutableLiveData<>();
            loadNews();
        }
        return newsList;
    }

    public String getCategory() {
        return category;
    }

    private void loadNews() {
        List<News> news = bundle.getParcelableArrayList("newsList");
        category = bundle.getString("category");
        newsList.setValue(news);
    }

    public void downLoadNews(int fromIndex, int number, String category) {
        this.category = category;
        String requestURL = String.format(NewsURL + "?channel=%s&start=%s&num=%s&appkey=%s", category, fromIndex, number, APIkey);
        Log.d(TAG, "requestURL=" + requestURL);
        GetNewsTask task = new GetNewsTask(handler);
        task.execute(requestURL);
    }
}