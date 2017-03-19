package com.sqing.www.mytxtread.txtviewpager.set;

/**
 * Created by sqing on 2016/11/6.
 */

public class TextSize {
    public static int adjustFontSize(int screenWidth, int screenHeight) {
        screenWidth=screenWidth>screenHeight?screenWidth:screenHeight;
        /**
         * 1. 在视图的 onsizechanged里获取视图宽度，一般情况下默认宽度是320，所以计算一个缩放比率
         rate = (float) w/320   w是实际宽度
         2.然后在设置字体尺寸时 paint.setTextSize((int)(8*rate));   8是在分辨率宽为320 下需要设置的字体大小
         实际字体大小 = 默认字体大小 x  rate
         */
        int rate = (int)(5*(float) screenWidth/320);
        return rate<15?15:rate; //字体太小也不好看的
    }
}
