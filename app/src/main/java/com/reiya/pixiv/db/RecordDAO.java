package com.reiya.pixiv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/24 0024.
 */
public class RecordDAO {
    private final Database database;

    public RecordDAO(Context context) {
        database = Database.getInstance(context);
    }

    public void addRecord(int id, String content, long time) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("content", content);
        values.put("time", time);
        db.insert("record", null, values);
        db.close();
    }

    public void removeRecords(long time) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.delete("record", "time < ?", new String[]{"" + time});
        db.close();
    }

    public void updateRecord(int id, long time) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);
        db.update("record", values, "id = ?", new String[]{"" + id});
        db.close();
    }

    public String getContent(int id) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM record WHERE id = " + id, null);
        String s = "";
        if (cursor.moveToNext()) {
            s =  cursor.getString(1);
        }
        cursor.close();
        return s;
    }

    public List<String> getContent() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM record ORDER BY time DESC LIMIT 500", null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
        }
        cursor.close();
        return list;
    }
}
