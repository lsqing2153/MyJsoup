package com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo;

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
        Elements elements = document.select("div[class=body]").select("table").select("table").select("tr");
        Elements select = elements.get(0).select("table").select("table").get(1).select("tr");
        Elements elements_bookname =select.get(0).select("h1");
        String book_name = elements_bookname.text();//获取书籍名字

        String img_url = document.select("img[class=picborder]").attr("src");//获取书籍图片URL

        strings[0]=book_name;strings[1]=img_url;
        Elements elements_state = select.select("table").select("tr");
        for(int i=0;i<= 1;i++){
            if(i==0){
                Element element = elements_state.get(i);
                Elements td = element.select("td");
                String book_category = td.get(0).text() + ":" + td.get(1).text();//获取小说类别
                String book_number = td.get(2).text() + ":" + td.get(3).text();//获取小说字数
                String book_time = td.get(4).text() + ":" + td.get(5).text();//获取小说最后更新时间
                strings[2]=book_category;
                strings[4]=book_number;
                strings[5]=book_time;

            } else if (i == 1) {
                Element element = elements_state.get(i);
                Elements td = element.select("td");
                String book_author = td.get(4).text() + ":" + td.get(5).text();//获取小说作者
                strings[3]=book_author;
            }
        }
        //获取小说简介内容
        Elements synopsis = elements.get(elements.size()-1).select("div[id=CrbsSum]");
        String[] split = synopsis.html().split("<br>");

        int i=1;
        String synopsis_string="";
        for(String s:split){
            if(i!=1){
                synopsis_string+=s;
            }else{
                i++;
            }
        }
        strings[6] =synopsis_string.replaceAll("&nbsp;","");
        //获取最新章节显示
        strings[7]=elements.get(elements.size()-2).select("a").text();
        //获取小说目录URL
        String book_catalog = "http://www.luoqiu.com/"+elements_state.get(3).select("a").attr("href");
        strings[8]=book_catalog;
//        for (String s:strings)
//            System.out.println(s);
        return strings;
    }

}
