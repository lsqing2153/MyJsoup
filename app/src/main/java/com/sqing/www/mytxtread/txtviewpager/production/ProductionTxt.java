package com.sqing.www.mytxtread.txtviewpager.production;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.sqing.www.mytxtread.txtviewpager.get.FontSize;


/**
 * Created by sqing on 2016/11/6.
 */

public class ProductionTxt {
    Context ctx;
    public ProductionTxt(Context context){
        this.ctx =context;
    }

    public int getStringNumber(){
        return getRow()*getCol();
    }


    public int getCol(){//行数
        DisplayMetrics metric = getDisplayMetrics();
        int height =metric.heightPixels;//屏幕高度（像素）
        SharedPreferences sp= ctx.getSharedPreferences("txt",Context.MODE_PRIVATE);
        int fontsize=sp.getInt("fontsize",20);
        int col= (int) (height/ FontSize.getFontHeight(fontsize)/metric.density+0.5);//height屏幕高度（像素）,,metric.density --屏幕密度对不同屏幕的适配
//        System.out.println(height+".............."+FontSize.getFontHeight(fontsize)+"....."+"......"+col);
        return col-3;
    }

    /**
     *
     * @return 一行能放下的字数
     */
    public int getRow(){//一行字数
        DisplayMetrics metric = getDisplayMetrics();
        int width = metric.widthPixels;// 屏幕宽度（像素）
        SharedPreferences sp= ctx.getSharedPreferences("txt",Context.MODE_PRIVATE);
        int fontsize=sp.getInt("fontsize",20);
        int row= (int) (width / FontSize.getFontWidth(fontsize)/metric.density+0.5);//width是屏幕宽度（像素）,,metric.density --屏幕密度对不同屏幕的适配
//        System.out.println(width+".............."+FontSize.getFontWidth(fontsize)+"......."+"......"+row+".....");
        return row-2;
    }

    @NonNull
    private DisplayMetrics getDisplayMetrics() {//为了获取宽高,像素密度
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric;
    }

}
