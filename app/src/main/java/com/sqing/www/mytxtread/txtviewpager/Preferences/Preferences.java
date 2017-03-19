package com.sqing.www.mytxtread.txtviewpager.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sqing on 2016/11/6.
 */

public class Preferences {
    SharedPreferences sp;
    public Preferences(Context cxt){
        sp=cxt.getSharedPreferences("txt",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("fontsize",18);
        editor.commit();
    }


}
