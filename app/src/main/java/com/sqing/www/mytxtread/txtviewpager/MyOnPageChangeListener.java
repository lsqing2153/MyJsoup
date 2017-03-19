package com.sqing.www.mytxtread.txtviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.sqing.www.mytxtread.txtviewpager.read.ObservationFlip;


/**
 * Created by sqing on 2016/11/12.
 */

public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
    boolean isScrolled = false;
    TxtViewPager tvp;
    public MyOnPageChangeListener(TxtViewPager tvp){
        this.tvp=tvp;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int status)
    {
        switch (status)
        {
            case 1:// 手势滑动
                isScrolled = false;
                break;
            case 2:// 界面切换
                isScrolled = true;
                break;
            case 0:// 滑动结束


// 当前为最后一张，此时从右向左滑，则切换到第一张
                if (tvp.getCurrentItem() == tvp.getAdapter()
                        .getCount() - 1 && !isScrolled)
                {
                    tvp.setCurrentItem(0);
                }
// 当前为第一张，此时从左向右滑，则切换到最后一张
                else if (tvp.getCurrentItem() == 0 && !isScrolled)
                {
                    tvp.setCurrentItem(tvp.getAdapter()
                            .getCount() - 1);
                }
                break;
        }
    }
    /*
    private int intdex_max;
    private Context ctx;
    private int chapter;
    public MyOnPageChangeListener(int index_max, Context ctx,int chapter) {
        this.intdex_max = index_max;
        this.ctx = ctx;
        this.chapter=chapter;
    }
    ObservationFlip of;
    public void setObservationFlip(ObservationFlip of) {
        this.of = of;
    }

    boolean current = false;//用于是否可以执行下一页的动作

    boolean next_mark = false;
    boolean up_mark = false;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset>0&& positionOffsetPixels>0){//当页面可以移动时
            next_mark = false;
            up_mark = false;
        }
        if (position == intdex_max - 1 && positionOffset == 0.0 && positionOffsetPixels == 0&& current) {
            next_mark = true;
        } else if (chapter != 1 && position == 0 && positionOffset == 0.0 && positionOffsetPixels == 0 && current ) {
            up_mark = true;
        }
    }


    @Override
    public void onPageSelected(int position) {
        current=false;
        if(position==0){
            up_mark=true;
        }else if (position==intdex_max){
            next_mark=true;
        }
    }

    //    boolean first=true;//用于判断是否是第一次进入0

    boolean identify = true;//标识是否执行过

    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == 0) {
            if (next_mark) {
                of.next();
                next_mark = false;
            } else if (up_mark) {
                of.up();
                up_mark = false;
            }
            current=true;
        }
        if(state==2){
            up_mark=false;
            current=false;
        }
    }


    public void setIntdex_max(int intdex_max) {
        this.intdex_max = intdex_max;
    }
    */
}
