package com.example.newstest.Utils;

import com.example.newstest.Beans.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtil {

    private static final String TAG = "JSONUtil";

    public static List<News> parseJSON(String jsonStr) {
        List<News> news_list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonStr);
            JSONArray newsList = object.getJSONObject("result").getJSONArray("list");
            for (int i = 0; i < newsList.length(); i++) {
                News news = new News();
                JSONObject news_item = newsList.getJSONObject(i);
                news.setTitle(news_item.getString("title"));
                news.setTime(DateUtil.parseDate(news_item.getString("time")));
                news.setSrc(news_item.getString("src"));
                news.setCategory(news_item.getString("category"));
                news.setPic(news_item.getString("pic"));
                news.setContent(news_item.getString("content"));
                news.setUrl(news_item.getString("url"));
                news.setWebrul(news_item.getString("weburl"));
                news_list.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news_list;
    }
}
