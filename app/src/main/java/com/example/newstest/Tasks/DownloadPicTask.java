package com.example.newstest.Tasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class DownloadPicTask extends AsyncTask<String, Void, Drawable> {

    private static final String TAG = "DownloadPicTask";
    private Handler mHandler;
    private ImageView mImageView;

    public DownloadPicTask(ImageView imageView, Handler handler) {
        mHandler = handler;
        mImageView = imageView;
    }

    @Override
    protected Drawable doInBackground(String... strings) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            //Log.d(TAG, "Download Pic: " + strings[0]);
            drawable = Drawable.createFromStream(new URL(strings[0]).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            //Log.d(TAG, "null drawable");
        } else {
            //Log.d(TAG, "not null drawable");
        }
        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if (drawable != null) {
            //listener.onFinished(drawable);
            Message msg = mHandler.obtainMessage();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("imageview", mImageView);
            hashMap.put("drawable", drawable);
            msg.obj = hashMap;
            mHandler.sendMessage(msg);
        }
    }

}
