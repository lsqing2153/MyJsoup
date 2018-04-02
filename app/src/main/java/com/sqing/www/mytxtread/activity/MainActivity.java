package com.sqing.www.mytxtread.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.dao.BookDao;

import com.sqing.www.mytxtread.jurisdiction.Jurisdiction;
import com.sqing.www.mytxtread.userdata.Book;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context ctx = this;
    GridView gv_books;

    boolean open_ing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        if (txt.getInt("night", 0) == 0) {
            this.setTheme(R.style.AppTheme);
        } else {
            this.setTheme(R.style.AppThemeNight);
        }
        setContentView(R.layout.activity_main);

        //检查是否第一次启动
        firstStart();


        newFile(Environment.getExternalStorageDirectory() + "", 0);

        info();

        new Jurisdiction(ctx,this).jurisdiction_sd();

    }

    private void firstStart() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);

        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isFirstRun) {
            editor.putBoolean("isFirstRun", false);
            editor.commit();

            SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = txt.edit();
            editor1.putInt("fontsize", 20);//初始化字体大小

            editor1.putInt("bj_color", 1);//初始化阅读背景颜色

            editor1.putInt("night", 0);

            editor1.commit();
        }
    }

    List<Book> books;
    BookDao dao = new BookDao(ctx);

    private void info() {
        gv_books = (GridView) findViewById(R.id.gv_books);
        load();
    }


    private void load() {
        books = dao.queryAll();
        gv_books.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return books.size() + 1;
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View v;
                if (i == books.size()) {
                    v = View.inflate(ctx, R.layout.btn_add, null);
                    Button btn_add = (Button) v.findViewById(R.id.btn_add);
                    btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, BookCategoryActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    v = View.inflate(ctx, R.layout.gv_book, null);
                    ImageView iv_book_gv_img = (ImageView) v.findViewById(R.id.iv_book_gv_img);
                    if(!books.get(i).img.equals("")) {
                        try {
                            FileInputStream localStream = openFileInput(books.get(i).img);
                            Bitmap bitmap = BitmapFactory.decodeStream(localStream);
                            iv_book_gv_img.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    TextView tv_book_gv_name = (TextView) v.findViewById(R.id.tv_book_gv_name);
                    tv_book_gv_name.setText(books.get(i).name);
                    TextView tv_book_gv_url = (TextView) v.findViewById(R.id.tv_book_gv_url);
                    tv_book_gv_url.setText(books.get(i).url);
                    TextView tv_book_gv_this_index = (TextView) v.findViewById(R.id.tv_book_gv_this_index);
                    tv_book_gv_this_index.setText(books.get(i).thischapter + "");
                    TextView tv_book_gv_page = (TextView) v.findViewById(R.id.tv_book_gv_page);
                    tv_book_gv_page.setText(books.get(i).page + "");
                }
                return v;
            }
        });
        //点击打开
        gv_books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (open_ing) {
                    open_ing = false;
                    new Thread() {
                        @Override
                        public void run() {
                            File fileName = new File(Environment.getExternalStorageDirectory() + "/yuedu/catalog", books.get(i).name + ".txt");
                            if (fileName.exists()) {
                                String res = "";
                                try {
                                    FileInputStream fin = new FileInputStream(fileName);

                                    int length = fin.available();

                                    byte[] buffer = new byte[length];
                                    fin.read(buffer);

                                    res = new String(buffer);
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, BookReadActivity.class);
                                    intent.putExtra("book", res);
                                    intent.putExtra("thischapter", books.get(i).thischapter);
                                    intent.putExtra("page", books.get(i).page);
                                    intent.putExtra("www", books.get(i).www);
                                    startActivity(intent);
                                    fin.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                try {
                                    List<String> catalog;
                                    if (books.get(i).www.equals("dingdian_category")) {
                                        catalog = new com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupCatalog().getCatalog(books.get(i).url);
                                    } else if (books.get(i).www.equals("luoqiu_category")){
                                        catalog = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupCatalog().getCatalog(books.get(i).url);
                                    } else {
                                        catalog = new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupCatalog().getCatalog(books.get(i).url);
                                    }
                                    String content = "" + books.get(i).name + "_____";
                                    for (String s : catalog) {
                                        content += s.replace("-----", "   ") + "-----";
                                    }
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, BookReadActivity.class);
                                    intent.putExtra("book", content);
                                    intent.putExtra("thischapter", books.get(i).thischapter);
                                    intent.putExtra("page", books.get(i).page);
                                    intent.putExtra("www", books.get(i).www);
                                    startActivity(intent);
                                    //在指定的文件夹中创建文件
                                    fileName.createNewFile();
                                    //第二个参数意义是说是否以append方式添加内容
                                    BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false));
                                    bw.write(content);
                                    bw.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                }
            }
        });
        gv_books.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                AlertDialog.Builder customizeDialog = new AlertDialog.Builder(ctx);
                customizeDialog.setTitle(books.get(i).name).setSingleChoiceItems(new String[]{"删除", "书籍详情"}, 3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            dao.delete(books.get(index));
                            load();
                        } else if (i == 1) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, BookDetailsActivity.class);
                            intent.putExtra("bookurl", books.get(index).details_url);
                            intent.putExtra("www", books.get(index).www);
                            startActivity(intent);
                        }
                        dialogInterface.dismiss();
                    }
                });
                customizeDialog.show();
                return true;
            }
        });


    }

    private void newFile(String sd, int i) {
        String catalog;
        if (i == 0) {
            catalog = sd + "/yuedu";
        } else {
            catalog = sd;
        }
        File file = new File(catalog);
        if (!file.exists()) {
            //按照指定的路径创建文件夹
            file.mkdir();
        }
        if (i == 0)
            newFile(catalog + "/catalog", 1);
    }

    int one = 1;

    @Override
    protected void onStart() {
        super.onStart();
        open_ing = true;//当从阅读或从其他界面回来时,可以打开阅读界面-----不让多次打开阅读界面

        if (one > 1) {//当不是第一次进入主界面时,重构一下界面,看是不是有新的书籍加入
//            load();
            recreate();
        } else {
            one++;
        }
    }


    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void openSetting(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }
    public void openSearch(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

}
