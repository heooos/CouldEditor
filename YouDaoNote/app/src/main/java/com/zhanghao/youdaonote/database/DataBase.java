package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ZH on 2016/2/24.
 */
public class DataBase extends SQLiteOpenHelper {
    public DataBase(Context context) {
        super(context, "note.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NoteContent("+
        "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
        "ID INTEGER DEFAULT \"\","+
        "data TEXT DEFAULT \"\","+
        "title TEXT DEFAULT \"\","+
        "content TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
