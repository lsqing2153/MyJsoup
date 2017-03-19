package com.sqing.www.mytxtread.txtviewpager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.txtviewpager.get.FontSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqing on 2016/11/6.
 */

public class MyPagerAdapter extends PagerAdapter {
    //    Contents contents;
    //    public MyPagerAdapter(Contents contents, Context ctx){
//        this.contents=contents;
//        this.ctx=ctx;
//    }
    List<View> list;
    Context ctx;
    public MyPagerAdapter(List<View> list,Context cxt){
        this.list=list;
        this.ctx =cxt;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.container=container;
        View view = list.get(position);
        container.addView(view);
        return view;
    }
    ViewGroup container;
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    public void listAdd(List<View> list){
        for(View v:list) {
            this.list.add(v);
        }
        notifyDataSetChanged();
    }

    public void listUp(List<View> list){
        List<View> l=new ArrayList<>();
        for (View v:list){
            l.add(v);
        }
        for (View v:this.list){
            l.add(v);
        }
        this.list=l;
        container.removeAllViews();
//        container.addView((View) instantiateItem(container,list.size()),2);
        notifyDataSetChanged();
    }

    /**
     * 每当数据变化是,就刷新
     * @param object
     * @return
     */
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }




}
