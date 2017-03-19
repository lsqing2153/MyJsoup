package com.sqing.www.mytxtread.myjsoup.qingkan;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by sqing on 2016/12/9.
 */

public class JsoupDetails {
    public String[] getDetails(String bookUrl) throws IOException {
        String[] strings = new String[9];
        Document document = JsoupGetDocument.getDocument(bookUrl);
//        Elements elements = document.select("dl[id=content]");
        String book_name = document.select("div[class=box]").select("h1").text();//获取书籍名字
        String img_url = "";//获取书籍图片URL
        strings[0] = book_name;
        strings[1] = img_url;
        Elements elements_state = document.select("div[id=aboutbook]");
        String[] split = elements_state.html().split("<br>");

        String book_category = split[0].split("&nbsp;&nbsp;&nbsp;&nbsp;")[1];//获取小说类别
        String book_author = split[0].split("&nbsp;&nbsp;&nbsp;&nbsp;")[0];//获取小说作者
        strings[2] = book_category;
        strings[3] = book_author;


        String book_number = "";//获取小说字数
        String book_time = "";//获取小说最后更新时间
        strings[4] = book_number;
        strings[5] = book_time;

        //获取小说简介内容
        String p = "";
        for (String s : split) {
            if (!s.equals(split[0])) {
                p += s;
            }
        }
        strings[6] = p.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;","");
        //获取最新章节显示
        strings[7] = document.select("div[class=newchapter]").text();
        //获取小说目录URL
        String book_catalog = bookUrl;
        strings[8] = book_catalog;
//        for (String s:strings)
//            System.out.println(s);
        return strings;
    }

}
