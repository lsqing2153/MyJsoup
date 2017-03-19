package com.sqing.www.mytxtread.txtviewpager.get;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by sqing on 2016/11/6.
 */

public class FontSize {

    public static int getFontHeight(float fontSize){
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 3;
    }

    public static int getFontWidth(float fontSize){
        Paint paint= new Paint();
        paint.setTextSize(fontSize);
        Rect rect = new Rect();
//返回包围整个字符串的最小的一个Rect区域
        paint.getTextBounds("清", 0, 1, rect);
        int strwidth = rect.width();
        return (int) paint.measureText("清");
    }
    public static int getFontWidth(float fontSize,String str){
        Paint paint= new Paint();
        paint.setTextSize(fontSize);
        Rect rect = new Rect();
//返回包围整个字符串的最小的一个Rect区域
        paint.getTextBounds(str+"清", 0, 2, rect);
        int strwidth = rect.width()-getFontWidth(fontSize)+1;
        return (int) (paint.measureText(str+"清")-paint.measureText("清"));
    }



}
