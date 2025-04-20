package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "LostFound.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE adverts(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, phone TEXT, description TEXT, " +
                "date TEXT, location TEXT, type TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("DROP TABLE IF EXISTS adverts");
        onCreate(DB);
    }

    public boolean insertAdvert(String name, String phone, String desc, String date, String loc, String type) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("phone", phone);
        cv.put("description", desc);
        cv.put("date", date);
        cv.put("location", loc);
        cv.put("type", type);
        long result = DB.insert("adverts", null, cv);
        return result != -1;
    }

    public Cursor getAllAdverts() {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT * FROM adverts", null);
    }

    public Integer deleteAdvert(int id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.delete("adverts", "id=?", new String[]{String.valueOf(id)});
    }
}
