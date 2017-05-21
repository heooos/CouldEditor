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
import android.widget.Toast;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.TApplication;
import com.zhanghao.youdaonote.database.DeleteNoteFromDB;
import com.zhanghao.youdaonote.entity.ClickableImageSpan;
import com.zhanghao.youdaonote.entity.ClickableMovementMethod;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;

public class NoteShowActivity extends BaseActivity implements View.OnClickListener {

    private TextView content_tv;
    private TextView title_tv;
    private ImageView back;
    private ImageButton edit, delete;
    private String title, content, date, webUri;
    private String downloadName;

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
        webUri = getIntent().getStringExtra("webUri");

        SpannableString ss = getSpannableString(content, this);

        title_tv.setText(title);
        content_tv.setText(ss);

        content_tv.setMovementMethod(ClickableMovementMethod.getInstance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public SpannableString getSpannableString(String str, Context context) {
        SpannableString ss = new SpannableString(str);
        Pattern p = Pattern.compile("((?:\\/[\\w\\.\\-]+)+)");
        final Matcher m = p.matcher(str);
        if (m.find()) {
            File f = new File(m.group(1));

            Pattern pName = Pattern.compile("^\\d{4}(_\\d{1,2}){5}.\\w{3}");
            Matcher mName = pName.matcher(f.getName());

            if (mName.find()){
               downloadName = mName.group(0);
            }

            if (!f.exists()) {
                Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
                //文件不存在的时候 先放置一个默认加载图片 然后去获取网络url 通过网络下载 成功后 替代默认图片
                Bitmap bitmapNormal = BitmapFactory.decodeResource(getResources(), R.drawable.loadingimage);
                Bitmap rbm = NoteEditActivity.resizeImage(bitmapNormal, 150, 200);
                ClickableImageSpan span = new ClickableImageSpan(context, rbm) {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(NoteShowActivity.this, "正在下载 请稍后", Toast.LENGTH_SHORT).show();
                        BmobFile bmobFile = new BmobFile(downloadName, null, webUri);
                        bmobFile.download(NoteShowActivity.this, new File(TApplication.filePath,downloadName), new DownloadFileListener() {
                            @Override
                            public void onSuccess(String s) {
                                Toast.makeText(NoteShowActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(NoteShowActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return ss;
            } else {
                String fileName = f.getName();
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                Log.d("文件类型", prefix);

                if (prefix.equals("jpg")) {
                    Bitmap bm = BitmapFactory.decodeFile(m.group(1));
                    Bitmap rbm = NoteEditActivity.resizeImage(bm, 500, 700);
                    ClickableImageSpan span = new ClickableImageSpan(context, rbm) {
                        @Override
                        public void onClick(View view) {

                        }
                    };
                    ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (prefix.equals("mp4")) {
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(m.group(1));
                    Bitmap bitmap1 = media.getFrameAtTime();
                    Bitmap bitmap2 = NoteEditActivity.resizeImage(bitmap1, 500, 700);
                    Bitmap bitmap3 = addPlayIcon(bitmap2);
                    ClickableImageSpan span = new ClickableImageSpan(context, bitmap3) {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(Uri.parse("file://" + m.group(1)), "video/mp4");
                            startActivity(i);
                        }
                    };
                    ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return ss;
    }

    public static Bitmap addPlayIcon(Bitmap bitmap) {
        Bitmap icon = BitmapFactory.decodeResource(TApplication.instance.getApplicationContext().getResources(), R.drawable.play);
        Bitmap newPhoto = Bitmap.createBitmap(500, 700, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newPhoto);
        canvas.drawBitmap(bitmap, 0, 0, null);
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
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.note_show_edit:
                Intent i = new Intent(this, NoteEditActivity.class);
                i.putExtra("title", title_tv.getText().toString());
                i.putExtra("content", content_tv.getText().toString());
                i.putExtra("date", date);
                i.putExtra("className", "NoteShowActivity");
                startActivity(i);
                finish();
                break;
            case R.id.note_show_delete:
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
