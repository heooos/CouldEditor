package com.zhanghao.youdaonote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.constants.Conf;
import com.zhanghao.youdaonote.database.DeleteNoteFromDB;

public class NoteShowActivity extends BaseActivity implements View.OnClickListener{

    private TextView content_tv;
    private TextView title_tv;
    private ImageView back;
    private ImageButton edit,delete;
    private String title,content,date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_show_activity);

        init();

        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        back.setOnClickListener(this);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        date = getIntent().getStringExtra("date");

        title_tv.setText(title);
        content_tv.setText(Html.fromHtml(content));
    }

    private void init() {
        content_tv = (TextView) findViewById(R.id.content_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        back = (ImageView) findViewById(R.id.back);
        edit = (ImageButton) findViewById(R.id.note_show_edit);
        delete = (ImageButton) findViewById(R.id.note_show_delete);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.note_show_edit:
                Intent i = new Intent(this,NoteEditActivity.class);
                i.putExtra("title",title_tv.getText().toString());
                i.putExtra("content", content_tv.getText().toString());
                i.putExtra("date",date);
                i.putExtra("className","NoteShowActivity");
                startActivityForResult(i, Conf.ACTIVITY_CODE);
                break;
            case  R.id.note_show_delete:
                DeleteNoteFromDB deleter = new DeleteNoteFromDB(this);
                deleter.delete(date);
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Conf.ACTIVITY_CODE:
                if (resultCode == RESULT_OK){
                    title_tv.setText(data.getExtras().getString("title"));
                    content_tv.setText(Html.fromHtml(data.getExtras().getString("content")));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
