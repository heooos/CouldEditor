package com.zhanghao.youdaonote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ZH on 2016/3/1.
 */
public class UpdateNoteToDB {

    private SQLiteDatabase dbWrite;
    private DataBase dataBase;
    private String title,content,date;

    public UpdateNoteToDB(Context context,String title,String content,String date){

        this.title = title;
        this.content = content;
        this.date = date;

        dataBase = new DataBase(context);
        dbWrite = dataBase.getWritableDatabase();

        update();
    }

    public void update(){
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("content",content);
        dbWrite.update("NoteContent",values,"date = ?",new String[]{date});

    }
}
