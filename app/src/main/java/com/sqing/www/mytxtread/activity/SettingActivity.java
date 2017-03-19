package com.sqing.www.mytxtread.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sqing.www.mytxtread.R;

public class SettingActivity extends AppCompatActivity {
    TextView tv_setting_size,tv_setting_show;
    int fontsize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        if(txt.getInt("night",0)==0){
            this.setTheme(R.style.AppTheme);
        }else{
            this.setTheme(R.style.AppThemeNight);
        }
        setContentView(R.layout.activity_setting);
        info();
    }

    private void info() {
        tv_setting_size= (TextView) findViewById(R.id.tv_setting_size);
        tv_setting_show= (TextView) findViewById(R.id.tv_setting_show);
        fontsize = getSharedPreferences("txt", Context.MODE_PRIVATE).getInt("fontsize", 20);
        tv_setting_size.setText(fontsize+"");
        tv_setting_show.setTextSize(fontsize);


        //设置背景
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        int bj_color = txt.getInt("bj_color", 1);
        switch (bj_color){
            case 1:
                tv_setting_show.setBackground(getResources().getDrawable(R.color.colorBjGreen));
                break;
            case 2:
                tv_setting_show.setBackground(getResources().getDrawable(R.color.colorBjGray));
                break;
            case 3:
                tv_setting_show.setBackground(getResources().getDrawable(R.color.colorBjBrown));
                break;
        }

        //获取背景按钮控件
        if(btn_book_read_bj_green==null  || btn_book_read_bj_gray==null || btn_book_read_bj_brown==null){
            btn_book_read_bj_green= (Button) findViewById(R.id.btn_book_read_bj_green);
            btn_book_read_bj_gray= (Button) findViewById(R.id.btn_book_read_bj_gray);
            btn_book_read_bj_brown= (Button) findViewById(R.id.btn_book_read_bj_brown);
        }

        if(((ColorDrawable)tv_setting_show.getBackground()).getColor()==getResources().getColor(R.color.colorBjGreen)){
            btn_book_read_bj_green.setBackground(getResources().getDrawable(R.drawable.book_bj_green_in));
        }else  if(((ColorDrawable)tv_setting_show.getBackground()).getColor()==getResources().getColor(R.color.colorBjGray)){
            btn_book_read_bj_gray.setBackground(getResources().getDrawable(R.drawable.book_bj_gray_in));
        }else{
            btn_book_read_bj_brown.setBackground(getResources().getDrawable(R.drawable.book_bj_brown_in));
        }

    }

    public void reduce(View view){
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1=txt.edit();
        editor1.putInt("fontsize",--fontsize);
        editor1.commit();
        tv_setting_show.setTextSize(fontsize);
        tv_setting_size.setText(fontsize+"");
    }

    public void add(View view){
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1=txt.edit();
        editor1.putInt("fontsize",++fontsize);
        editor1.commit();
        tv_setting_show.setTextSize(fontsize);
        tv_setting_size.setText(fontsize+"");
    }

    public void resetting(View view){
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1=txt.edit();
        editor1.putInt("fontsize",20);
        editor1.putInt("bj_color",1);
        editor1.commit();
        fontsize=20;
        tv_setting_show.setTextSize(fontsize);
        tv_setting_size.setText(fontsize+"");
        btn_book_read_bj_green.setBackground(getResources().getDrawable(R.drawable.book_bj_green_in));
        btn_book_read_bj_gray.setBackground(getResources().getDrawable(R.drawable.book_bj_gray));
        btn_book_read_bj_brown.setBackground(getResources().getDrawable(R.drawable.book_bj_brown));
        tv_setting_show.setBackground(getResources().getDrawable(R.color.colorBjGreen));
    }

    Button btn_book_read_bj_green,btn_book_read_bj_gray,btn_book_read_bj_brown;

    /**
     * 背景切换
     * @param view
     */
    public void bjSwitch(View view){
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1=txt.edit();
        switch (view.getId()){
            case R.id.btn_book_read_bj_green:
                view.setBackground(getResources().getDrawable(R.drawable.book_bj_green_in));
                btn_book_read_bj_gray.setBackground(getResources().getDrawable(R.drawable.book_bj_gray));
                btn_book_read_bj_brown.setBackground(getResources().getDrawable(R.drawable.book_bj_brown));
                if(((ColorDrawable)tv_setting_show.getBackground()).getColor()!=getResources().getColor(R.color.colorBjGreen)){
                    tv_setting_show.setBackground(getResources().getDrawable(R.color.colorBjGreen));
                    editor1.putInt("bj_color",1);
                    editor1.commit();
                }
                break;
            case R.id.btn_book_read_bj_gray:
                view.setBackground(getResources().getDrawable(R.drawable.book_bj_gray_in));
                btn_book_read_bj_green.setBackground(getResources().getDrawable(R.drawable.book_bj_green));
                btn_book_read_bj_brown.setBackground(getResources().getDrawable(R.drawable.book_bj_brown));
                if(((ColorDrawable)tv_setting_show.getBackground()).getColor()!=getResources().getColor(R.color.colorBjGray)){
                    tv_setting_show.setBackground(getResources().getDrawable(R.color.colorBjGray));
                    editor1.putInt("bj_color",2);
                    editor1.commit();
                }
                break;
            case R.id.btn_book_read_bj_brown:
                view.setBackground(getResources().getDrawable(R.drawable.book_bj_brown_in));
                btn_book_read_bj_gray.setBackground(getResources().getDrawable(R.drawable.book_bj_gray));
                btn_book_read_bj_green.setBackground(getResources().getDrawable(R.drawable.book_bj_green));
                if(((ColorDrawable)tv_setting_show.getBackground()).getColor()!=getResources().getColor(R.color.colorBjBrown)){
                    tv_setting_show.setBackground(getResources().getDrawable(R.color.colorBjBrown));
                    editor1.putInt("bj_color",3);
                    editor1.commit();
                }
                break;
        }
    }




    /**
     * 响应返回按钮
     *
     * @param view
     */
    public void ib_close(View view) {
        finish();
    }
}
