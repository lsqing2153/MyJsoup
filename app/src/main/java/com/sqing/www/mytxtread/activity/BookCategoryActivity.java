package com.sqing.www.mytxtread.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.sqing.www.mytxtread.R;


public class BookCategoryActivity extends AppCompatActivity {
    Context ctx=this;
    GridView gv_book_category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences txt = ctx.getSharedPreferences("txt", Context.MODE_PRIVATE);
        if(txt.getInt("night",0)==0){
            this.setTheme(R.style.AppTheme);
        }else{
            this.setTheme(R.style.AppThemeNight);
        }
        setContentView(R.layout.activity_book_category);
        info();

    }

    private void info() {
        gv_book_category = (GridView) findViewById(R.id.gv_book_category);
        gv_book_category.setAdapter(new BaseAdapter() {
            String[] dingdian_category =new String[]{"玄幻魔法","武侠修真","都市言情","历史军事","网游竞技","科幻小说","恐怖灵异",
                    "女生小说","其他小说","全本小说"};
            String[] luoqiu_category =new String[]{"全本小说","玄幻魔法","武侠修真","都市言情","历史军事","游戏竞技","科幻小说","恐怖灵异",
                    "同人小说","商战职场","文学美文","女生小说"};
            String[] qingkan520_category =new String[]{"玄幻魔法","武侠修真","现代都市","历史军事","游戏竞技","科幻灵异",
                    "女生言情","其他小说","短篇杂文"};
            @Override
            public int getCount() {
                return 6+dingdian_category.length+2+luoqiu_category.length+qingkan520_category.length+3;
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
            public View getView(final int i, View view, ViewGroup viewGroup) {
                View v = View.inflate(ctx, R.layout.gv_category, null);
                Button btn = (Button) v.findViewById(R.id.btn_book_category);
                if(i<=2){
                   if(i==0) {
                       btn.setText("顶点小说");
                       btn.setBackgroundColor(Color.GRAY);
                   }
                }else if(i<=2+dingdian_category.length){
                    btn.setText(dingdian_category[i-3]);
                    if (i != 2+dingdian_category.length ) {
                        final int item = i + 1-2;
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setClass(BookCategoryActivity.this, BooksListActivity.class);
                                intent.putExtra("html_1", "http://www.23us.so/list/");
                                intent.putExtra("html_2", "_");
                                intent.putExtra("html_3", ".html");
                                intent.putExtra("www", "dingdian_category");
                                intent.putExtra("dingdian_category", item-2+1);
                                intent.putExtra("category_name", ((Button)view).getText());
                                System.out.println(item-2);
                                startActivity(intent);
                            }
                        });
                    } else {
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setClass(BookCategoryActivity.this, BooksListActivity.class);
                                intent.putExtra("html_1", "http://www.23us.so/modules/article/articlelist.php?fullflag=");
                                intent.putExtra("html_2", "&page=");
                                intent.putExtra("html_3", "");
                                intent.putExtra("www", "dingdian_category");
                                intent.putExtra("dingdian_category", 1);
                                intent.putExtra("category_name", ((Button)view).getText());
                                startActivity(intent);
                            }
                        });
                    }
                }else if(i<=2+dingdian_category.length +2){

                }else if(i<=2+dingdian_category.length +2+3){
                    if(i==2+dingdian_category.length +2+1){
                        btn.setText("落秋中文");
                        btn.setBackgroundColor(Color.GRAY);
                    }
                }else if(i<=2+dingdian_category.length +2+3+luoqiu_category.length){
                    btn.setText(luoqiu_category[i-(2+dingdian_category.length +2+3)-1]);
                    if(i-(2+dingdian_category.length +2+3)-1==0){
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setClass(BookCategoryActivity.this, BooksListActivity.class);
                                intent.putExtra("html_1", "http://www.luoqiu.com/modules/article/index_299.php?fullflag=");
                                intent.putExtra("html_2", "&page=");
                                intent.putExtra("html_3", "");
                                intent.putExtra("www", "luoqiu_category");
                                intent.putExtra("luoqiu_category", 1);
                                intent.putExtra("category_name", luoqiu_category[i - (2 + dingdian_category.length + 2 + 3) - 1]);
                                startActivity(intent);
                            }
                        });
                    }else {
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setClass(BookCategoryActivity.this, BooksListActivity.class);
                                intent.putExtra("html_1", "http://www.luoqiu.com/class/");
                                intent.putExtra("html_2", "_");
                                intent.putExtra("html_3", ".html");
                                intent.putExtra("www", "luoqiu_category");
                                intent.putExtra("luoqiu_category", i - (2 + dingdian_category.length + 2 + 3) - 1);
                                intent.putExtra("category_name", luoqiu_category[i - (2 + dingdian_category.length + 2 + 3) - 1]);
                                startActivity(intent);
                            }
                        });
                    }
                }else if(i<=2+dingdian_category.length +2+3+luoqiu_category.length+3){
                    if(i==2+dingdian_category.length +2+3+luoqiu_category.length+1){
                        btn.setText("请看小说");
                        btn.setBackgroundColor(Color.GRAY);
                    }
                }else if(i<=2+dingdian_category.length +2+3+luoqiu_category.length+3+qingkan520_category.length){
                    btn.setText(qingkan520_category[i-(2+dingdian_category.length +2+3+3+luoqiu_category.length)-1]);

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setClass(BookCategoryActivity.this, BooksListActivity.class);
                                intent.putExtra("html_1", "http://www.qingkan520.com/shuku/");
                                intent.putExtra("html_2", "_0_0_0_0_0_0_0_");
                                intent.putExtra("html_3", ".html");
                                intent.putExtra("www", "qingkan520_category");
                                intent.putExtra("qingkan520_category", i - (2 + dingdian_category.length + 2 + 3+3+luoqiu_category.length));
                                intent.putExtra("category_name", qingkan520_category[i - (2 + dingdian_category.length + 2 + 3+3+luoqiu_category.length) - 1]);
                                startActivity(intent);
                            }
                        });

                }

                return v;
            }
        });

    }



    int one = 1;
    @Override
    protected void onStart() {
        super.onStart();
        if (one > 1) {
            recreate();
        } else {
            one++;
        }
    }


    /**
     * 响应返回按钮
     * @param view
     */
    public void ib_close(View view){
        finish();
    }
}
