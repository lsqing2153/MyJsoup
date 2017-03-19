package com.sqing.www.mytxtread.txtviewpager.content;

import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sqing on 2016/11/6.
 */

public class Content {
    public Map<String , List<View>> content_map;
    public Content(){
        content_map=new HashMap<String , List<View>>();
    }
    public void addContent(String index,List<View> content){
        content_map.put(index,content);
    }

    public List<View>  getContent_map(String index) {
        return content_map.get(index);
    }
}
