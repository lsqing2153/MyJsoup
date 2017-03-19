package com.sqing.www.mytxtread.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.adapter.SearchListAdapter;
import com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupSearch;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2, t3;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        if (txt.getInt("night", 0) == 0) {
            this.setTheme(R.style.AppTheme);
            txt.edit().putInt("search_night",0).commit();
        } else {
            this.setTheme(R.style.AppThemeNight);
            txt.edit().putInt("search_night",1).commit();
        }
        setContentView(R.layout.activity_search);

        InitImageView();
        InitTextView();
        InitViewPager();
    }
    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.tv_www_1);
        t2 = (TextView) findViewById(R.id.tv_www_2);
        t3 = (TextView) findViewById(R.id.tv_www_3);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
    }
    ListView listView,listView1,listView2;


    Context ctx=this;
    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager_search);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        listView = (ListView) mInflater.inflate(R.layout.search_list, null);

        listView1 = (ListView) mInflater.inflate(R.layout.search_list, null);

        listView2= (ListView) mInflater.inflate(R.layout.search_list, null);



        listViews.add(listView);
        listViews.add(listView1);
        listViews.add(listView2);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.this_display)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
    List<String[]> dingdianxiaoshuo,luoqiuxiaoshuo,qingkan;
    public void onSearch(View view){
        EditText et_name = (EditText) findViewById(R.id.et_name);
        final String name = et_name.getText() + "";
        if(!name.equals("")) {
            Message msg=new Message();
            msg.arg1=ING;
            handler.sendMessage(msg);
            new Thread() {
                @Override
                public void run() {
                    try {
                        dingdianxiaoshuo = new JsoupSearch().getSearchResult("http://www.23us.so/search/?searchkey=" + name, 1);
                        luoqiuxiaoshuo = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupSearch().getSearchResult("http://zhannei.baidu.com/cse/search?s=17782022296417237613&entry=1&ie=gbk&q=" + URLEncoder.encode(name, "GBK"), 2);
                        qingkan = new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupSearch().getSearchResult("http://www.qingkan520.com/novel.php?action=search&searchtype=novelname&searchkey=" + URLEncoder.encode(name, "GBK"), 3);
                        Message msg=new Message();
                        msg.arg1=OK;
                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Message msg=new Message();
                        msg.arg1=OK;
                        handler.sendMessage(msg);
                    }
                }
            }.start();
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    final int OK=1;
    final int ING=0;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case OK:
                    if(dingdianxiaoshuo.size()>0) {
                        listView.setAdapter(new SearchListAdapter(dingdianxiaoshuo, ctx));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView tv_search_url = (TextView) view.findViewById(R.id.tv_search_url);
                                String www="dingdian_category";
                                Intent intent=new Intent();
                                intent.setClass(SearchActivity.this,BookDetailsActivity.class);
                                intent.putExtra("bookurl",tv_search_url.getText());
                                intent.putExtra("www",www);
                                startActivity(intent);
                            }
                        });
                    }
                    if(luoqiuxiaoshuo.size()>0) {
                        listView1.setAdapter(new SearchListAdapter(luoqiuxiaoshuo, ctx));
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView tv_search_url = (TextView) view.findViewById(R.id.tv_search_url);
                                String www="luoqiu_category";
                                Intent intent=new Intent();
                                intent.setClass(SearchActivity.this,BookDetailsActivity.class);
                                intent.putExtra("bookurl",tv_search_url.getText());
                                intent.putExtra("www",www);
                                startActivity(intent);
                            }
                        });
                    }
                    if(qingkan.size()>0) {
                        listView2.setAdapter(new SearchListAdapter(qingkan, ctx));
                        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView tv_search_url = (TextView) view.findViewById(R.id.tv_search_url);
                                String www="qingkan520_category";
                                Intent intent=new Intent();
                                intent.setClass(SearchActivity.this,BookDetailsActivity.class);
                                intent.putExtra("bookurl",tv_search_url.getText());
                                intent.putExtra("www",www);
                                startActivity(intent);
                            }
                        });
                    }
                    findViewById(R.id.ll_pb_saerch).setVisibility(View.GONE);
                    break;
                case ING:
                    findViewById(R.id.ll_pb_saerch).setVisibility(View.VISIBLE);
                    findViewById(R.id.pb_saerch).setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences txt = ctx.getSharedPreferences("txt", Context.MODE_PRIVATE);
        if (txt.getInt("night",0) != txt.getInt("search_night",0)) {
            recreate();
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
