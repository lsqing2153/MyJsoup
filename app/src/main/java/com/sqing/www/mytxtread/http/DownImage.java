package com.sqing.www.mytxtread.http;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Created by Sqing on 2017/2/19.
 */

public class DownImage {
    public String image_path;

    public DownImage(String image_path) {
        this.image_path = image_path;
    }

    public void loadImage(final ImageCallBack callBack) {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bitmap bitmap = (Bitmap) msg.obj;
                callBack.getBitmap(bitmap);
            }

        };

        new Thread(new Runnable() {

            @Override
            public void run() {
                Bitmap bitmap=null;
                try {
                    bitmap = new HttpGetBitmap().returnBitMap(image_path);
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    public interface ImageCallBack {
        public void getBitmap(Bitmap bitmap);
    }
}
