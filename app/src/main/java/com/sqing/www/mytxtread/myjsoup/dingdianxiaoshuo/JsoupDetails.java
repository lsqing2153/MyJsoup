package com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by sqing on 2016/12/9.
 */

public class JsoupDetails {
    public String[] getDetails(String bookUrl) throws IOException{
        String[] strings=new String[9];
        Document document = JsoupGetDocument.getDocument(bookUrl);
        Elements elements = document.select("dl[id=content]");
        Elements elements_bookname = elements.select("dd").select("h1");
        String book_name = elements_bookname.text().split(" ")[0];//获取书籍名字
        String img_url = elements.select("img").attr("src");//获取书籍图片URL
        strings[0]=book_name;strings[1]=img_url;
        Elements elements_state = elements.select("tbody").select("tr");
        for(int i=0;i<= 1;i++){
            if(i==0){
                Element element = elements_state.get(i);
                Elements th = element.select("th");
                Elements td = element.select("td");
                String book_category = th.get(0).text() + ":" + td.get(0).text();//获取小说类别
                String book_author = th.get(1).text() + ":" + td.get(1).text();//获取小说作者
                strings[2]=book_category;strings[3]=book_author;
            } else if (i == 1) {
                Element element = elements_state.get(i);
                Elements th = element.select("th");
                Elements td = element.select("td");
                String book_number = th.get(1).text() + ":" + td.get(1).text();//获取小说字数
                String book_time = th.get(2).text() + ":" + td.get(2).text();//获取小说最后更新时间
                strings[4]=book_number;strings[5]=book_time;
            }
        }
        //获取小说简介内容
        Element synopsis = elements.select("dd").get(3);
        Elements p = synopsis.select("p");
        strings[6] = p.get(1).text();
        //获取最新章节显示
        strings[7]=p.get(5).text();
        //获取小说目录URL
        String book_catalog = elements.select("a[class=read]").attr("href");
        strings[8]=book_catalog;
//        for (String s:strings)
//            System.out.println(s);
        return strings;
    }

}
