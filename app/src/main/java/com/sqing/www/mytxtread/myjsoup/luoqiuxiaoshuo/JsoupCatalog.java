package com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo;

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
        Elements elements = document.select("div[id=defaulthtml4]").select("div[class=dccss]");
        System.out.println(elements.size());
        for(Element element:elements){
            Elements a = element.select("a");
            String catalog_name = a.text();
            String catalog_url="http://www.luoqiu.com/"+a.attr("href");
            list.add(catalog_name+"====="+catalog_url);
        }

        return list;
    }
}
