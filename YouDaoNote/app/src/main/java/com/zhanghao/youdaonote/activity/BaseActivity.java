package com.zhanghao.youdaonote.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zhanghao.youdaonote.ActivityCollector;

/**
 * Created by ZH on 2016/3/5.
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
