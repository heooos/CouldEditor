package com.zhanghao.youdaonote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhanghao.youdaonote.ActivityCollector;
import com.zhanghao.youdaonote.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePasswordActivity extends BaseActivity {

    private EditText originalPassword,changedPassword;
    private Button btn_changedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);

        initView();
        btn_changedPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.updateCurrentUserPassword(ChangePasswordActivity.this, originalPassword.getText().toString().trim(), changedPassword.getText().toString().trim(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ChangePasswordActivity.this,"修改成功，请重新登录！",Toast.LENGTH_SHORT).show();
                        BmobUser.logOut(ChangePasswordActivity.this);
                        ActivityCollector.finishAll();
                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        });

    }

    private void initView() {
        originalPassword = (EditText) findViewById(R.id.note_originalPassword);
        changedPassword = (EditText) findViewById(R.id.note_changedPassword);
        btn_changedPassword = (Button) findViewById(R.id.btn_changePassword);
    }
}
