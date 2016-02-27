package com.zhanghao.youdaonote.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.zhanghao.youdaonote.R;

/**
 * Created by ZH on 2016/2/24.
 */
public class NoteEditActivity extends Activity implements View.OnClickListener {

    private EditText titleEditText,contentEditText;
    private ScrollView view;
    private ImageView back;
    private String title,content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.note_edit_activity);

        init();
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        if (title != null){
            Log.d("NoteEditActivity",title);
            titleEditText.setText(title);
        }
        if (content != null){
            Log.d("0.0",content);
            contentEditText.setText(content);
            contentEditText.setSelection(content.length());
            contentEditText.requestFocus();
        }



    }

    private void init() {

        titleEditText = (EditText) findViewById(R.id.note_edit_title);
        contentEditText = (EditText) findViewById(R.id.note_edit_content);
        view = (ScrollView) findViewById(R.id.myScrollView);
        back = (ImageView) findViewById(R.id.back);
        contentEditText.requestFocus();

        view.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myScrollView:
                Log.d("hello", "click");
                contentEditText.requestFocus();
                break;
            case R.id.back:
                finish();
                break;

        }
    }
}
