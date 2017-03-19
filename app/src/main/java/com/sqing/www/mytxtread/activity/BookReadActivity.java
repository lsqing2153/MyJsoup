package com.sqing.www.mytxtread.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.adapter.MyBookReadCatalogAdapter;
import com.sqing.www.mytxtread.dao.BookDao;
import com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupCatalog;
import com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupDetails;
import com.sqing.www.mytxtread.txtviewpager.MyPagerAdapter;
import com.sqing.www.mytxtread.txtviewpager.TxtViewPager;
import com.sqing.www.mytxtread.txtviewpager.get.FontSize;
import com.sqing.www.mytxtread.txtviewpager.production.ProductionTxt;
import com.sqing.www.mytxtread.txtviewpager.readytxt.TxtReady;
import com.sqing.www.mytxtread.userdata.Book;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookReadActivity extends AppCompatActivity {
    TxtViewPager tvp_book;
    LinearLayout ll_pb_read,ll_read_setting_top, ll_read_setting_bottom,ll_read_setting;
    TextView tv_book_read_name;
    FrameLayout fl_book_read;

    List<String> catalog = new ArrayList<>();
    Map catalogUrl = new HashMap();
    List<View> list_one = new ArrayList<>();
    int index = 0;

    int page=0;

    String www;

//    int max_index =0;
    int[] this_index_num=new int[10240];
    public int min_index =0;



    Context ctx = this;


    int list_one_max = 0;

    int number = 0;

//    Content content;




    Thread thread_inspect = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        if(txt.getInt("night", 0)==0){
            this.setTheme(R.style.BookRead);
        }else{
            this.setTheme(R.style.DayNight);
        }
        setContentView(R.layout.activity_book_read);
        info();
    }

    /**
     * 第一次进入时进行初始化
     */
    private void info() {
        tvp_book = (TxtViewPager) findViewById(R.id.tvp_book);
        tvp_book.setCtx(ctx);
        tvp_book.setRead(this);
        fl_book_read= (FrameLayout) findViewById(R.id.fl_book_read);
        ll_pb_read = (LinearLayout) findViewById(R.id.ll_pb_read);
        ll_read_setting_top= (LinearLayout) findViewById(R.id.ll_read_setting_top);
        ll_read_setting_bottom = (LinearLayout) findViewById(R.id.ll_read_setting_bottom);
        ll_read_setting= (LinearLayout) findViewById(R.id.ll_read_setting);
        tvp_book.setLinearLayout(ll_read_setting_top,ll_read_setting_bottom,ll_pb_read,ll_read_setting);
        tv_book_read_name = (TextView) findViewById(R.id.tv_book_read_name);
        Intent intent = getIntent();
        String book = intent.getStringExtra("book");

        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        int bj_color = txt.getInt("bj_color", 1);
        int night = txt.getInt("night", 0);
        if(night==0) {
            bjJudge(bj_color);
        }else{
            ImageButton ib_night = (ImageButton) findViewById(R.id.ib_night);
            ib_night.setImageDrawable(getResources().getDrawable(R.drawable.book_daytime));
        }

         www= getIntent().getStringExtra("www");

        index=intent.getIntExtra("thischapter",0);
        if(index!=0){
            min_index =index;
        }
        page=intent.getIntExtra("page",0);
        String name=book.split("_____")[0];
        tv_book_read_name.setText(name);
        String[] item = book.split("_____")[1].split("-----");
        int index = 1;
        for (String s : item) {
            if(!s.equals("") ) {
                if(!s.equals("=====") ) {
                    String[] catalog_url = s.split("=====");
                    catalog.add(catalog_url[0]);
                    this.catalogUrl.put(index, catalog_url[1]);
                    index++;
                }
            }
        }

        set();

        if(txt.getInt("is_recreate",0)!=0){
            ll_read_setting_top.setVisibility(View.VISIBLE);
            ll_read_setting_bottom.setVisibility(View.VISIBLE);
        }
        //把list填充数据
        ReadThread();

        SharedPreferences.Editor edit = txt.edit();
        edit.putInt("is_recreate",0);
        edit.commit();
    }

    private void set() {
        //用于判断翻页时是不是第一页或最后一页,是的时候就向前翻章,或向后翻章
        tvp_book.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean isScrolled = false;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:// 手势滑动
                        isScrolled = false;
                        break;
                    case 2:// 界面切换
                        isScrolled = true;
                        break;
                    case 0:// 滑动结束

// 当前为最后一张，此时从右向左滑，则切换加载下一章
                        if (tvp_book.getCurrentItem() == tvp_book.getAdapter().getCount() - 1 && !isScrolled) {
                            ll_pb_read.setVisibility(View.VISIBLE);
                            NextThread();
                        }
// 当前为第一张，此时从左向右滑，则加载前一章
                        if (tvp_book.getCurrentItem() == 0 && !isScrolled) {
                            if(min_index !=0) {
                                ll_pb_read.setVisibility(View.VISIBLE);
                                UpThread();
                            }else{
                                Toast.makeText(ctx,"当前在最开始",Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
            }
        });

    }
    boolean next_ing=true;

    /**
     * 向后翻一章
     */
    public void NextThread() {
        if(next_ing) {
            next_ing=false;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Message msg2 = new Message();
                        if(catalogUrl.get(index+1)!=null) {
                            setList(index);
                            Message msg = new Message();
                            msg.arg1 = LIST_OK;
                            myReadHandler.sendMessage(msg);
                        }else{
                            msg2.arg2=1;
                        }
                        Thread.sleep(200);
                        msg2.arg1 = DOWN_LIST;
                        myReadHandler.sendMessage(msg2);
                        next_ing=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        MsgException();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


    boolean go_chapters =false;
    boolean up_ing=true;

    /**
     * 向上翻页
     */
    public void UpThread() {
        if(up_ing) {
            up_ing=false;
            new Thread() {
                @Override
                public void run() {
                    try {
                        go_chapters = true;
                        min_index--;
                        setList(min_index);
                        Message msg = new Message();
                        Thread.sleep(200);
                        msg.arg1 = UP_LIST;
                        myReadHandler.sendMessage(msg);
                        up_ing=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        MsgException();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * 第一次进入时加载或跳转章节使用
     */
    private void ReadThread() {
        //把list填充数据
        new Thread() {
            @Override
            public void run() {
                try {
                    setList(index);
                    Message msg = new Message();
                    msg.arg1 = LIST_OK;
                    myReadHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    MsgException();
                }
            }
        }.start();
    }

    /**
     * 检查是否翻页到第二个页的中间
     * @return
     */
    private Thread InspectIndex() {
        return new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (list_one_max != 0) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (number - tvp_book.getCurrentItem()  == list_one_max * 2 / 3 && index!=catalog.size() || list_one_max==1&& index!=catalog.size() ) {
                            try {
                                setList(index);
                                Message msg = new Message();
                                msg.arg1 = LIST_OK;
                                myReadHandler.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                                MsgException();
                            }
                        }
                    }
                }
            }
        };
    }

    private void MsgException() {
        Message msg = new Message();
        msg.arg1 = EXCEPTION;
        myReadHandler.sendMessage(msg);
    }


    MyReadHandler myReadHandler = new MyReadHandler();
    final int EXCEPTION = 110;
    final int LIST_OK = 0;
    final int CATALOG_LIAT=1;
    final int GO_CATALOG=2;
    final int UP_LIST=3;
    final int DOWN_LIST=4;
    final int PAGE_TURNING=5;
    final int SWITCH_SOURCE=6;
    MyPagerAdapter mpa;

    /**
     * 对线程的各种操作进行对界面的修改
     */
    private  class MyReadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.arg1) {
                case EXCEPTION:
                    Toast.makeText(ctx, "网络连接超时", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case LIST_OK:
                    if (mpa == null && ll_pb_read.getVisibility() != View.GONE) {
                        List<View> list = new ArrayList<View>();
                        for (View v : list_one) {
                            list.add(v);
                        }
                        mpa = new MyPagerAdapter(list,ctx);
                        tvp_book.setAdapter(mpa);
                        tvp_book.setCurrentItem(page,false);
                        page=0;
                        ll_pb_read.setVisibility(View.GONE);
                    }else {
                        mpa.listAdd(list_one);
                    }
//                    if(content==null)
//                        content = new Content();
//                    content.addContent(index  + "", list_one);
                    index++;
//                    max_index =index;
                    if (thread_inspect == null) {
                        thread_inspect = InspectIndex();
                        thread_inspect.start();
                    }

                    //不让快速修改字体大小
                    fontSize_ing=true;

                    break;
                case CATALOG_LIAT:
                    AlertDialog.Builder customizeDialog =new AlertDialog.Builder(ctx);
                    MyBookReadCatalogAdapter myBookReadCatalogAdapter=new MyBookReadCatalogAdapter((List<String[]>) msg.obj,ctx,msg.arg2);

                    View inflate = View.inflate(ctx, R.layout.dialog_title, null);
                    TextView tv_dialog_title = (TextView) inflate .findViewById(R.id.tv_dialog_title);
                    tv_dialog_title.setText(tv_book_read_name.getText());
                    customizeDialog.setCustomTitle(inflate).setSingleChoiceItems(myBookReadCatalogAdapter, msg.arg2-3, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            index=i;
                            min_index =i;
                            number=0;
                            this_index_num=new int[10240];
                            Message msg=new Message();
                            msg.arg1=GO_CATALOG;
                            myReadHandler.sendMessage(msg);
                            dialogInterface.dismiss();
                        }
                    });
                    customizeDialog.show();
                    break;
                case GO_CATALOG:
                    ll_pb_read.setVisibility(View.VISIBLE);
                    mpa=null;

//                    List<View> list;
//                    if((list=content.getContent_map(min_index+""))!=null){
//                        list_one=list;
//                        message.arg1 = LIST_OK;
//                        myReadHandler.sendMessage(message);
//                    }else {
//                        ReadThread();
//                    }

                    ReadThread();
                    break;
                case UP_LIST:
                    mpa.listUp(list_one);
                    mpa.notifyDataSetChanged();
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                Message msg = new Message();
                                msg.arg1 = PAGE_TURNING;
                                myReadHandler.sendMessage(msg);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                case DOWN_LIST:
                    if(msg.arg2==1){
                        Toast.makeText(ctx,"最后一章!!",Toast.LENGTH_LONG).show();
                    }
                    tvp_book.setCurrentItem(tvp_book.getCurrentItem()+1,false);
                    ll_pb_read.setVisibility(View.GONE);
                    break;
                case PAGE_TURNING:

                      //后来因为修改了TxtViewPager的代码,让它每当有数据更新时就把全部内容刷新一遍,所以不使用下面的方法

                      //因为会残留之前的第一个页面的内容,先获取到,去除后还要把它添加上去,不这样做就会出现内容无法引用,需要removeView后再添加
//                    tvp_book.removeAllViews();
//                    tvp_book.setCurrentItem(list_one_max-4,false);

                    tvp_book.setCurrentItem(list_one_max-1,false);

                    ll_pb_read.setVisibility(View.GONE);
                    break;
                case SWITCH_SOURCE:
                    index=0;
                    min_index=0;
                    mpa=null;
                    ll_pb_read.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    /**
     * 进行获取数据,填充list
     * @param index
     * @throws IOException
     */
    private void setList(int index) throws IOException {
        SharedPreferences sharedPreferences = getSharedPreferences("txt", Context.MODE_PRIVATE);
        String contents1="";


        if(www.equals("dingdian_category")) {
            contents1= new com.sqing.www.mytxtread.myjsoup.dingdianxiaoshuo.JsoupContents().GetContents((String) catalogUrl.get(index + 1));
        }else if(www.equals("luoqiu_category")){
            contents1= new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupContents().GetContents((String) catalogUrl.get(index + 1));
        }else if(www.equals("qingkan520_category")){
            contents1= new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupContents().GetContents((String) catalogUrl.get(index + 1));
        }
        String contents = new String(contents1.getBytes("utf-8"), "utf-8");
        List<String> txt;
        if(www.equals("qingkan520_category")) {
            txt = new TxtReady(ctx).ReadTxt(contents,sharedPreferences.getInt("fontsize",20),1);
        }else {
            txt = new TxtReady(ctx).ReadTxt(contents,sharedPreferences.getInt("fontsize",20),2);
        }
        list_one=new ArrayList<>();
        int i = 1;
        for (String s : txt) {
            View view = View.inflate(ctx, R.layout.content_viewpager, null);
            TextView tv_chapter_name = (TextView) view.findViewById(R.id.tv_chapter_name);
            tv_chapter_name.setText(catalog.get(index ));
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_content.setText(s);
            tv_content.setTextSize(sharedPreferences.getInt("fontsize",20));

            //当添加控件时,对内部的东西进行修改
            tv_content.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener(tv_content));



            ProductionTxt productionTxt = new ProductionTxt(ctx);
            View v = view.findViewById(R.id.ll_viewpager_content);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) v.getLayoutParams();
            layoutParams.width= (int) (FontSize.getFontWidth(sharedPreferences.getInt("fontsize",20))*getDisplayMetrics().density*productionTxt.getRow());
            v.setLayoutParams(layoutParams);

            TextView tv_book_read_time = (TextView) view.findViewById(R.id.tv_book_read_time);
//            Calendar mCalendar = Calendar.getInstance();
//            tv_book_read_time.setText(mCalendar.get(Calendar.HOUR) + ":" + mCalendar.get(Calendar.MINUTE));
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
            Date now=new Date();
            tv_book_read_time.setText(sdf.format(now));
            TextView tv_page = (TextView) view.findViewById(R.id.tv_page);
            tv_page.setText(i++ + "/" + txt.size());
            list_one.add(view);
        }

        if (list_one_max==0) {
            list_one_max = list_one.size() - 1;
        }else{
            list_one_max = list_one.size() ;
        }
        number += list_one_max;
        this_index_num[index]=list_one_max;
        if(go_chapters) {
//            max_index++;
            go_chapters=false;
        }

    }

    /**
     * 当添加控件时,对内部的东西进行修改,不让因为标点出现突然换行
     */
    private class OnTvGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        public OnTvGlobalLayoutListener(TextView tx){
            mText=tx;
        }
        TextView mText;
        @Override
        public void onGlobalLayout() {
            mText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            String newText = autoSplitText(mText);
            if (!TextUtils.isEmpty(newText)) {
                mText.setText(newText);
            }
        }
    }
    private String autoSplitText(TextView tv) {
        String rawText = tv.getText().toString(); //原始文本
        Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String [] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }




    /**
     * 响应返回按钮
     *
     * @param view
     */
    public void ib_close(View view) {
        finish();
    }


    /**
     * 响应目录按钮
     * @param v
     */
    public void OpenCatalog(View v){

        List<String[]> list=new ArrayList<String[]>();
        for(int i=0;i<catalog.size();i++) {
            String[] strings=new String[]{i+"",catalog.get(i)};
            list.add(strings);
        }
        Message msg=new Message();
        msg.obj=list;
        msg.arg1=CATALOG_LIAT;
        msg.arg2=getCurrentChapterAndPage().get(0);
        myReadHandler.sendMessage(msg);
    }


    /**
     * 打开设置
     * @param view
     */
    public void openSetting(View view){
        isGone();
        //获取背景按钮控件
        getBtn();

        if(((ColorDrawable)fl_book_read.getBackground()).getColor()==getResources().getColor(R.color.colorBjGreen)){
            btn_book_read_bj_green.setBackground(getResources().getDrawable(R.drawable.book_bj_green_in));
        }else  if(((ColorDrawable)fl_book_read.getBackground()).getColor()==getResources().getColor(R.color.colorBjGray)){
            btn_book_read_bj_gray.setBackground(getResources().getDrawable(R.drawable.book_bj_gray_in));
        }else{
            btn_book_read_bj_brown.setBackground(getResources().getDrawable(R.drawable.book_bj_brown_in));
        }
    }

    private void getBtn() {
        if(btn_book_read_bj_green==null  || btn_book_read_bj_gray==null || btn_book_read_bj_brown==null){
            btn_book_read_bj_green= (Button) findViewById(R.id.btn_book_read_bj_green);
            btn_book_read_bj_gray= (Button) findViewById(R.id.btn_book_read_bj_gray);
            btn_book_read_bj_brown= (Button) findViewById(R.id.btn_book_read_bj_brown);
        }
    }

    private void isGone() {
        if (ll_read_setting.getVisibility()== View.GONE){
            ll_read_setting.setVisibility(View.VISIBLE);
        }else{
            ll_read_setting.setVisibility(View.GONE);
        }
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
                if(((ColorDrawable)fl_book_read.getBackground()).getColor()!=getResources().getColor(R.color.colorBjGreen)){
                    fl_book_read.setBackground(getResources().getDrawable(R.color.colorBjGreen));
                    ll_pb_read.setBackground(getResources().getDrawable(R.color.colorBjGreen));
                    editor1.putInt("bj_color",1);
                    editor1.commit();
                }
                break;
            case R.id.btn_book_read_bj_gray:
                view.setBackground(getResources().getDrawable(R.drawable.book_bj_gray_in));
                btn_book_read_bj_green.setBackground(getResources().getDrawable(R.drawable.book_bj_green));
                btn_book_read_bj_brown.setBackground(getResources().getDrawable(R.drawable.book_bj_brown));
                if(((ColorDrawable)fl_book_read.getBackground()).getColor()!=getResources().getColor(R.color.colorBjGray)){
                    fl_book_read.setBackground(getResources().getDrawable(R.color.colorBjGray));
                    ll_pb_read.setBackground(getResources().getDrawable(R.color.colorBjGray));
                    editor1.putInt("bj_color",2);
                    editor1.commit();
                }
                break;
            case R.id.btn_book_read_bj_brown:
                view.setBackground(getResources().getDrawable(R.drawable.book_bj_brown_in));
                btn_book_read_bj_gray.setBackground(getResources().getDrawable(R.drawable.book_bj_gray));
                btn_book_read_bj_green.setBackground(getResources().getDrawable(R.drawable.book_bj_green));
                if(((ColorDrawable)fl_book_read.getBackground()).getColor()!=getResources().getColor(R.color.colorBjBrown)){
                    fl_book_read.setBackground(getResources().getDrawable(R.color.colorBjBrown));
                    ll_pb_read.setBackground(getResources().getDrawable(R.color.colorBjBrown));
                    editor1.putInt("bj_color",3);
                    editor1.commit();
                }
                break;
        }
    }
    /**
     * 夜间模式
     * @param view
     */
    public void nightSwitch(View view){
        SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
        int night = txt.getInt("night", 0);
        SharedPreferences.Editor edit = txt.edit();
        if(night==0) {
            edit.putInt("night", 1);
            edit.putInt("is_recreate", 1);
            recreate();
        }else{
            edit.putInt("night", 0);
            edit.putInt("is_recreate", 1);
            recreate();
        }
        edit.commit();
    }

    /**
     * 背景颜色判断
     * @param bj_color
     */
    private void bjJudge(int bj_color) {
        if (bj_color == 1) {
            fl_book_read.setBackground(getResources().getDrawable(R.color.colorBjGreen));
            ll_pb_read.setBackground(getResources().getDrawable(R.color.colorBjGreen));
        } else if (bj_color == 2) {
            fl_book_read.setBackground(getResources().getDrawable(R.color.colorBjGray));
            ll_pb_read.setBackground(getResources().getDrawable(R.color.colorBjGray));
        } else {
            fl_book_read.setBackground(getResources().getDrawable(R.color.colorBjBrown));
            ll_pb_read.setBackground(getResources().getDrawable(R.color.colorBjBrown));
        }
    }


    //不让快速修改字体大小
    boolean fontSize_ing=true;
    /**
     * 增加字体大小
     * @param view
     */
    public void fontSizeAdd(View view){
        if (fontSize_ing) {
            fontSize_ing=false;
            SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = txt.edit();
            int anInt = getSharedPreferences("txt", Context.MODE_PRIVATE).getInt("fontsize", 20);
            editor1.putInt("fontsize", ++anInt);
            editor1.commit();
            Message msg = new Message();
            msg.arg1 = GO_CATALOG;
            index--;
            myReadHandler.sendMessage(msg);
        }
    }
    /**
     * 减少字体大小
     * @param view
     */
    public void fontSizeReduce(View view) {
        if (fontSize_ing) {
            fontSize_ing=false;
            SharedPreferences txt = getSharedPreferences("txt", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = txt.edit();
            int anInt = getSharedPreferences("txt", Context.MODE_PRIVATE).getInt("fontsize", 20);
            editor1.putInt("fontsize", --anInt);
            editor1.commit();
            Message msg = new Message();
            msg.arg1 = GO_CATALOG;
            index--;
            myReadHandler.sendMessage(msg);
        }
    }

    /**
     * 获取屏幕对象
     * @return
     */
    private DisplayMetrics getDisplayMetrics() {//为了获取宽高,像素密度
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    /**
     * 销毁读取数据时对屏幕的点击事件
     * @param view
     */
    public void noClick(View view){}

    /**
     * 切换来源
     * @param view
     */
    public void switchSource(View view){
        Intent intent=new Intent();
        intent.setClass(BookReadActivity.this, SourceActivity.class);
        intent.putExtra("www",www);
        intent.putExtra("name",tv_book_read_name.getText());
        startActivityForResult(intent,10010);
    }

    /**
     * 获取从来源界面返回的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10010 && resultCode==10086){
            www=data.getStringExtra("www");
            System.out.println(www);
            final String url = data.getStringExtra("url");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Message msg = new Message();
                            msg.arg1 = SWITCH_SOURCE;
                            myReadHandler.sendMessage(msg);

                            List<String> catalog_1 = null;
                            if(www.equals("dingdian_category")) {
                                String[] ss =  new JsoupDetails().getDetails(url);
                                catalog_1 = new JsoupCatalog().getCatalog(ss[8]);
                            }else if(www.equals("luoqiu_category")){
                                String[] ss = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupDetails().getDetails(url);
                                catalog_1 = new com.sqing.www.mytxtread.myjsoup.luoqiuxiaoshuo.JsoupCatalog().getCatalog(ss[8]);
                            }else if(www.equals("qingkan520_category")){
                                catalog_1 =  new com.sqing.www.mytxtread.myjsoup.qingkan.JsoupCatalog().getCatalog(url.split("info.html")[0]);
                            }
                            catalogUrl= new HashMap();
                            String content = ""+tv_book_read_name.getText()+"_____";
                            int i=1;
                            for (String s : catalog_1) {
                                content += s.replaceAll("-----","   ") + "-----";
                                if(!s.equals("")) {
                                    String[] catalog_url = s.split("=====");
                                    catalog.add(catalog_url[0]);
                                    catalogUrl.put(i, catalog_url[1]);
                                    i++;
                                }
                            }
                            String file_catalog = Environment.getExternalStorageDirectory() + "/yuedu/catalog";
                            File dir = new File(file_catalog, tv_book_read_name.getText() + ".txt");
                            BufferedWriter bw = new BufferedWriter(new FileWriter(dir, false));
                            bw.write(content);
                            bw.flush();

                            ReadThread();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();


        }

    }

    /**
     * 退出时保存数据
     */
    @Override
    protected void onPause() {
        BookDao dao=new BookDao(ctx);

        List<Integer> list = getCurrentChapterAndPage();
        dao.update(new Book(tv_book_read_name.getText()+"","","",list.get(0),list.get(1),"",""));
        super.onPause();
    }


    /**
     * 确定当前所在的章节和页码
     * @return
     */
    @NonNull
    private List<Integer> getCurrentChapterAndPage() {
        List<Integer> list = new ArrayList<>();
        int bookmark_index=0;
        int bookmark_page_number=0;
        for(int j=0;j<=index-min_index;j++) {
            if (this_index_num[index-j] != 0 && this_index_num[min_index] != 0) {
                int num = 0;
                for (int i = min_index; i <= index-j; i++) {
                    num += this_index_num[i];
                    if (tvp_book.getCurrentItem() <= num) {
                        bookmark_index = i;
                        bookmark_page_number = tvp_book.getCurrentItem() - num + this_index_num[i];

                    }
                }
            }
        }
        list.add(bookmark_index);
        list.add(bookmark_page_number);
        return list;
    }
}
