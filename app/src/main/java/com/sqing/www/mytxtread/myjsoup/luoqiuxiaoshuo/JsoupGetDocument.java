package com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sqing on 2016/12/9.
 */

public class JsoupGetDocument {
    public static Document getDocument(String url) throws IOException {
            Connection connect = Jsoup.connect(url);
            Map<String, String> header = new HashMap<String, String>();
//            header.put("User-Agent", "	Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
//            header.put("Content-Encoding", "zgzip");
//            header.put("ETag", "1487608818|");
//            header.put("Connection", "keep-alive");
//            header.put("Upgrade-Insecure-Requests", "1");
//            header.put("Vary", "Accept-Encoding");
//            header.put("Cookie", "CNZZDATA1253347400=2110769954-1456128520-null%7C1456236522; targetEncodingwww23wxcom=2");
            Connection data = connect.data(header);
//        Document document = data.timeout(10000).get();
        Document document = data.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                .timeout(10000).get();
            return document;
    }
}
