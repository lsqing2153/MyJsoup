package com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by sqing on 2016/12/11.
 */

public class JsoupContents {
    public String GetContents(String url) throws IOException{
        Document document = JsoupGetDocument.getDocument(url);
        Elements elements = document.select("dd[id=contents]");

        return  elements.html();
    }
}
