package com.sqing.www.mytxtread.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqing.www.mytxtread.adapter.MyBooksListAdapter;
import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.view.ReFlashListView;

import java.io.IOException;
import java.util.List;

public class BooksListActivity extends AppCompatActivity {
    private String www;
    private int category = 1;//类别
    private int page = 1;//页码

    //将网页分段传入,方便后面处理
    String html_1;
    String html_2;
    String html_3;



    private Context ctx = this;
    private ReFlashListView book_lv;
    private List<String[]> list;//数据
    private MyBooksListAdapter myBooksListAdapter;//listview适配器
    private LinearLayout ll_pb_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences txt = ctx.getSharedPreferences("txt", Context.MODE_PRIVATE);
        if (txt.getInt("night", 0) == 0) {
            this.setTheme(R.style.AppTheme);
            txt.edit().putInt("list_night", 0).commit();
        } else {
            this.setTheme(R.style.AppThemeNight);
            txt.edit().putInt("list_night", 1).commit();
        }
        setContentView(R.layout.activity_books_list);
        inif();

        getData();


    }

    private void getData() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if(www.equals("dingdian_category")) {
                        list = new com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                    }else if(www.equals("luoqiu_category")){
                        list = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                    }else if(www.equals("qingkan520_category")){
                        list = new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                    }
                    Message message = new Message();
                    message.arg1 = ONE;
                    mh.sendMessage(message);
                    page++;
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.arg1 = EXCEPTION;
                    mh.sendMessage(message);
                }

            }
        };
        thread.start();


    }

    MyHandler mh = new MyHandler();
    public final int EXCEPTION = 555;
    public final int ONE = -1;//进入界面时
    public final int UP = 0;//下拉刷新时
    public final int DOWN = 1;//上拉加载

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case ONE:
                    if (list != null)
                        myBooksListAdapter = new MyBooksListAdapter(list, ctx);
                    book_lv.setAdapter(myBooksListAdapter);
                    ll_pb_list.setVisibility(View.GONE);
                    break;
                case UP:
                    myBooksListAdapter.setList(list);
                    myBooksListAdapter.notifyDataSetChanged();
                    book_lv.reflashComplete();
                    break;
                case DOWN:
                    book_lv.loadComplete();
                    myBooksListAdapter.notifyDataSetChanged();
                    book_lv.reflashComplete();
                    break;
                case EXCEPTION:
                    Toast.makeText(ctx, "网络连接超时", Toast.LENGTH_LONG).show();
                    book_lv.loadComplete();
                    break;
            }


        }
    }

    private void inif() {
        book_lv = (ReFlashListView) findViewById(R.id.book_list);
        TextView tv_book_category = (TextView) findViewById(R.id.tv_book_category);
        html_1 = getIntent().getStringExtra("html_1");
        html_2 = getIntent().getStringExtra("html_2");
        html_3 = getIntent().getStringExtra("html_3");
        www=getIntent().getStringExtra("www");
        category = getIntent().getIntExtra(www, 1);
        tv_book_category.setText(getIntent().getStringExtra("category_name"));
        System.out.println(html_1 + category + html_2 + page + html_3);
        book_lv.setInterface(new ReFlashListView.IReflashListener() {//添加下拉刷新
            @Override
            public void onReflash() {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            page = 1;
                            if(www.equals("dingdian_category")) {
                                list = new com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                            }else if(www.equals("luoqiu_category")){
                                list = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                            }else if(www.equals("qingkan520_category")){
                                list = new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                            }

                            Message message = new Message();
                            message.arg1 = UP;
                            mh.sendMessage(message);
                            page++;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Message message = new Message();
                            message.arg1 = EXCEPTION;
                            mh.sendMessage(message);

                        }

                    }
                };
                thread.start();
            }
        });
        book_lv.setLoadMoreInterface(new ReFlashListView.ILoadMoreDataListener() {//加载更多
            @Override
            public void onLoadMoreData() {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        List<String[]> list_transition = null;
                        try {
                            if(www.equals("dingdian_category")) {
                                list_transition = new com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                            }else if(www.equals("luoqiu_category")){
                                list_transition = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                            }else if(www.equals("qingkan520_category")){
                                list_transition = new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupChapter().getChapter(html_1 + category + html_2 + page + html_3);
                            }
                            for (String[] s : list_transition) {
                                list.add(s);
                            }
                            Message message = new Message();
                            message.arg1 = DOWN;
                            mh.sendMessage(message);
                            page++;
                        } catch (IOException e) {
                            Message message = new Message();
                            message.arg1 = EXCEPTION;
                            mh.sendMessage(message);
                            e.printStackTrace();
                        }

                    }
                };
                thread.start();

            }
        });

        book_lv.setOnItemClickListener(new MyBooksOnItemClickListener());


        ll_pb_list = (LinearLayout) findViewById(R.id.ll_pb_list);
    }

    /**
     * 点击item时进入书籍详情界面
     * 用intent传书的URL name
     */
    class MyBooksOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView tv_book_url = (TextView) view.findViewById(R.id.tv_book_url);
            TextView tv_book_name = (TextView) view.findViewById(R.id.tv_book_name);
            Intent intent = new Intent();
            intent.setClass(BooksListActivity.this, BookDetailsActivity.class);
            intent.putExtra("bookurl", tv_book_url.getText() + "");
            intent.putExtra("www", www);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences txt = ctx.getSharedPreferences("txt", Context.MODE_PRIVATE);
        if (txt.getInt("night", 0) != txt.getInt("list_night", 0)) {
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
