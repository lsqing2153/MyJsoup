package com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by sqing on 2016/12/11.
 */

public class JsoupContents {
    public String GetContents(String url) throws IOException{
        Document document = JsoupGetDocument.getDocument(url);
        Elements elements = document.select("div[id=content]");

        return  elements.html();
    }
}
