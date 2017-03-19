package com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo;


import com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupGetDocument;

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
            Elements element = document.select("dl[id=content]").select("tr").get(1).select("td");
            String name = element.get(0).text();
            String href = "http://www.23us.so" + element.get(0).select("a").attr("href");
            String new_chapter = element.get(1).text();
            String date = element.get(4).text();
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
            for(int i=1;i<=document.select("dl[id=content]").select("tr").size()-1;i++) {
                Elements element = document.select("dl[id=content]").select("tr").get(i).select("td");
                String name = element.get(0).text();
                String href = "http://www.23us.so/xiaoshuo/" + element.get(0).select("a").attr("href").split("/")[5]+".html";
                String author =element.get(2).text();
                String date = element.get(4).text();
                String[] ss =new String[5];
                ss[0] = name;
                ss[1] = author;
                ss[2] = date;
                ss[3] = href;
                ss[4]="";
                list.add(ss);
            }
        }catch (IndexOutOfBoundsException e){
        }catch (SocketTimeoutException e){
        }
        return list;
    }
}
