package com.sqing.www.mytxtread.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.http.DownImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sqing on 2016/12/7.
 */

public class MyBooksListAdapter extends BaseAdapter {
    public List<String[]> list;
    public Context ctx;

    public MyBooksListAdapter(List<String[]> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    public void setList(List<String[]> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    Map<String ,Bitmap> bitmapMap =new HashMap<>();
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        View v;
//        final ViewHolder viewHolder;
//        if(view==null) {
//            v = View.inflate(ctx, R.layout.chapter_list, null);
//            viewHolder=new ViewHolder();
//            viewHolder.book_img= (ImageView) v.findViewById(R.id.iv_book_list_img);
//            viewHolder.book_name = (TextView) v.findViewById(R.id.tv_book_name);
//            viewHolder. book_author = (TextView) v.findViewById(R.id.tv_book_author);
//            viewHolder.book_latestChapter = (TextView) v.findViewById(R.id.tv_book_LatestChapter);
//            viewHolder.book_url= (TextView) v.findViewById(R.id.tv_book_url);
//
//
//            String[] strings = list.get(i);
//            viewHolder.book_name.setText(strings[0] + "");
//            viewHolder.book_author.setText(strings[1] +" | "+strings[2]+ "字 | 更新时间:"+strings[3]);
//            viewHolder.book_latestChapter.setText("更新到:" + strings[4] + "");
//            viewHolder.book_url.setText(strings[5]);
//            if(!strings[6].equals("")) {
//
//                //接口回调的方法，完成图片的读取;
//                DownImage downImage = new DownImage(strings[6]);
//                downImage.loadImage(new DownImage.ImageCallBack() {
//
//                    @Override
//                    public void getBitmap(Bitmap bitmap) {
//                        if(bitmap!=null) {
//                            viewHolder.book_img.setImageBitmap(bitmap);
//                            viewHolder.bitmap = bitmap;
//                        }
//                    }
//                });
//            }
//
//
//            v.setTag(viewHolder);
//        }else {
//            v=view;
//            viewHolder= (ViewHolder) v.getTag();
//
//
//            String[] strings = list.get(i);
//            viewHolder.book_name.setText(strings[0] + "");
//            viewHolder.book_author.setText(strings[1] +" | "+strings[2]+ "字 | 更新时间:"+strings[3]);
//            viewHolder.book_latestChapter.setText("更新到:" + strings[4] + "");
//            viewHolder.book_url.setText(strings[5]);
//            if(viewHolder.bitmap!=null) {
//                viewHolder.book_img.setImageBitmap(viewHolder.bitmap);
//            }else{
//                viewHolder.book_img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.book_no_img));
//            }
//        }
        View v = View.inflate(ctx, R.layout.chapter_list, null);
        final ImageView book_img = (ImageView) v.findViewById(R.id.iv_book_list_img);
        TextView book_name = (TextView) v.findViewById(R.id.tv_book_name);
        TextView book_author = (TextView) v.findViewById(R.id.tv_book_author);
        TextView book_latestChapter = (TextView) v.findViewById(R.id.tv_book_LatestChapter);
        TextView book_url = (TextView) v.findViewById(R.id.tv_book_url);

        String[] strings = list.get(i);
        book_name.setText(strings[0] + "");
        book_author.setText(strings[1] + " | " + strings[2] + "字 | 更新时间:" + strings[3]);
        book_latestChapter.setText("更新到:" + strings[4] + "");
        book_url.setText(strings[5]);

        if(i>=bitmapMap.size()){
            setImgBitmap(book_img, strings[6],i);
        }else if(bitmapMap.get(i+"")!=null){
            book_img.setImageBitmap(bitmapMap.get(i+""));
        }

        return v;
    }

    private void setImgBitmap(final ImageView book_img, String string, final int i) {
        if (!string.equals("")) {
            //接口回调的方法，完成图片的读取;
            DownImage downImage = new DownImage(string);
            downImage.loadImage(new DownImage.ImageCallBack() {
                @Override
                public void getBitmap(Bitmap bitmap) {
                    if (bitmap != null) {
                        book_img.setImageBitmap(bitmap);
                    }
                    bitmapMap.put(i+"",bitmap);
                }
            });
        }else{
            bitmapMap.put(i+"",null);
        }
    }


}
