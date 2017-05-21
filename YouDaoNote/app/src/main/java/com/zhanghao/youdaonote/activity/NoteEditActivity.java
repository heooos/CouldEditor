package com.zhanghao.youdaonote.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.TApplication;
import com.zhanghao.youdaonote.constants.Conf;
import com.zhanghao.youdaonote.database.AddNoteToDB;
import com.zhanghao.youdaonote.database.QueryNoteFromDB;
import com.zhanghao.youdaonote.database.UpdateNoteToDB;
import com.zhanghao.youdaonote.tool.DateTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ZH on 2016/2/24.
 */
public class NoteEditActivity extends BaseActivity implements View.OnClickListener {

    private EditText titleEditText, contentEditText;
    private String title, content, date;
    private ScrollView view;
    private ImageView back, save, takePhotos, takeVideo;
    private Uri originalUri;
//    private String filePath;
    private String FILE_NAME;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.note_edit_activity);

//        filePath = Environment.getExternalStorageDirectory().toString() + "/youdaoNote";
//        Log.d("根目录", filePath);

        initView();
        initEvent();

        if (savedInstanceState != null) {
            Bundle b = savedInstanceState.getBundle("savedState");
            if (b != null) {
                title = b.getString("title");
                content = b.getString("content");
            }
        }

        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");

        if (title != null) {
            titleEditText.setText(title);
        }
        if (date != null) {
            content = new QueryNoteFromDB(this).readContent(date);
            SpannableString ss = new NoteShowActivity().getSpannableString(content, NoteEditActivity.this);

            contentEditText.setText(ss);
            contentEditText.setSelection(ss.length());
            contentEditText.requestFocus();
        }
    }


    private void initView() {

        titleEditText = (EditText) findViewById(R.id.note_edit_title);
        contentEditText = (EditText) findViewById(R.id.note_edit_content);
        view = (ScrollView) findViewById(R.id.myScrollView);
        back = (ImageView) findViewById(R.id.back);
        save = (ImageView) findViewById(R.id.save);
        contentEditText.requestFocus();
        takePhotos = (ImageView) findViewById(R.id.note_edit_photo);
        takeVideo = (ImageView) findViewById(R.id.note_edit_video);
    }

    private void initEvent() {

        view.setOnClickListener(this);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        takePhotos.setOnClickListener(this);
        takeVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myScrollView:
                contentEditText.requestFocus();
                break;
            case R.id.save:
                onSave();
                break;
            case R.id.back:
                onBack();
                break;
            case R.id.note_edit_photo:
                ChoosePhoto();
                break;
            case R.id.note_edit_video:
                ChooseVideo();
                break;
        }
    }

    private void ChooseVideo() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.alertdialog_video_cell, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选取视频");
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        //摄像
        v.findViewById(R.id.videos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.media.action.VIDEO_CAPTURE");
                intent.addCategory("android.intent.category.DEFAULT");
                File file = new File(TApplication.filePath + "/" + new DateTool().getCurrentDate(2) + ".mp4");
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 0);
                dialog.dismiss();
            }
        });
        //文件中选取
        v.findViewById(R.id.files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("video/*");
                startActivityForResult(getImage, 3);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 选取图片
     */
    private void ChoosePhoto() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.alertdialog_photo_cell, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选取照片");
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        //拍照
        v.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        //相册选取
        v.findViewById(R.id.photos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/*");
                startActivityForResult(getImage, 1);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 复制文件到指定文件夹下
     *
     * @param bitmap
     */
    private void CreateFile(Bitmap bitmap) {
//        File file = new File(filePath);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
        new AsyncTask<Bitmap, Void, File>() {

            @Override
            protected File doInBackground(Bitmap... params) {
                FILE_NAME = new DateTool().getCurrentDate(2);
                FileOutputStream fos;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                params[0].compress(Bitmap.CompressFormat.JPEG, 100, baos);
                InputStream in = new ByteArrayInputStream(baos.toByteArray());

                File file = new File(TApplication.filePath, FILE_NAME + ".jpg");

                try {
                    fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    fos.flush();
                    fos.close();
                    in.close();

                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return file;
            }

            @Override
            protected void onPostExecute(File file) {
                Bitmap _bitmap = BitmapFactory.decodeFile(file.getPath());
                Uri _uri = Uri.fromFile(file);
                if (_bitmap != null) {
                    insertIntoEditText(getBitmapMime(NoteEditActivity.this, _bitmap, _uri));
                } else {
                    Toast.makeText(NoteEditActivity.this, "获取图片失败",
                            Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(file);
            }
        }.execute(bitmap);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Bitmap bitmap;
                    originalUri = data.getData();
                    try {
                        Bitmap originalBitmap = BitmapFactory.decodeStream(resolver
                                .openInputStream(originalUri));
                        bitmap = resizeImage(originalBitmap, 500, 700);
                        CreateFile(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Bitmap bitmap1 = data.getParcelableExtra("data");
                    Bitmap bitmap2 = resizeImage(bitmap1, 500, 700);
                    CreateFile(bitmap2);
                    break;
                case 0:
                    String path = data.getData().getPath();
                    Log.d("录像", path);
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(path);
                    Bitmap bitmap3 = media.getFrameAtTime();
                    Bitmap bitmap4 = resizeImage(bitmap3, 500, 700);
                    Bitmap bitmap5 = NoteShowActivity.addPlayIcon(bitmap4);
                    insertIntoEditText(getBitmapMime(NoteEditActivity.this, bitmap5, data.getData()));
                    break;
                case 3:
                    String path1 = data.getData().getPath();
                    Log.d("视频文件地址", path1);
                    MediaMetadataRetriever media1 = new MediaMetadataRetriever();
                    media1.setDataSource(path1);
                    Bitmap bitmap6 = media1.getFrameAtTime();
                    Bitmap bitmap7 = resizeImage(bitmap6, 500, 700);
                    Bitmap bitmap8 = NoteShowActivity.addPlayIcon(bitmap7);
                    insertIntoEditText(getBitmapMime(NoteEditActivity.this, bitmap8, data.getData()));
                    break;
            }
        }
    }

    public static SpannableString getBitmapMime(Context context, Bitmap pic, Uri uri) {
        //在此处获取到图片的位置 插入到ss中
        String path = uri.getPath();
        SpannableString ss = new SpannableString(path);
        ImageSpan span = new ImageSpan(context, pic);
        ss.setSpan(span, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    private void insertIntoEditText(SpannableString ss) {
        Editable et = contentEditText.getText();// 先获取Edittext中的内容
        int start = contentEditText.getSelectionStart();
        et.insert(start, ss);// 设置ss要添加的位置
        contentEditText.setText(et);// 把et添加到Edittext中
        contentEditText.setSelection(start + ss.length());// 设置Edittext中光标在最后面显示
    }

    /**
     * 修改bitmap大小
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    /**
     * 保存按钮逻辑
     */
    private void onSave() {
        if (!(titleEditText.getText().toString().equals("") && contentEditText.getText().toString().equals(""))) {
            addNoteToDB();
            finish();
        } else {
            Toast.makeText(this, "内容为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回按钮逻辑
     *
     * @return
     */
    private void onBack() {
        if (!(titleEditText.getText().toString().equals("") && contentEditText.getText().toString().equals(""))) {
            //  非空退出提示
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("警告");
            dialog.setMessage("当前笔记不为空，退出讲丢失已记录内容！");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NoteEditActivity.this.finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        } else {
            finish();
        }
    }


    /**
     * 用于保存意外退出的状态
     *
     * @return
     */
    private Bundle saveState() {

        Bundle bundle = new Bundle();
        bundle.putString("title", titleEditText.getText().toString());
        bundle.putString("content", contentEditText.getText().toString());
        return bundle;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("savedState", saveState());
    }


    /**
     * 添加数据到数据库
     */
    private void addNoteToDB() {
        if (getIntent().getStringExtra("className").equals("NoteFragment")) {
            AddNoteToDB addNoteToDB = new AddNoteToDB(this);
            addNoteToDB.addToDB(new DateTool().getCurrentDate(1), titleEditText.getText().toString(), contentEditText.getText().toString(), Conf.STATE_NOSYNCHRONIZATION, null, null);
        } else {
            UpdateNoteToDB updater = new UpdateNoteToDB(this);
            QueryNoteFromDB queryNoteFromDB = new QueryNoteFromDB(this);
            Cursor cursor = queryNoteFromDB.queryByDate(getIntent().getStringExtra("date"));
            Log.d("getIntent()", getIntent().getStringExtra("date"));
            cursor.moveToNext();
            if (cursor.getInt(cursor.getColumnIndex("isReload")) == Conf.STATE_NOSYNCHRONIZATION) {
                updater.update("",titleEditText.getText().toString(), contentEditText.getText().toString(), date, Conf.STATE_NOSYNCHRONIZATION);
            } else {
                updater.update("",titleEditText.getText().toString(), contentEditText.getText().toString(), date, Conf.STATE_EDIT);
            }
        }
    }

}
