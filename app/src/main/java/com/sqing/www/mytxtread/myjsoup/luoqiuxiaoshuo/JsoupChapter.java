package com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqing on 2016/12/7.
 */

public class JsoupChapter {

    public List<String[]> getChapter(String url) throws IOException {
        List<String[]> list = new ArrayList<>();
        Document document = JsoupGetDocument.getDocument(url);
        Elements elements = document.select("table[class=sf-grid]").select("tbody").select("tr");
//        System.out.println(document.select("table").select("tbody").select("tr").html());
        for (Element e : elements) {

            Elements elements_value = e.select("td");
            if (elements_value.size() > 0 && !elements_value.text().equals("")) {
                String bookName = elements_value.get(1).select("a").text();//书名
                String bookUrl = elements_value.get(1).select("a").attr("href");
                String bookLatestChapter = elements_value.get(2).text();//最新章节
                String bookAuthor = elements_value.get(4).text();//作者
                String bookNumber = elements_value.get(3).text();//字数
                String bookItme = elements_value.get(5).text();//最后更新的时间
                String bookImg = elements_value.get(1).select("a").attr("href");//图片路径有可能是错误的
                String[] img_url = bookImg.split("/");
                if(img_url.length>=5) {
                    String[] split = img_url[img_url.length-1].split(".html");
                    if (split[0].length()>=4){
                        bookImg="http://image.luoqiu.com/" +split[0].substring(0,split[0].length()-3)+ "/" +split[0]+ "/"+  split[0] + "s.jpg";
                    }
//                    bookImg = "http://image.luoqiu.com/" + split[0].substring() + "/" + img_url[7] + "/" + split[0] + "s.jpg";
                    list.add(new String[]{bookName, bookAuthor, bookNumber, bookItme, bookLatestChapter, bookUrl, bookImg});

                }else{
                    list.add(new String[]{bookName, bookAuthor, bookNumber, bookItme, bookLatestChapter, bookUrl, ""});
                }
            }


        }
        return list;
    }


}
