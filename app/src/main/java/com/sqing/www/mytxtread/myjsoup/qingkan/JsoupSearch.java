package com.sqing.www.mytxtread.myjsoup.qingkan;


import com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupGetDocument;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sqing on 2017/2/24.
 */

public class JsoupSearch {
    public String[] getSearchResult(String url) throws IOException {
        String[] ss =new String[4];
        try {
            Document document = JsoupGetDocument.getDocument(url);
            Element element = document.select("div[class=ss_box]").get(0);
            String name = element.select("this_display[class=sp_bookname]").text();
            String href = element.select("this_display[class=sp_bookname]").attr("href");
            String new_chapter = element.select("this_display[class=sp_chaptername]").text();
            String date = element.select("h4").text().split(new_chapter)[1];
            ss[0] = name;
            ss[1] = href;
            ss[2] = date;
            ss[3] = new_chapter;
        }catch (IndexOutOfBoundsException e){
            ss[0] = "";
            ss[1] = "";
            ss[2] = "";
            ss[3] = "";
        }catch (SocketTimeoutException e){
            ss[0] = "";
            ss[1] = "";
            ss[2] = "";
            ss[3] = "";
        }
        return ss;
    }
    public List<String[]> getSearchResult(String url,int id) throws IOException {
        List<String[]> list =new ArrayList<>();
        try {
            Document document = JsoupGetDocument.getDocument(url);
            for(int i=0;i<= document.select("div[class=ss_box]").size()-1;i++) {
                Element element = document.select("div[class=ss_box]").get(i);
                String name = element.select("a[class=sp_bookname]").text();
                String author=element.select("span[class=sp_name]").text().split("/")[1];
                String href = element.select("span[class=sp_menu]").select("a").get(0).attr("href");
                String new_chapter = element.select("a[class=sp_chaptername]").text();
                String date = element.select("h4").text().split(new_chapter)[1];

                String[] ss = new String[5];
                ss[0] = name;
                ss[1] = author;
                ss[2] = date;
                ss[3] = href;
                ss[4] = "";
                list.add(ss);
            }
        }catch (IndexOutOfBoundsException e){
        }catch (SocketTimeoutException e){
        }
        return list;
    }
}
