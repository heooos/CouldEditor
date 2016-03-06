package com.zhanghao.youdaonote.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.database.AddNoteToDB;
import com.zhanghao.youdaonote.database.QueryNoteFromDB;
import com.zhanghao.youdaonote.database.UpdateNoteToDB;

import java.io.FileNotFoundException;
import java.util.Calendar;

import cn.bmob.v3.BmobUser;

/**
 * Created by ZH on 2016/2/24.
 */
public class NoteEditActivity extends BaseActivity implements View.OnClickListener {

    private EditText titleEditText,contentEditText;
    private String title,content,date;
    private ScrollView view;
    private ImageView back,xieti,takePhotos;
    private  Uri imageUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.note_edit_activity);
        if (savedInstanceState != null){
            Bundle b = savedInstanceState.getBundle("savedState");
            if (b != null){
                title = b.getString("title");
                content = b.getString("content");
            }
        }

        init();
        initEvent();

        title = getIntent().getStringExtra("title");
      //  content = getIntent().getStringExtra("content");
        date = getIntent().getStringExtra("date");

        if (title != null){
            Log.d("NoteEditActivity", title);
            titleEditText.setText(title);
        }
        if (/*content != null || */ date != null){
            content = new QueryNoteFromDB(this).readContent(date);
            Log.d("0.0", content);

            contentEditText.setText(content);
            contentEditText.setSelection(content.length());
            contentEditText.requestFocus();
        }
    }

    private  void init() {

        titleEditText = (EditText) findViewById(R.id.note_edit_title);
        contentEditText = (EditText) findViewById(R.id.note_edit_content);
        view = (ScrollView) findViewById(R.id.myScrollView);
        back = (ImageView) findViewById(R.id.back);
        contentEditText.requestFocus();
        xieti = (ImageView) findViewById(R.id.note_edit_xieti);
        takePhotos = (ImageView) findViewById(R.id.note_edit_photo);
    }

    private void initEvent(){

        view.setOnClickListener(this);
        back.setOnClickListener(this);
        xieti.setOnClickListener(this);
        takePhotos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myScrollView:
                contentEditText.requestFocus();
                break;
            case R.id.back:
                onFinish();
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

        switch (requestCode ){
            case 1:
                try {
                    Uri originalUri = data.getData();
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(originalUri));
                    bitmap = resizeImage(bitmap, 200, 200);
                    if (bitmap!=null){
                        addImageSpan(bitmap);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
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


    /**
     *
     * 返回数据到上一活动
     */
    private void setResultForBack(){
        Intent intent = new Intent();
        intent.putExtra("title", titleEditText.getText().toString());
        intent.putExtra("content", contentEditText.getText().toString());
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "笔记已经保存", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        onFinish();
        super.onBackPressed();
    }

    /**
     * 根据控件状态选择返回类型
     */
    private void onFinish() {
        if (!(titleEditText.getText().toString().equals("") && contentEditText.getText().toString().equals(""))){
            addNoteToDB();
            setResultForBack();
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
     * 用于获取当前系统时间 格式化为yyyy-mm-dd hh:mm:ss
     * @return
     */
    private String getCurrentDate(){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int mon = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE)+1;
        int sec = c.get(Calendar.SECOND);
        String noteDate = year+"-"+mon+"-"+day+" "+hour+":"+min+":"+sec;
        return noteDate;
    }

    /**
     * 添加数据到数据库
     */
    private void addNoteToDB(){
        if (getIntent().getStringExtra("className").equals("NoteFragment")){
            AddNoteToDB addNoteToDB = new AddNoteToDB(this,getCurrentDate(),titleEditText.getText().toString(),contentEditText.getText().toString(), BmobUser.getCurrentUser(this).getUsername());
            addNoteToDB.addToDB(true);
        }else {
            UpdateNoteToDB updater = new UpdateNoteToDB(this,titleEditText.getText().toString(),contentEditText.getText().toString(),date);
            updater.update();
        }
    }

}
