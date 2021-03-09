package com.example.newstest.Utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class HTMLUtil {

    private static final String TAG = "HTMLUtil";

    public static ArrayList<HashMap<String,String>> parseHTML(String htmlStr){
        ArrayList<HashMap<String,String>> data = new ArrayList<>();
        Document doc = Jsoup.parseBodyFragment(htmlStr);
        Element body = doc.body();
        Elements elements = body.getAllElements();
        for(Element element : elements){
            //Log.d(TAG,element.toString());
            if(element.tagName().equals("p")){
                Log.d(TAG,"FOUND TEXT");
                HashMap<String,String> map = new HashMap<>();
                map.put("type","text");
                map.put("value",element.text());
                data.add(map);
            }else if(element.tagName().equals("figure")){
                Log.d(TAG,"FOUND IMAGE");
                Elements img = element.getElementsByTag("img");
                String imgURL = img.attr("src");
                Log.d(TAG,"image src="+imgURL);
                HashMap<String,String> map = new HashMap<>();
                map.put("type","image");
                map.put("value",imgURL);
                data.add(map);
            }
        }
        return data;
    }

}
