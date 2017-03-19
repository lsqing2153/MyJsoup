package com.sqing.www.mytxtread.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sqing.www.mytxtread.R;
import com.sqing.www.mytxtread.http.DownImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sqing on 2017/2/28.
 */

public class SearchListAdapter extends BaseAdapter {
    List<String[]> list;
    Context ctx;
    public SearchListAdapter(List<String[]> list, Context ctx){
        this.list=list;
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    Map<String ,Bitmap> bitmapMap =new HashMap<>();
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(ctx, R.layout.list_search_content, null);
        final ImageView book_img = (ImageView) v.findViewById(R.id.iv_search_list_img);
        TextView book_name = (TextView) v.findViewById(R.id.tv_search_name);
        TextView book_author = (TextView) v.findViewById(R.id.tv_search_author);
        TextView book_time = (TextView) v.findViewById(R.id.tv_search_time);
        TextView book_url = (TextView) v.findViewById(R.id.tv_search_url);

        String[] strings = list.get(position);

        book_name.setText(strings[0] + "");
        book_author.setText(strings[1] + "");
        book_time.setText(strings[2] + "");
        book_url.setText(strings[3] + "");
        if(position>=bitmapMap.size()){
            setImgBitmap(book_img, strings[4],position);
        }else if(bitmapMap.get(position+"")!=null){
            book_img.setImageBitmap(bitmapMap.get(position+""));
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
