package com.zhanghao.youdaonote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhanghao.youdaonote.tool.DateTool;

/**
 * Created by ZH on 2016/3/1.
 */
public class UpdateNoteToDB {

    private SQLiteDatabase dbWrite;
    private DataBase dataBase;
    private Context context;

    public UpdateNoteToDB(Context context){

        dataBase = new DataBase(context);
        dbWrite = dataBase.getWritableDatabase();
        this.context = context;
    }

    public void updateForSynchronization(ContentValues values,String date){
        dbWrite.update("NoteContent", values, "date = ?", new String[]{date});
    }

    public void update(String webUri,String title,String content,String date,int isReload){
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("webUri",webUri);
        values.put("content", content);
        values.put("isReload",isReload);
        values.put("date", new DateTool().getCurrentDate(1));
        dbWrite.update("NoteContent", values, "date = ?", new String[]{date});
    }

}
