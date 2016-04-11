package com.zhanghao.youdaonote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 将数据添加到本地数据库
 * Created by ZH on 2016/3/1.
 */
public class AddNoteToDB {

    private SQLiteDatabase dbWrite;
    private DataBase dataBase;

    public AddNoteToDB(Context _context){

        dataBase = new DataBase(_context);
        dbWrite = dataBase.getWritableDatabase();
    }
    public void addToDB(String date,String title,String content,int isReload,String objId,String userName){
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("title", title);
        values.put("content", content);
        values.put("isReload",isReload);
        values.put("objectId",objId);
        values.put("ID", userName);
        dbWrite.insert("NoteContent", null, values);
    }


}
