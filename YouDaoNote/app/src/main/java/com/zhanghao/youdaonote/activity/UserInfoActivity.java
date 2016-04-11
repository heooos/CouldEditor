package com.zhanghao.youdaonote.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhanghao.youdaonote.ActivityCollector;
import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.constants.Conf;
import com.zhanghao.youdaonote.database.DeleteNoteFromDB;
import com.zhanghao.youdaonote.entity.NoteUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView userHeadImage,backImage;
    private TextView userId;
    private RelativeLayout userNicknameSetting,userSexSetting,userAreaSetting,userResumeSetting;
    private Button userChangePassword,userExit;
    private NoteUser user;

    private TextView tv_nickname,tv_sex,tv_area,tv_resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);

        user = BmobUser.getCurrentUser(this,NoteUser.class);

        initView();
        initContent();
        initEvent();
    }

    private void initView() {
        backImage = (ImageView) findViewById(R.id.back);
        userHeadImage = (ImageView) findViewById(R.id.head_image);
        userId = (TextView) findViewById(R.id.user_id);

        userNicknameSetting = (RelativeLayout) findViewById(R.id.user_nickname_setting);
        userSexSetting = (RelativeLayout) findViewById(R.id.user_sex_setting);
        userAreaSetting = (RelativeLayout) findViewById(R.id.user_area_setting);
        userResumeSetting = (RelativeLayout) findViewById(R.id.user_resume_setting);
        userChangePassword = (Button) findViewById(R.id.user_changePassword);
        userExit = (Button) findViewById(R.id.user_exit);

        tv_nickname = (TextView) userNicknameSetting.findViewById(R.id.tv_nickname_setting);
        tv_sex = (TextView) userSexSetting.findViewById(R.id.tv_sex_setting);
        tv_area = (TextView) userAreaSetting.findViewById(R.id.tv_area_setting);
        tv_resume = (TextView) userResumeSetting.findViewById(R.id.tv_resume_setting);
    }

    private void initContent() {
            userId.setText( user.getUsername());
            if (user.getUserNickname() != null){
                tv_nickname.setText(user.getUserNickname());
            }
            if (user.getUserSex() != null){
                tv_sex.setText(user.getUserSex());
            }
            if (user.getUserArea() != null){
                tv_area.setText(user.getUserArea());
            }
            if (user.getUserResume() != null){
                tv_resume.setText(user.getUserResume());
            }
    }

    private void initEvent() {

        backImage.setOnClickListener(this);
        userHeadImage.setOnClickListener(this);
        userNicknameSetting.setOnClickListener(this);
        userSexSetting.setOnClickListener(this);
        userAreaSetting.setOnClickListener(this);
        userResumeSetting.setOnClickListener(this);
        userChangePassword.setOnClickListener(this);
        userExit.setOnClickListener(this);

    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()){

            case R.id.back:
                finish();
                break;
            case R.id.head_image:
                //todo 在此可以后期拓展更换头像
                break;
            case R.id.user_nickname_setting:
                AlertDialogHelper(v,"写一个比较屌的昵称！",Conf.NICKNAME_SETTING);
                break;
            case R.id.user_sex_setting:
                AlertDialogHelper(v,"说！你丫男的女的？",Conf.SEX_SETTING);
                break;
            case R.id.user_area_setting:
                AlertDialogHelper(v,"老乡~~哪旮旯的？",Conf.AREA_SETTING);
                break;
            case R.id.user_resume_setting:
                AlertDialogHelper(v,"出来混的，没有个人简介怎么行",Conf.RESUME_SETTING);
                break;
            case R.id.user_changePassword:
                startActivity(new Intent(this,ChangePasswordActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.user_exit:
                BmobUser.logOut(this);
                DeleteNoteFromDB deleteNoteFromDB = new DeleteNoteFromDB(this);
                deleteNoteFromDB.deleteAll(user.getUsername());
                ActivityCollector.finishAll();
                Intent loginIntent = new Intent(this,LoginActivity.class);
                startActivity(loginIntent);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
        }
    }

    /**
     * 自定义弹出对话框
     * @param v
     */
    public void AlertDialogHelper(final View v,String title, final int tag) {

        final Context context = UserInfoActivity.this;
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = inflater.inflate(R.layout.alert_layout,null);
        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et = (EditText) view.findViewById(R.id.et_input);
                String info;
                switch (tag){
                    case Conf.NICKNAME_SETTING:
                        TextView tv_nickname = (TextView)v.findViewById(R.id.tv_nickname_setting);
                        if (!(info = et.getText().toString()).equals(""))
                            tv_nickname.setText(info);
                        //同步信息到网络端
                        UpdateUserInfo(context, tag,info);
                        break;
                    case Conf.SEX_SETTING:
                        TextView tv_sex = (TextView)v.findViewById(R.id.tv_sex_setting);
                        if (!(info = et.getText().toString()).equals(""))
                            tv_sex.setText(info);
                        //同步信息到网络端
                        UpdateUserInfo(context, tag,info);
                        break;
                    case Conf.AREA_SETTING:
                        TextView tv_area = (TextView)v.findViewById(R.id.tv_area_setting);
                        if (!(info = et.getText().toString()).equals(""))
                            tv_area.setText(info);
                        //同步信息到网络端
                        UpdateUserInfo(context, tag,info);
                        break;
                    case Conf.RESUME_SETTING:
                        TextView tv_resume = (TextView)v.findViewById(R.id.tv_resume_setting);
                        if (!(info = et.getText().toString()).equals(""))
                            tv_resume.setText(info);
                        //同步信息到网络端
                        UpdateUserInfo(context, tag,info);
                        break;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UserInfoActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 更新个人信息到数据库
     * @param context
     * @param tag
     * @param info
     */

    private void UpdateUserInfo(final Context context,int tag,String info){
        NoteUser newUser = new NoteUser();

        switch (tag){
            case Conf.NICKNAME_SETTING:
                newUser.setUserNickname(info);
                break;
            case Conf.SEX_SETTING:
                newUser.setUserSex(info);
                break;
            case Conf.AREA_SETTING:
                newUser.setUserArea(info);
                break;
            case Conf.RESUME_SETTING:
                newUser.setUserResume(info);
                break;
        }

        BmobUser user = BmobUser.getCurrentUser(UserInfoActivity.this);
        newUser.update(context, user.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
