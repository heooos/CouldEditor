package com.zhanghao.youdaonote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhanghao.youdaonote.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_accountNumber,et_password;
    private Button btn_login,btn_register;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        init();
        initEvent();
    }

    private void init(){
        et_accountNumber = (EditText) findViewById(R.id.note_login_accountNumber);
        et_password = (EditText) findViewById(R.id.note_login_password);
        btn_login = (Button) findViewById(R.id.note_login);
        btn_register = (Button) findViewById(R.id.note_toRegister);
        back = (ImageView) findViewById(R.id.back);
    }

    private void initEvent(){
        back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.note_login:
                login();
                break;
            case R.id.note_toRegister:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }

    /**
     * 登录
     */
    private void login(){
        BmobUser user = new BmobUser();
        user.setUsername(et_accountNumber.getText().toString().trim());
        user.setPassword(et_password.getText().toString());
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this,"登录失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
