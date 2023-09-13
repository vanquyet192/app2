package com.example.app_banhang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.app_banhang.database.dbhelper;

public class userDAO {
    private dbhelper Dbhelper;

    public userDAO(Context context) {
        Dbhelper = new dbhelper(context);
    }

    //login
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase sqLiteDatabase = Dbhelper.getReadableDatabase();
//cursor câu truy vấn lấy user
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USER WHERE username = ? AND password = ?", new String[]{username, password});

        return cursor.getCount() > 0;
    }

    public boolean Register(String username, String password, String fullname) {
        SQLiteDatabase sqLiteDatabase = Dbhelper.getWritableDatabase();

        //theem du lieuj laf dung content value
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("fullname", fullname);

        long check = sqLiteDatabase.insert("USER", null, contentValues);
        return check != -1;
    }

    //quen matkhau
    public String ForgotPassword(String email) {
        SQLiteDatabase sqLiteDatabase = Dbhelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT password FROM USER WHERE username = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(0);


        } else {
            return "";
        }
    }
}
