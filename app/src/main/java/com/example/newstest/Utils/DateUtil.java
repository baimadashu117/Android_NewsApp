package com.example.newstest.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static Date parseDate(String str) {
        Date date = null;
        try {
            if (validateDate(str)) {
                Log.d("DateUtil", "Date str = " + str);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.parse(str);
                Log.d("DateUtil", "Parsed Date is " + date);
            }else{
                Log.d("DateUtil", "Date str = " + str);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                date = sdf.parse(str);
                Log.d("DateUtil", "Parsed Date is " + date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean validateDate(String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
