package com.sqing.www.mytxtread.userdata;

/**
 * Created by sqing on 2016/12/25.
 */

public class Book {
    public String name;
    public String img;
    public String url;
    public int thischapter;
    public int page;
    public String details_url;
    public String www;
    public Book(String name,String img,String url, int thischapter, int page,String details_url,String www){
        this.name=name;
        this.img=img;
        this.url=url;
        this.thischapter =thischapter;
        this.page=page;
        this.details_url=details_url;
        this.www=www;
    }
}
