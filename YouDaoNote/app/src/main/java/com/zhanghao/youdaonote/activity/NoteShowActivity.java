package com.zhanghao.youdaonote.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.TApplication;
import com.zhanghao.youdaonote.database.DeleteNoteFromDB;
import com.zhanghao.youdaonote.entity.ClickableImageSpan;
import com.zhanghao.youdaonote.entity.ClickableMovementMethod;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        initView();
        initEvent();

        showNote();
    }

    /**
     * 笔记内容展示
     */
    private void showNote() {
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        date = getIntent().getStringExtra("date");

        SpannableString ss = getSpannableString(content,this);

        title_tv.setText(title);
        content_tv.setText(ss);
        content_tv.setMovementMethod(ClickableMovementMethod.getInstance());
    }


    public SpannableString getSpannableString(String str,Context context) {
        SpannableString ss = new SpannableString(str);
        Pattern p = Pattern.compile("((?:\\/[\\w\\.\\-]+)+)");
        final Matcher m = p.matcher(str);
        if(m.find()){
            File f = new File(m.group(1));
            String fileName = f.getName();
            String prefix = fileName.substring(fileName.lastIndexOf(".")+1);
            Log.d("文件类型", prefix);
            if (prefix.equals("jpg")){
                Bitmap bm = BitmapFactory.decodeFile(m.group(1));
                Bitmap rbm = NoteEditActivity.resizeImage(bm, 500, 700);
                ClickableImageSpan span = new ClickableImageSpan(context, rbm) {
                    @Override
                    public void onClick(View view) {

                    }
                };
                ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (prefix.equals("mp4")){
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(m.group(1));
                Bitmap bitmap1 = media.getFrameAtTime();
                Bitmap bitmap2 = NoteEditActivity.resizeImage(bitmap1, 500, 700);
                Bitmap bitmap3 = addPlayIcon(bitmap2);
                ClickableImageSpan span = new ClickableImageSpan(context, bitmap3) {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.parse("file://"+m.group(1)),"video/mp4");
                        startActivity(i);
                    }
                };
                ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }

    public static Bitmap addPlayIcon(Bitmap bitmap) {
        Bitmap icon = BitmapFactory.decodeResource(TApplication.instance.getApplicationContext().getResources(), R.drawable.play);
        Bitmap newPhoto = Bitmap.createBitmap(500,700, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newPhoto);
        canvas.drawBitmap(bitmap,0,0,null);
        canvas.drawBitmap(icon, (bitmap.getWidth() - icon.getWidth()) / 2, (bitmap.getHeight() - icon.getHeight()) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        icon.recycle();
        return newPhoto;
    }

    private void initView() {
        content_tv = (TextView) findViewById(R.id.content_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        back = (ImageView) findViewById(R.id.back);
        edit = (ImageButton) findViewById(R.id.note_show_edit);
        delete = (ImageButton) findViewById(R.id.note_show_delete);
    }

    private void initEvent() {
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        back.setOnClickListener(this);
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
                startActivity(i);
                finish();
                break;
            case  R.id.note_show_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("警告");
                dialog.setMessage("确定删除此条信息么？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteNoteFromDB deleter = new DeleteNoteFromDB(NoteShowActivity.this);
                        deleter.delete(date);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
        }
    }

}
