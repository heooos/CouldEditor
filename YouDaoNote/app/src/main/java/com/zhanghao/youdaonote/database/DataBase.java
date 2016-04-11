package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库
 * Created by ZH on 2016/2/24.
 * ID 用户名
 * isReload 标记是否同步
 * objectId 同步后的笔记唯一标识
 * date 时间
 * title 标题
 * content 内容
 */
public class DataBase extends SQLiteOpenHelper {
    public DataBase(Context context) {
        super(context, "note.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NoteContent("+
        "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
        "ID TEXT DEFAULT \"\","+
        "isReload INTEGER DEFAULT \"\","+
        "objectId TEXT DEFAULT \"\","+
        "date TEXT DEFAULT \"\","+
        "title TEXT DEFAULT \"\","+
        "content TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
