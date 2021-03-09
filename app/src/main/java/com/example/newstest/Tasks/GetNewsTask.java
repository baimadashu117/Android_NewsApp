package com.example.newstest.Tasks;

import android.content.Context;
import android.media.MediaExtractor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.newstest.Utils.HttpUtil;

public class GetNewsTask extends AsyncTask<String, Void, String> {

    private static String TAG = "GetNewsTask";
    private Handler handler;

    public GetNewsTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... strings) {
        return HttpUtil.HttpGet(strings[0]);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        Message msg = handler.obtainMessage();
//        Log.d(TAG, s);
        msg.obj = s;
        handler.sendMessage(msg);
    }


}
