package com.zhanghao.youdaonote.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.TApplication;
import com.zhanghao.youdaonote.constants.Conf;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        if (TApplication.instance.hasCurrentUser()){

            handler.sendEmptyMessageDelayed(Conf.START_TO_MAIN,2000);
        }else{
            handler.sendEmptyMessageDelayed(Conf.START_TO_LOGIN,2000);
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Conf.START_TO_LOGIN:
                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    finish();
                    break;
                case Conf.START_TO_MAIN:
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    finish();
                    break;
            }
        }
    };


}
