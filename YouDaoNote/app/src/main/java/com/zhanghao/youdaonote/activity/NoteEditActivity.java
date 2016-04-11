package com.zhanghao.youdaonote.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.zhanghao.youdaonote.DateTool;
import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.constants.Conf;
import com.zhanghao.youdaonote.database.AddNoteToDB;
import com.zhanghao.youdaonote.database.QueryNoteFromDB;
import com.zhanghao.youdaonote.database.UpdateNoteToDB;

/**
 * Created by ZH on 2016/2/24.
 */
public class NoteEditActivity extends BaseActivity implements View.OnClickListener {

    private EditText titleEditText,contentEditText;
    private String title,content,date;
    private ScrollView view;
    private ImageView back,save, italic,takePhotos;
    private Uri originalUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.note_edit_activity);

        initView();
        initEvent();

        if (savedInstanceState != null){
            Bundle b = savedInstanceState.getBundle("savedState");
            if (b != null){
                title = b.getString("title");
                content = b.getString("content");
            }
        }

        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");

        if (title != null){
            titleEditText.setText(title);
        }
        if ( date != null){
            content = new QueryNoteFromDB(this).readContent(date);
            contentEditText.setText(content);
            contentEditText.setSelection(content.length());
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
        italic = (ImageView) findViewById(R.id.note_edit_xieti);
        takePhotos = (ImageView) findViewById(R.id.note_edit_photo);
    }

    private void initEvent(){

        view.setOnClickListener(this);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        italic.setOnClickListener(this);
        takePhotos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myScrollView:
                contentEditText.requestFocus();
                break;
            case R.id.save:
                onSave();
                break;
            case R.id.back:
                onBack();
                break;
            case R.id.note_edit_xieti:
                if (contentEditText.isFocused()){
                    contentEditText.getText().append("<i></i>");
                    Selection.setSelection(contentEditText.getText(),contentEditText.getText().length() - 4);
                }
                break;
            case R.id.note_edit_photo:

                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/*");
                startActivityForResult(getImage, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK){

            if (requestCode == 1){
                originalUri = data.getData();
            }

        }

    }

    private void addImageSpan(Bitmap b){

        ImageSpan span = new ImageSpan(this,b);

        SpannableString spanString = new SpannableString("[local]"+1+"[/local]");
        spanString.setSpan(span, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int index = contentEditText.getSelectionStart();
        Editable editable = contentEditText.getEditableText();
        if(index <0 || index >= editable.length()){
            editable.append(spanString);
        }else{
            editable.insert(index, spanString);
        }
    }

    /**
     * 图片缩放
     * @return 缩放后的Bitmap
     */
    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //定义欲转换成的宽、高
//      int newWidth = 200;
//      int newHeight = 200;
        //计算宽、高缩放率
        float scanleWidth = (float)newWidth/width;
        float scanleHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth,scanleHeight);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
        return resizedBitmap;
    }


    @Override
    public void onBackPressed() {
        onBack();
        //super.onBackPressed();
    }

    /**
     * 保存按钮逻辑
     */
    private void onSave() {
        if (!(titleEditText.getText().toString().equals("") && contentEditText.getText().toString().equals(""))){
            addNoteToDB();
            finish();
        }else {
            Toast.makeText(this,"内容为空",Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * 返回按钮逻辑
     * @return
     */
    private void onBack(){
        if (!(titleEditText.getText().toString().equals("") && contentEditText.getText().toString().equals(""))){
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
        }else {
            finish();
         }
    }


    private Bundle saveState(){

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
    private void addNoteToDB(){
        if (getIntent().getStringExtra("className").equals("NoteFragment")){
            AddNoteToDB addNoteToDB = new AddNoteToDB(this);
            addNoteToDB.addToDB(new DateTool().getCurrentDate(),titleEditText.getText().toString(),contentEditText.getText().toString(), Conf.STATE_NOSYNCHRONIZATION, null,null);
        }else {
            UpdateNoteToDB updater = new UpdateNoteToDB(this);
            QueryNoteFromDB queryNoteFromDB = new QueryNoteFromDB(this);
            Cursor cursor = queryNoteFromDB.queryByDate(getIntent().getStringExtra("date"));
            Log.d("getIntent()",getIntent().getStringExtra("date"));
            cursor.moveToNext();
            if (cursor.getInt(cursor.getColumnIndex("isReload")) == Conf.STATE_NOSYNCHRONIZATION){
                updater.update(titleEditText.getText().toString(),contentEditText.getText().toString(),date,Conf.STATE_NOSYNCHRONIZATION);
            }else {
                updater.update(titleEditText.getText().toString(),contentEditText.getText().toString(),date,Conf.STATE_EDIT);
            }
        }
    }

}
