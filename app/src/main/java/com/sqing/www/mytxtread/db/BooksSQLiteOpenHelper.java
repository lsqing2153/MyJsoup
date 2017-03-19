package com.sqing.www.mytxtread.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sqing on 2016/12/25.
 */

public class BooksSQLiteOpenHelper extends SQLiteOpenHelper {
    public BooksSQLiteOpenHelper(Context context) {
        super(context, "books", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table books (id integer primary key autoincrement"
                +",bookname text"
                +",img text"
                +",book_url text"
                +",thischapter integer"
                +",page integer"
                +",details_url text"
                +",www text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
