package com.example.newstest;

import android.app.Application;

public class MyApplication extends Application {
    //记录夜间模式的全局变量
    private boolean nightMode = false;

    public boolean getMode() {
        return nightMode;
    }

    public void setMode() {
        nightMode = !nightMode;
    }
}
