package com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqing on 2016/12/11.
 */

public class JsoupCatalog {
    public List<String> getCatalog(String url) throws IOException{
        List<String> list= new ArrayList();
        Document document = JsoupGetDocument.getDocument(url);
        Elements elements = document.select("table[id=at]").select("tr");

        for(Element element:elements){
            Elements td = element.select("td");
            for(Element e:td){
                String catalog_name = e.text();
                String catalog_url=e.select("a").attr("href");
                list.add(catalog_name+"====="+catalog_url);
            }
        }
        return list;
    }
}
