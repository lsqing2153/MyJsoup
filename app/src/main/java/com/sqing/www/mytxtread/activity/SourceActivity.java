package com.sqing.www.mytxtread.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.dao.BookDao;

import java.io.IOException;
import java.net.URLEncoder;

public class SourceActivity extends AppCompatActivity {
    Context ctx=this;
    ListView lv_source;
    LinearLayout ll_pb_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        if(txt.getInt("night",0)==0){
            this.setTheme(R.style.AppTheme);
        }else{
            this.setTheme(R.style.AppThemeNight);
        }
        setContentView(R.layout.activity_source);
        info();
    }
    String[] dingdianxiaoshuo;
    String[] luoqiuxiaoshuo;
    String[] qingkan;
    String www;
    String name;
    private void info() {
        lv_source = (ListView) findViewById(R.id.lv_source);
        ll_pb_list = (LinearLayout) findViewById(R.id.ll_pb_list);
        www= getIntent().getStringExtra("www");
        name = getIntent().getStringExtra("name").replaceAll("《", "").replaceAll("》", "");

        new Thread(){
            @Override
            public void run() {
                try {
                    dingdianxiaoshuo = new com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupSearch().getSearchResult("http://www.23us.so/search/?searchkey=" + name);
                    luoqiuxiaoshuo = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupSearch().getSearchResult("http://zhannei.baidu.com/cse/search?s=17782022296417237613&entry=1&ie=gbk&q=" + URLEncoder.encode(name, "GBK"));
                    qingkan = new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupSearch().getSearchResult("http://www.qingkan520.com/novel.php?action=search&searchtype=novelname&searchkey=" + URLEncoder.encode(name, "GBK"));
                    Message msg = new Message();
                    msg.arg1=1;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    Message msg = new Message();
                    msg.arg1=0;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();

    }



    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==0){
                Toast.makeText(ctx,"网络连接超时",Toast.LENGTH_LONG).show();
            }else {
                lv_source.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return 3;
                    }

                    @Override
                    public Object getItem(int position) {
                        return position;
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View inflate = View.inflate(ctx, R.layout.list_source, null);

                        TextView tv_source_www = (TextView) inflate.findViewById(R.id.tv_source_www);
                        TextView tv_source_name = (TextView) inflate.findViewById(R.id.tv_source_name);
                        TextView tv_source_date = (TextView) inflate.findViewById(R.id.tv_source_date);
                        TextView tv_source_chapter = (TextView) inflate.findViewById(R.id.tv_source_chapter);
                        TextView tv_source_url = (TextView) inflate.findViewById(R.id.tv_source_url);
                        if (position == 0) {
                            tv_source_www.setText("http://www.23us.so/");
                            if (!dingdianxiaoshuo[0].equals("")) {
                                tv_source_name.setText(dingdianxiaoshuo[0]);
                                tv_source_date.setText(dingdianxiaoshuo[2]);
                                tv_source_chapter.setText(dingdianxiaoshuo[3]);
                                tv_source_url.setText(dingdianxiaoshuo[1]);
                                if (www.equals("dingdian_category")) {
                                    inflate.findViewById(R.id.ll_source_this).setVisibility(View.VISIBLE);
                                }
                            } else {
                                tv_source_name.setText("无");
                                tv_source_name.setTextSize(25);
                                tv_source_date.setVisibility(View.GONE);
                                tv_source_chapter.setVisibility(View.GONE);
                                tv_source_url.setVisibility(View.GONE);
                            }
                        } else if (position == 1) {
                            tv_source_www.setText("http://www.luoqiu.com/");
                            if (!luoqiuxiaoshuo[0].equals("")) {
                                tv_source_name.setText(luoqiuxiaoshuo[0]);
                                tv_source_date.setText(luoqiuxiaoshuo[2]);
                                tv_source_chapter.setText(luoqiuxiaoshuo[3]);
                                tv_source_url.setText(luoqiuxiaoshuo[1]);
                                if (www.equals("luoqiu_category")) {
                                    inflate.findViewById(R.id.ll_source_this).setVisibility(View.VISIBLE);
                                }
                            } else {
                                tv_source_name.setText("无");
                                tv_source_date.setVisibility(View.GONE);
                                tv_source_chapter.setVisibility(View.GONE);
                                tv_source_url.setVisibility(View.GONE);
                            }

                        } else if (position == 2) {
                            tv_source_www.setText("http://www.qingkan520.com/");
                            if (!qingkan[0].equals("")) {
                                tv_source_name.setText(qingkan[0]);
                                tv_source_date.setText(qingkan[2]);
                                tv_source_chapter.setText(qingkan[3]);
                                tv_source_url.setText(qingkan[1]);
                                if (www.equals("qingkan520_category")) {
                                    inflate.findViewById(R.id.ll_source_this).setVisibility(View.VISIBLE);
                                }
                            } else {
                                tv_source_name.setText("无");
                                tv_source_date.setVisibility(View.GONE);
                                tv_source_chapter.setVisibility(View.GONE);
                                tv_source_url.setVisibility(View.GONE);
                            }

                        }
                        return inflate;
                    }
                });
                ll_pb_list.setVisibility(View.GONE);


                lv_source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv_source_name = (TextView) view.findViewById(R.id.tv_source_name);
                        TextView tv_source_url = (TextView) view.findViewById(R.id.tv_source_url);
                        TextView tv_source_www = (TextView) view.findViewById(R.id.tv_source_www);
                        if (!tv_source_name.getText().equals("无")) {
                            Intent intent = new Intent();
                            intent.putExtra("url", tv_source_url.getText());

                            BookDao dao = new BookDao(ctx);
                            dao.update(name, "book_url", tv_source_url.getText().toString());
                            if (tv_source_www.getText().equals("http://www.qingkan520.com/")) {
                                intent.putExtra("www", "qingkan520_category");
                                dao.update(name, "www", "qingkan520_category");
                            } else if (tv_source_www.getText().equals("http://www.luoqiu.com/")) {
                                intent.putExtra("www", "luoqiu_category");
                                dao.update(name, "www", "luoqiu_category");
                            } else if (tv_source_www.getText().equals("http://www.23us.so/")) {
                                intent.putExtra("www", "dingdian_category");
                                dao.update(name, "www", "dingdian_category");
                            }
                            setResult(10086, intent);


                            finish();
                        }
                    }
                });
            }
        }
    };

    /**
     * 响应返回按钮
     *
     * @param view
     */
    public void ib_close(View view) {
        finish();
    }
}
