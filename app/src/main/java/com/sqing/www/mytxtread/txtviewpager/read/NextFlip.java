package com.sqing.www.mytxtread.txtviewpager.read;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.txtviewpager.readytxt.TxtReady;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqing on 2016/12/5.
 */

public class NextFlip {
    private String txt;
    private  Context ctx;
    public NextFlip(String txt, Context ctx){
        this.txt=txt;
        this.ctx=ctx;
    }
    public List<View> getTxtContent(){
        TxtReady tr=new TxtReady(ctx);
//        List<String> txt=tr.ReadTxt(this.txt);
//
//
//
        List<View> list = new ArrayList<>();
//        LayoutInflater inflater = LayoutInflater.from(ctx);
//        int i=1;
//        for(String ss:txt) {
//            View view = inflater.inflate(R.layout.content_viewpager, null);
//            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
//            tv_content.setText(ss);
//            TextView tv_page = (TextView) view.findViewById(R.id.tv_page);
//            tv_page.setText(i+++"/"+txt.size());
//            list.add(view);
////            System.out.println(ss);
//
//        }



        return  list;
    }



}
