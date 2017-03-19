package com.sqing.www.mytxtread.txtviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sqing.www.mytxtread.activity.BookReadActivity;

/**
 * Created by sqing on 2016/12/15.
 */

public class TxtViewPager extends ViewPager {
    Context ctx;
    public TxtViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx=context;
    }

    public TxtViewPager(Context context) {
        super(context);
        ctx=context;
    }
    float x=-1;
    float y=-1;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        DisplayMetrics metric = getDisplayMetrics();
        float width = metric.widthPixels;
        float height = metric.heightPixels;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                x=ev.getX();y=ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(x>(width/3*2) || y>height/5*4) {
                    ifGone();
                    if (x == ev.getX() && y == ev.getY()) {
                        if (getCurrentItem() == getAdapter().getCount() - 1) {
                            ll_pb_read.setVisibility(View.VISIBLE);
                            read.NextThread();
                        }else{
                            setCurrentItem(getCurrentItem() + 1, false);
                        }
                    } else if (x - 8.0 < ev.getX() && x + 8.0 > ev.getX() && y - 8.0 < ev.getY() && y + 8.0 > ev.getY()) {
                        if (getCurrentItem() == getAdapter().getCount() - 1) {
                            ll_pb_read.setVisibility(View.VISIBLE);
                            read.NextThread();
                        }else{
                            setCurrentItem(getCurrentItem() + 1, false);
                        }
                    }
                }else if(x<(width/3)) {
                    ifGone();
                    if (x == ev.getX() && y == ev.getY()) {
                        if(getCurrentItem()!=0) {
                            setCurrentItem(getCurrentItem() - 1, false);
                        }else {
                            if(read.min_index !=0) {
                                ll_pb_read.setVisibility(View.VISIBLE);
                                read.UpThread();
                            }else{
                                Toast.makeText(ctx,"当前在最开始",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (x - 8.0 < ev.getX() && x + 8.0 > ev.getX() && y - 8.0 < ev.getY() && y + 8.0 > ev.getY()) {

                        if(getCurrentItem()!=0) {
                            setCurrentItem(getCurrentItem() - 1, false);
                        }else {
                            if(read.min_index !=0) {
                                ll_pb_read.setVisibility(View.VISIBLE);
                                read.UpThread();
                            }else{
                                Toast.makeText(ctx,"当前在最开始",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else {
                    if (x == ev.getX() && y == ev.getY()) {
                        isVisibility();
                    } else if (x - 8.0 < ev.getX() && x + 8.0 > ev.getX() && y - 8.0 < ev.getY() && y + 8.0 > ev.getY()) {
                        isVisibility();
                    }
                }
                break;
        }
        super.onTouchEvent(ev);
        return true;
    }

    private void ifGone() {
        if(setting_top.getVisibility()!= View.GONE && setting_bottom.getVisibility()!=View.GONE){
            setting_top.setVisibility(View.GONE);
            setting_bottom.setVisibility(View.GONE);
            setting.setVisibility(View.GONE);
        }
    }

    private void isVisibility() {
        if(setting_top.getVisibility()!= View.GONE && setting_bottom.getVisibility()!=View.GONE){
            setting_top.setVisibility(View.GONE);
            setting_bottom.setVisibility(View.GONE);
            setting.setVisibility(View.GONE);
        }else{
            setting_top.setVisibility(View.VISIBLE);
            setting_bottom.setVisibility(View.VISIBLE);
        }
    }


    LinearLayout setting_top;
    LinearLayout setting_bottom;
    LinearLayout setting;
    LinearLayout ll_pb_read;

    public void setLinearLayout(LinearLayout linearLayout_top,LinearLayout linearLayout_bottom,LinearLayout ll_pb_read,LinearLayout setting){
        this.setting_top =linearLayout_top;
        this.setting_bottom =linearLayout_bottom;
        this.ll_pb_read=ll_pb_read;
        this.setting=setting;
    }

    private DisplayMetrics getDisplayMetrics() {//为了获取宽高,像素密度
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    public void setCtx(Context ctx){
        this.ctx=ctx;
    }
    BookReadActivity read;
    public void setRead(BookReadActivity read){
        this.read=read;
    }


}
