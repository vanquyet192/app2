package com.example.app_banhang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_banhang.database.dbhelper;
import com.example.app_banhang.model.Product;

import java.util.ArrayList;

public class productDAO {
    private dbhelper Dbhelper;

    public productDAO(Context context) {
        Dbhelper = new dbhelper(context);
    }
//lay danh sach san pham
        public ArrayList<Product> getDS() {
            SQLiteDatabase sqLiteDatabase = Dbhelper.getReadableDatabase();
            ArrayList<Product> list = new ArrayList<>();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM product", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    list.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4)));
                } while (cursor.moveToNext());
            }
            return list;
        }
        //them sp
        public boolean insertproduct(Product product){
        SQLiteDatabase sqLiteDatabase = Dbhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", product.getTitle());
            contentValues.put("price", product.getPrice());
            contentValues.put("quantity", product.getQuantity());
            contentValues.put("image", product.getImage());

            long check = sqLiteDatabase.insert("PRODUCT", null, contentValues);
            return check != -1;
            //or use if(check == -1){
            // return false};
            // else{return true};
        }


        //edit product
    public boolean editproduct(Product product){
        SQLiteDatabase sqLiteDatabase = Dbhelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", product.getTitle());
        contentValues.put("price", product.getPrice());
        contentValues.put("quantity", product.getQuantity());
        contentValues.put("image", product.getImage());

        int check = sqLiteDatabase.update("PRODUCT", contentValues, "id = ?", new String[]{String.valueOf(product.getId())});
        if (check <= 0 ) return false;
        return true;

    }
    public boolean DeleteProduct(int idproduct){
        SQLiteDatabase sqLiteDatabase = Dbhelper.getWritableDatabase();
        int check = sqLiteDatabase.delete("PRODUCT", "id = ?", new String[]{String.valueOf(idproduct)});
        if(check <= 0){
            return false;
        }else {
            return true;
        }

    }

}
