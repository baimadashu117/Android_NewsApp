package com.example.newstest.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.newstest.Utils.DateUtil;

import java.util.Date;
import java.util.List;

public class News implements Parcelable {
    String title;
    Date time;
    String src;
    String category;
    String pic;
    String content;
    String url;
    String webrul;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void setWebrul(String webrul) {
        this.webrul = webrul;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", src='" + src + '\'' +
                ", category='" + category + '\'' +
                ", pic='" + pic + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", webrul='" + webrul + '\'' +
                '}';
    }

    public static final Parcelable.Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel parcel) {
            News mNews = new News();
            mNews.title = parcel.readString();
            mNews.time = DateUtil.parseDate(parcel.readString());
            mNews.src = parcel.readString();
            mNews.category = parcel.readString();
            mNews.pic = parcel.readString();
            mNews.content = parcel.readString();
            mNews.url = parcel.readString();
            mNews.webrul = parcel.readString();
            return mNews;
        }

        @Override
        public News[] newArray(int i) {
            return new News[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(time.toString());
        parcel.writeString(src);
        parcel.writeString(category);
        parcel.writeString(pic);
        parcel.writeString(content);
        parcel.writeString(url);
        parcel.writeString(webrul);
    }
}
