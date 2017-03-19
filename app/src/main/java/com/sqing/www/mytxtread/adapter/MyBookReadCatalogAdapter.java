package com.sqing.www.mytxtread.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sqing.www.mytxtread.R;

import java.util.List;

/**
 * Created by sqing on 2016/12/17.
 */

public class MyBookReadCatalogAdapter extends BaseAdapter {
    private List<String[]> list;
    private Context ctx;
    private int index;
    public MyBookReadCatalogAdapter(List<String[]> list, Context ctx,int index) {
        this.list = list;
        this.ctx = ctx;
        this.index=index;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        ViewHolder viewHolder;
        if(view==null) {
            v = View.inflate(ctx, R.layout.catalog_list, null);
            viewHolder= new ViewHolder();
            viewHolder.tv_catalog_id = (TextView) v.findViewById(R.id.tv_catalog_id);
            viewHolder. tv_catalog_name = (TextView) v.findViewById(R.id.tv_catalog_name);
            viewHolder. currentTextColor= viewHolder. tv_catalog_name.getCurrentTextColor();
            v.setTag(viewHolder);
        }else {
            v=view;
            viewHolder= (ViewHolder) v.getTag();
        }
        String[] strings = list.get(i);
        viewHolder.tv_catalog_id.setText(strings[0]);
        viewHolder.tv_catalog_name.setText(strings[1]);
        viewHolder.tv_catalog_id.setTextColor(viewHolder.currentTextColor);
        viewHolder.tv_catalog_name.setTextColor(viewHolder.currentTextColor);

        if(i==index){
            viewHolder.tv_catalog_id.setTextColor(Color.RED);
            viewHolder.tv_catalog_name.setTextColor(Color.RED);
        }
        return v;
    }
    class ViewHolder{
        TextView tv_catalog_id;
        TextView tv_catalog_name;
        int currentTextColor;
    }
}
