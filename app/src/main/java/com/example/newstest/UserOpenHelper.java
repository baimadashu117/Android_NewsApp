package com.example.newstest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserOpenHelper extends SQLiteOpenHelper {
    public UserOpenHelper(@Nullable Context context) {
        super(context, "EasyNote.db", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table res (_HEAD varchar(100000000),_MSG TEXT,_UID integer,_TITLE varchar(3000000));");
        db.execSQL("create table users (_ID integer primary key autoincrement , _NAME varchar(20) not null ,_PSWD varchar(20) not null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists res");
        onCreate(db);
    }
}
