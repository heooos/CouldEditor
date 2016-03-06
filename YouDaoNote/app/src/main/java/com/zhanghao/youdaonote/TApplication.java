package com.zhanghao.youdaonote;

import android.app.Application;

import com.zhanghao.youdaonote.constants.Conf;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by ZH on 2016/3/3.
 */
public class TApplication extends Application {

    public static TApplication instance;
    public static BmobUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        
        Bmob.initialize(this, Conf.APP_ID);
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
}
