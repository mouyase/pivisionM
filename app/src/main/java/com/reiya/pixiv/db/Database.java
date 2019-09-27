package com.reiya.pixiv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/11/24 0024.
 */
public class Database extends SQLiteOpenHelper {
    private static Database instance;

    public Database(Context context) {
        super(context, "pixive-v2.db", null, 1);
    }

    public static Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE record ( id INTEGER PRIMARY KEY, content TEXT, time INTEGER )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
