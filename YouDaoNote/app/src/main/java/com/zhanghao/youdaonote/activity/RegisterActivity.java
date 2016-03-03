package com.zhanghao.youdaonote.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.entity.NoteUser;

import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private ImageView back;
    private EditText et_accountNumber,et_password;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        init();
        initEvent();
    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);
        et_accountNumber = (EditText) findViewById(R.id.note_register_accountNumber);
        et_password = (EditText) findViewById(R.id.note_register_password);
        btn_register = (Button) findViewById(R.id.note_register);
    }

    private void initEvent(){

        back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.note_register:
                singUp();
                break;
        }
    }

    /**
     * 注册
     */
    private void singUp(){
        NoteUser user = new NoteUser();
        user.setUsername(et_accountNumber.getText().toString().trim());
        user.setPassword(et_password.getText().toString());
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this,"注册成功，请返回登录",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegisterActivity.this,"注册失败"+ s,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
