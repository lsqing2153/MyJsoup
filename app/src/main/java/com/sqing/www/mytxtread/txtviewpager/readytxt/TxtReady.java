package com.sqing.www.mytxtread.txtviewpager.readytxt;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import com.sqing.www.mytxtread.txtviewpager.get.FontSize;
import com.sqing.www.mytxtread.txtviewpager.production.ProductionTxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqing on 2016/11/6.
 */

public class TxtReady {
    Context cxt;
    public TxtReady(Context cxt) {
        this.cxt = cxt;
    }


    /**
     * 如果已经保存为TXT文件就在这里读取为字符串
     *
     * @param txtfile
     * @return
     */
    public List<String> ReadTxt(File txtfile) {
        String txt = MosaicString(txtfile);

        return null;
    }

    /**
     * 对TXT文件处理
     *
     * @param txtfile
     * @return
     */
    private String MosaicString(File txtfile) {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(txtfile), new FileCharsetDetector().guessFileEncoding(txtfile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String txtStirng = "";
        String s;
        try {
            while ((s = bufferedReader.readLine()) != null) {
                txtStirng = s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return txtStirng;
    }
    int fontsize;
    /**
     * 在线阅读,对爬虫爬出了的章节内容处理
     *
     * @param txt
     * @return
     */
    public List<String> ReadTxt(String txt,int fontsize,int type) {
        List<String> page_txt=new ArrayList<>();
//        txt = ToDBC(txt);
        String content = Manage(txt,type);//把读取的空格去掉和对多余内容标签的去除

        //重写前的屏幕适配
        //获取行数
        ProductionTxt pt = new ProductionTxt(cxt);
//        int number = pt.getStringNumber();
        int row = pt.getRow();
        int col = pt.getCol();
        String content_txt = "";
        List<String> row_txt = getRowTxt(content, row,fontsize);//把所有的字排列成行
        int index=0;
//        for (String s : row_txt) {//把行排列成页
//            content_txt+=s;
//            index++;
//            System.out.println(s);
//            if(index==col){
//                page_txt.add(content_txt);
//                content_txt="";
//                index=0;
//            }
//        }
        for(int i=0;i<=row_txt.size()-1;i++){//把行排列成页
            content_txt+=row_txt.get(i);
            if(i==row_txt.size()-1){
                page_txt.add(content_txt);
            }
            index++;
            if(index==col){
                page_txt.add(content_txt);
                content_txt="";
                index=0;
            }
        }

//        //重写的屏幕适配
//        SharedPreferences sp=cxt.getSharedPreferences("txt",Context.MODE_PRIVATE);
//        fontsize=sp.getInt("fontsize",20);
//
//        ProductionTxt pt = new ProductionTxt(cxt);
//        int col = pt.getCol();
//        List<String> row_list = array_row(content);
//        int col_num=1;
//        String page="";
//        for(String row_txt:row_list){
//            if(col_num==col){
//                page_txt.add(page);
//                page="";
//                col_num=0;
//            }
//            page+=row_txt;
//            col_num++;
//        }

        return page_txt;
    }

    private DisplayMetrics getDisplayMetrics() {//为了获取宽高,像素密度
        WindowManager wm = (WindowManager) cxt.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    private List<String> array_row(String content) {
        char[] chars = content.toCharArray();
        int width_num=0;
        String row_txt="";
        List<String> row_list=new ArrayList<>();
        for(int i=0;i<=chars.length-1;i++) {
            if(chars[i]!='\n'){
                width_num += FontSize.getFontWidth(fontsize, chars[i] + "");
                if(width_num>getDisplayMetrics().widthPixels/getDisplayMetrics().density){
                    if(chars[i]!='。'|| chars[i]!='，'||chars[i]!='？'||chars[i]!='。'||chars[i]!='！'||chars[i]!='!'||chars[i]!='?'){
                        width_num= FontSize.getFontWidth(fontsize, chars[i] + "");
                        row_list.add(row_txt);
                        row_txt=chars[i]+"";
                    }else{
                        width_num= FontSize.getFontWidth(fontsize, chars[i] + "")+ FontSize.getFontWidth(fontsize, chars[i-1] + "");
                        row_txt.substring(0,row_txt.length()-2);
                        row_list.add(row_txt);
                        row_txt=chars[i-1]+""+chars[i];
                    }
                }else{
                    row_txt+=chars[i];
                }
            }else{
                row_txt+=chars[i];
                row_list.add(row_txt);
                width_num=0;
                row_txt="";
            }
        }
        return row_list;
    }



    public String ToDBC(String input) {//修改中文标点
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    private String Manage(String txt,int type) {//去除空格o
        String s1 = txt.replaceAll("\r\n", "").replaceAll("<br>","").replaceAll("\t","").replaceAll("\n","").replaceAll(" ","");
        String[] str=null;
        if(type==2) {
            str = s1.split("&nbsp;&nbsp;&nbsp;&nbsp;");
        }else if(type==1){
            str = s1.split("　　");
        }
        String content = "";
        int i=1;
        for (String s : str) {
            if(i==1){
                content=content+s;
                i++;
            }else {
                content = content +"       "+ s+"\r" ;
            }
        }

        return content.replaceAll("&nbsp;", "");
    }

    /**
     * 把字符串按一行row个字排列
     *
     * @param txt
     * @param row
     * @return
     */
    private static List<String> getRowTxt(String txt, int row,int fontsize) {
//        List<String> rowTxt = new ArrayList<>();
//        char[] chars = txt.toCharArray();
//        String txt_col = "";
//        int i = 1;
//        int o=0;
//        for (char c : chars) {
//            if(c==' '|| c=='“' || c=='”' || c=='‘'|| c=='’'){
//                o++;
//            }
//            if(o==2){
//                i=i-o/2;
//                o = 0;
//            }
//            txt_col += c ;
//            if (i == row) {
//                rowTxt.add(txt_col);
//                txt_col = "";
//                i = 1;
//            } else {
//                i += 1;
//            }
//            if (c == '\n') {
//                rowTxt.add(txt_col);
//                txt_col = "";
//                i = 1;
//            }
//
//        }
//        return rowTxt;


        //-----修改
        List<String> rowTxt = new ArrayList<>();
        char[] chars = txt.toCharArray();
        String txt_col = "";
        double i = 0;
        int o = 0;
        for (char c : chars) {
            if (c == ' ' || c == '.') {
                txt_col += c;
                i = i + 0.25;
            }else if (c == '“' || c == '”' || c == '‘' || c == '’'|| c == '['|| c == ']'|| c=='/'||c=='f' || c=='t') {
                txt_col += c;
                i = i + 0.35;
            }else if(c=='-'||c=='~'){
                i+=0.75;
                txt_col +=c;
            }else if(c>=48 &&c<=57 ||c=='I' ){
                i+=0.55;
                txt_col +=c;
            }else if(c=='i' || c=='l'){
                i+=0.3;
                txt_col +=c;
            }else if(  c=='W'|| c=='M'){
                i+=0.9;
                txt_col +=c;
            }else if(  c>=65&&c<=90 || c=='m'){
                i+=0.75;
                txt_col +=c;
            }else if( c>=97 && c<=122){
                i+=0.5;
                txt_col +=c;
            } else if (c == '\r') {
//                    System.out.println("-------"+txt_col+"-------");
                rowTxt.add(txt_col+"\n");
                txt_col = "";
                i = 0;
            } else{
                txt_col += c;
                i+=1;
            }
            if(o!=chars.length-1) {
                if (i + FontSize.getFontWidth(fontsize, chars[o + 1] + "")/20 >row) {
//                    System.out.println(txt_col);
                    rowTxt.add(txt_col);
                    txt_col = "";
                    if (i - row > 0) {
                        i = i - row;
                    } else {
                        i = 0;
                    }
                }
            }else{
                rowTxt.add(txt_col);
            }
            o++;
        }
        return rowTxt;



    }
}
