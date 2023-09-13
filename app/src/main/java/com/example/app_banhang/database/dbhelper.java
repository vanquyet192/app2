package com.example.app_banhang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhelper extends SQLiteOpenHelper {

    //TAO NAME DATABASE
    public dbhelper(Context context) {
        super(context, "APPBANHANG", null, 1);
    }

    @Override //tao ra cac database trong bang "appbanhang"
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String quser = "CREATE TABLE USER(username TEXT PRIMARY KEY, password TEXT, fullname TEXT)";
        sqLiteDatabase.execSQL(quser);
        String qproduct = "CREATE TABLE PRODUCT(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, price INTEGER, quantity INTEGER, image TEXT)";
        sqLiteDatabase.execSQL(qproduct);

        //ADD VALUES
        String duser = "INSERT INTO USER(username, password, fullname) VALUES ('quyet',1111, 'daovanquyet')";
        sqLiteDatabase.execSQL(duser);
        String dproduct = "INSERT INTO PRODUCT VALUES (1, 'm1', 500,5, 'https://res.cloudinary.com/deq97is5t/image/upload/v1692614129/hero-image.fill.size_1248x702.v1623389216_d955jo.jpg'), (2, 'm2', 1000,5, 'https://res.cloudinary.com/deq97is5t/image/upload/v1692614129/hero-image.fill.size_1248x702.v1623389216_d955jo.jpg')";
        sqLiteDatabase.execSQL(dproduct);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USER");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PRODUCT");
            onCreate(sqLiteDatabase);
        }
    }
}

