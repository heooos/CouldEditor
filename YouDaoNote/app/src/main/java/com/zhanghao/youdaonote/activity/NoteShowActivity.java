package com.zhanghao.youdaonote.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhanghao.youdaonote.R;

public class NoteShowActivity extends Activity implements View.OnClickListener{

    private TextView content_tv;
    private TextView title_tv;
    private ImageView back;
    private ImageButton edit,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_show_activity);

        initView();

        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        back.setOnClickListener(this);
        String title = getIntent().getStringExtra("title");

        title_tv.setText(title);
        final String html="<html>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<p>每个表格由 table 标签开始。</p>\n" +
                "<p>每个表格行由 tr 标签开始。</p>\n" +
                "<p>每个表格数据由 td 标签开始。</p>\n" +
                "\n" +
                "<h4>一列：</h4>\n" +
                "<table border=\"1\">\n" +
                "<tr>\n" +
                "  <td>100</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "\n" +
                "<h4>一行三列：</h4>\n" +
                "<table border=\"1\">\n" +
                "<tr>\n" +
                "  <td>100</td>\n" +
                "  <td>200</td>\n" +
                "  <td>300</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "\n" +
                "<h4>两行三列：</h4>\n" +
                "<table border=\"1\">\n" +
                "<tr>\n" +
                "  <td>100</td>\n" +
                "  <td>200</td>\n" +
                "  <td>300</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "  <td>400</td>\n" +
                "  <td>500</td>\n" +
                "  <td>600</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        content_tv.setText(Html.fromHtml(html));
    }

    private void initView() {
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
                i.putExtra("content",content_tv.getText().toString());
                Log.d("NoteShowActivity", title_tv.getText().toString());
                startActivity(i);
                break;
            case  R.id.note_show_delete:
                // TODO: 2016/2/28 删除按钮功能添加
                break;
        }
    }
}
