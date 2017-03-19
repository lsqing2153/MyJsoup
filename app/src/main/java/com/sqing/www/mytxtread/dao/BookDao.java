package com.sqing.www.mytxtread.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sqing.www.mytxtread.db.BooksSQLiteOpenHelper;
import com.sqing.www.mytxtread.userdata.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqing on 2016/12/25.
 */

public class BookDao {
    private BooksSQLiteOpenHelper dbHelper =null;

    public BookDao(Context ctx){
        dbHelper =new BooksSQLiteOpenHelper(ctx);
    }
    public void add(Book book){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("bookname", book.name);//列名-列值
        values.put("img", book.img);
        values.put("book_url", book.url);
        values.put("thischapter", book.thischapter);
        values.put("page", book.page);
        values.put("details_url",book.details_url);
        values.put("www",book.www);
        db.insert("books",null,values);
        db.close();
    }
    public List<Book> queryAll(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("books"//表名
                , null
                , null//where的条件
                , null//条件的值
                , null//groupBy
                , null//having
                , null//orderBy
        );
        List<Book> books=new ArrayList<Book>(cursor.getCount());//存放查询结果
        //从query返回的cursor,其指向数据的指针位于一个空行，并不是第一条数据记录
        boolean hasData=cursor.moveToFirst();//将游标集的指针查询的第一条结果
        while(hasData){
            //从游标集中提取数据
            int nameIdx=cursor.getColumnIndex("bookname");
            int imgIdx=cursor.getColumnIndex("img");
            int urlIdx=cursor.getColumnIndex("book_url");
            int thischapterIdx=cursor.getColumnIndex("thischapter");
            int pageIdx=cursor.getColumnIndex("page");
            int details_urlIdx=cursor.getColumnIndex("details_url");
            int www_urlIdx=cursor.getColumnIndex("www");
            String name=cursor.getString(nameIdx);
            String img=cursor.getString(imgIdx);
            String url=cursor.getString(urlIdx);
            int thischapter = cursor.getInt(thischapterIdx);
            int page = cursor.getInt(pageIdx);
            String details_url=cursor.getString(details_urlIdx);
            String www=cursor.getString(www_urlIdx);
            Book book=new Book(name,img,url,thischapter,page,details_url,www);
            books.add(book);
            hasData=cursor.moveToNext();//  将游标集的指针指向下一条数据
        }
        cursor.close();
        db.close();
        return books;
    }
    public boolean query(Book book){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("books"//表名
                , null
                , "bookname=?"//where的条件
                , new String[]{book.name}//条件的值
                , null//groupBy
                , null//having
                , null//orderBy
        );
        boolean hasData=cursor.moveToNext();//是否有结果
        cursor.close();
        db.close();
        return hasData;
    }

    public int delete(Book book){//删
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int affectedRows=db.delete("books", "bookname=?", new String[]{book.name});
        db.close();
        return affectedRows;
    }


    public int update(Book book){//改
//        String encondePwd=Base64.encodeToString(user.password.getBytes(), Base64.DEFAULT);//对密码加密
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("thischapter", book.thischapter);
        values.put("page", book.page);
        int affectedRows=db.update("books", values, "bookname=?", new String[]{book.name});
        db.close();
        return affectedRows;
    }


    public int update(String name,String target,String value){//改
//        String encondePwd=Base64.encodeToString(user.password.getBytes(), Base64.DEFAULT);//对密码加密
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(target, value);
        int affectedRows=db.update("books", values, "bookname=?", new String[]{name});
        db.close();
        return affectedRows;
    }
    public int update(String name,String target,int value){//改
//        String encondePwd=Base64.encodeToString(user.password.getBytes(), Base64.DEFAULT);//对密码加密
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(target, value);
        int affectedRows=db.update("books", values, "bookname=?", new String[]{name});
        db.close();
        return affectedRows;
    }





}
