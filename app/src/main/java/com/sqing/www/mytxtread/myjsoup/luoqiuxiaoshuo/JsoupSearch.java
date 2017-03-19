package com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
            Element element = document.select("div[class=result-game-item-detail]").get(0);
            String name = element.select("a[class=result-game-item-title-link]").text();
            String href = element.select("a[class=result-game-item-title-link]").attr("href");
            String date = element.select("div[class=result-game-item-info]").select("p").get(2).text();
            ss[0]=name;
            ss[1]=href;
            ss[2]=date;
            ss[3]="";
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
            for(int i=0; i<document.select("div[class=result-item result-game-item]").size()-1;i++) {
                Element element = document.select("div[class=result-item result-game-item]").get(i);
                String name = element.select("a[class=result-game-item-title-link]").text();
                String author=element.select("div[class=result-game-item-info]").select("p").get(0).text();
                String href = element.select("a[class=result-game-item-title-link]").attr("href");
                String date = element.select("div[class=result-game-item-info]").select("p").get(2).text();
                String img_url=element.select("img").attr("src");

                if(href.split("/")[3].equals("read")){
                    href="http://www.luoqiu.com/book/"+href.split("/")[4]+".html";
                }



                String[] ss = new String[5];
                ss[0] = name;
                ss[1] = author;
                ss[2] = date;
                ss[3] = href;
                ss[4] = img_url;
                list.add(ss);
            }
        }catch (SocketTimeoutException e){

        }
        return list;
    }
}
