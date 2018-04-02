package com.sqing.www.mytxtread.jurisdiction;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.sqing.www.mytxtread.R;

/**
 * Created by Sqing on 2017/4/7.
 */

public class Jurisdiction {

    Context ctx;
    Activity activity;
    public Jurisdiction(Context ctx,Activity activity){
        this.ctx=ctx;
        this.activity=activity;
    }


    private static final int REQUEST_WRITE = 1;
    /**
     * 权限申请
     */
    public void jurisdiction_sd() {

        if(Build.VERSION.SDK_INT>=23){
            //判断是否有这个权限
            if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //第一请求权限被取消显示的判断，一般可以不写
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Log.i("readTosdCard","我们需要这个权限给你提供存储服务");
                    showAlert();
                }else {
                    //2、申请权限: 参数二：权限的数组；参数三：请求码
                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE);
                }
            }else {
//                writeToSdCard();
            }
        } else{
//            writeToSdCard();
        }
    }

    private void showAlert(){
        Dialog alertDialog = new AlertDialog.Builder(ctx).
                setTitle("权限说明").
                setMessage("我们需要这个权限给你提供存储服务").
                setIcon(R.mipmap.ic_app).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //2、申请权限: 参数二：权限的数组；参数三：请求码
                        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE);
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                create();
        alertDialog.show();
    }

}
