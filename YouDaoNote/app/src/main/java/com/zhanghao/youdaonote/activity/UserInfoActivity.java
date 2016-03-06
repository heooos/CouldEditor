package com.zhanghao.youdaonote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhanghao.youdaonote.ActivityCollector;
import com.zhanghao.youdaonote.R;

import cn.bmob.v3.BmobUser;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView userHeadImage,backImage;
    private TextView userId;
    private RelativeLayout userIdSetting,userSexSetting,userAreaSetting,userResumeSetting;
    private Button userChangePassword,userExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);

        initView();
        initEvent();

    }

    private void initEvent() {
        backImage.setOnClickListener(this);
        userHeadImage.setOnClickListener(this);
        userIdSetting.setOnClickListener(this);
        userSexSetting.setOnClickListener(this);
        userAreaSetting.setOnClickListener(this);
        userResumeSetting.setOnClickListener(this);
        userChangePassword.setOnClickListener(this);
        userExit.setOnClickListener(this);

        userId.setText( BmobUser.getCurrentUser(this).getUsername());

    }

    private void initView() {
        backImage = (ImageView) findViewById(R.id.back);
        userHeadImage = (ImageView) findViewById(R.id.head_image);
        userId = (TextView) findViewById(R.id.user_id);
        userIdSetting = (RelativeLayout) findViewById(R.id.user_id_setting);
        userSexSetting = (RelativeLayout) findViewById(R.id.user_sex_setting);
        userAreaSetting = (RelativeLayout) findViewById(R.id.user_area_setting);
        userResumeSetting = (RelativeLayout) findViewById(R.id.user_resume_setting);
        userChangePassword = (Button) findViewById(R.id.user_changePassword);
        userExit = (Button) findViewById(R.id.user_exit);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back:
                finish();
                break;
            case R.id.head_image:

                break;
            case R.id.user_id_setting:


                break;
            case R.id.user_sex_setting:

                break;
            case R.id.user_area_setting:

                break;
            case R.id.user_resume_setting:

                break;
            case R.id.user_changePassword:

                break;
            case R.id.user_exit:
                BmobUser.logOut(this);
                ActivityCollector.finishAll();
                Intent loginIntent = new Intent(this,LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }
}
