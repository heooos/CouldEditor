package com.zhanghao.youdaonote;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.zhanghao.youdaonote.constants.Conf;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;

/**
 * Created by ZH on 2016/3/3.
 */
public class TApplication extends Application {

    public static TApplication instance;
    public static BmobUser user;
    public static String filePath;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        
        Bmob.initialize(this, Conf.APP_ID);
        filePath = Environment.getExternalStorageDirectory().toString() + "/youdaoNote";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        //设置BmobConfig
        BmobConfig config =new BmobConfig.Builder()
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setBlockSize(500*1024)
                .build();
        Bmob.getInstance().initConfig(config);

    }
    /**获取是否存在用户*/
    public boolean hasCurrentUser(){
        BmobUser mUser = new BmobUser().getCurrentUser(this);
        return mUser!=null?true:false;
    }
    public BmobUser getUser(){

        user = new BmobUser().getCurrentUser(this);
        return user;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}
