package com.sqing.www.mytxtread.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.dao.BookDao;
import com.sqing.www.mytxtread.http.HttpGetBitmap;

import com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupDetails;
import com.sqing.www.mytxtread.userdata.Book;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    private final int EXCEPTION = 555;
    private final int IMG_OK = 1;
    private final int STRING_OK = 0;
    private final int OPEN_OK=3;


    private LinearLayout ll_pb_details;
    String[] details;
    TextView tv_book_details_name;//小说名字
    TextView tv_book_details_category;//小说类别
    TextView tv_book_details_author;//小说作者
    TextView tv_book_details_number;//小说字数
    TextView tv_book_details_time;//小说最后更新时间
    TextView tv_book_details_latest_chapter;//小说最新章节
    TextView tv_book_details_synopsis;
    ImageView iv_book_img;
    Button btn_book_details_open;//阅读按钮
    Button btn_book_details_add;
    Context ctx = this;

    List<String> catalog;
    String bookurl;
    String www;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences txt = ctx.getSharedPreferences("txt", Context.MODE_PRIVATE);
        if(txt.getInt("night",0)==0){
            this.setTheme(R.style.AppTheme);
            txt.edit().putInt("details_night",0).commit();
        }else{
            this.setTheme(R.style.AppThemeNight);
            txt.edit().putInt("details_night",1).commit();
        }
        setContentView(R.layout.activity_book_details);
        info();
        Intent intent = getIntent();

        bookurl = intent.getStringExtra("bookurl");
        www =intent.getStringExtra("www");
        thread(bookurl);


    }

    /**
     * 开启线程获取数据
     * @param bookurl
     */
    private void thread(final String bookurl) {
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();

                try {
                    if(www.equals("dingdian_category")) {
                        details = new JsoupDetails().getDetails(bookurl);
                    }else if(www.equals("luoqiu_category")){
                        details = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupDetails().getDetails(bookurl);
                    }else if(www.equals("qingkan520_category")){
                        details = new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupDetails().getDetails(bookurl);
                    }
                    msg.obj = details;
                    msg.arg1 = STRING_OK;
                    bdh.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.arg1 = EXCEPTION;
                    bdh.sendMessage(msg);
                }


            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                boolean img_ok = true;
                while (img_ok) {
                    if (details != null) {
                        Message msg = new Message();
                        if(details[1]!="")
                             bitmap = new HttpGetBitmap().returnBitMap(details[1]);
                        msg.obj = bitmap;
                        msg.arg1 = IMG_OK;
                        bdh.sendMessage(msg);
                        img_ok = false;
                    }
                }
            }
        }.start();
    }
    private void info() {
        tv_book_details_name = (TextView) findViewById(R.id.tv_book_details_name);
        tv_book_details_category = (TextView) findViewById(R.id.tv_book_details_category);
        tv_book_details_author = (TextView) findViewById(R.id.tv_book_details_author);
        tv_book_details_number = (TextView) findViewById(R.id.tv_book_details_number);
        tv_book_details_time = (TextView) findViewById(R.id.tv_book_details_time);
        tv_book_details_latest_chapter = (TextView) findViewById(R.id.tv_book_details_latest_chapter);
        tv_book_details_synopsis = (TextView) findViewById(R.id.tv_book_details_synopsis);
        btn_book_details_open = (Button) findViewById(R.id.btn_book_details_open);
        btn_book_details_add= (Button) findViewById(R.id.btn_book_details_add);
        iv_book_img = (ImageView) findViewById(R.id.iv_book_img);
        ll_pb_details = (LinearLayout) findViewById(R.id.ll_pb_details);
    }

    BookDetailsHandler bdh = new BookDetailsHandler();
    String[] strings;

    class BookDetailsHandler extends Handler {//对线程中的数据填充到控件中

        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case IMG_OK:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if(!details[1].equals(""))
                        iv_book_img.setImageBitmap(bitmap);
                    ll_pb_details.setVisibility(View.GONE);
                    break;
                case STRING_OK:
                    strings = (String[]) msg.obj;
                    tv_book_details_name.setText(strings[0]);

                    BookDao dao=new BookDao(ctx);
                    boolean query = dao.query(new Book(strings[0], "", "", 0, 0,"",""));
                    if(query){
                        btn_book_details_add.setText("删除");
                        btn_book_details_add.setBackgroundColor(Color.parseColor("#888888"));
                    }

                    tv_book_details_category.setText(strings[2]);
                    tv_book_details_author.setText(strings[3]);
                    tv_book_details_number.setText(strings[4]);
                    tv_book_details_time.setText(strings[5]);
                    tv_book_details_synopsis.setText(strings[6]);
                    tv_book_details_latest_chapter.setText(strings[7]);

                    break;
                case EXCEPTION:
                    Toast.makeText(ctx, "网络连接超时", Toast.LENGTH_LONG).show();
                    break;
                case OPEN_OK:
                    btn_book_details_open.setBackgroundColor(Color.parseColor("#3F51B5"));
                    btn_book_details_open.setClickable(true);
                    btn_book_details_open.setText("阅读");

                    break;


            }
        }
    }

    /**
     * 打开阅读界面
     * @param view
     */
    public void OpenBook(View view) {
        btn_book_details_open.setBackgroundColor(getResources().getColor(R.color.colorGray));
        btn_book_details_open.setClickable(false);
        btn_book_details_open.setText("正在打开...");
        new Thread() {
            @Override
            public void run() {
                try {
                    if(www.equals("dingdian_category")) {
                        catalog = new com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupCatalog().getCatalog(strings[8]);
                    }else if(www.equals("luoqiu_category")){
                        catalog = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupCatalog().getCatalog(strings[8]);
                    }else if(www.equals("qingkan520_category")){
                        catalog = new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupCatalog().getCatalog(strings[8]);
                    }
                    String content = ""+tv_book_details_name.getText()+"_____";
                    for (String s : catalog) {
                        content += s.replaceAll("-----","   ") + "-----";
                    }
                    GoRead(content);
                    String file_catalog = Environment.getExternalStorageDirectory() + "/yuedu/catalog";
                    File dir = new File(file_catalog, strings[0] + ".txt");
                    if (!dir.exists()) {
                        //在指定的文件夹中创建文件
                        dir.createNewFile();
                        //第二个参数意义是说是否以append方式添加内容
                        BufferedWriter bw = new BufferedWriter(new FileWriter(dir, false));
                        bw.write(content);
                        bw.flush();
                    }

                    Message msg = new Message();
                    msg.arg1 = OPEN_OK;
                    bdh.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.arg1 = EXCEPTION;
                    bdh.sendMessage(msg);
                }
            }
        }.start();
    }


    Bitmap bitmap;//图片数据

    /**
     *  保存img
     * @return
     */
    private String saveBitmap() {

        File file = new File(tv_book_details_name.getText()+".png");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream localFileOutputStream1 = openFileOutput(file+"",Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, localFileOutputStream1);
            localFileOutputStream1.flush();
            localFileOutputStream1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file+"";

    }
    /**
     * 把书调加到数据库
     *
     * @param view
     */
    public void AddBook(View view) {
        String img = "";
        if(details[1]!="") {
            img= saveBitmap();
        }
        BookDao dao=new BookDao(ctx);
        Book book=new Book(tv_book_details_name.getText()+"",img,strings[8],0,0,bookurl,www);
        Button btn = (Button) view;
        if(!btn.getText().equals("删除")) {
            dao.add(book);
            btn.setText("删除");
            btn_book_details_add.setBackgroundColor(Color.parseColor("#888888"));
        } else{
            File f = new File(tv_book_details_name.getText()+".png");
            if (f.exists()) {
                f.delete();
            }
            dao.delete(book);
            btn.setText("加入书架");
            btn_book_details_add.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
    }


    private void GoRead(String catalog_url) {
        Intent intent = new Intent();
        intent.setClass(BookDetailsActivity.this, BookReadActivity.class);
        intent.putExtra("book", catalog_url);
        intent.putExtra("www", www);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences txt = ctx.getSharedPreferences("txt", Context.MODE_PRIVATE);
        if (txt.getInt("night",0) != txt.getInt("details_night",0)) {
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
