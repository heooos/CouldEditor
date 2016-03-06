package com.zhanghao.youdaonote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ZH on 2016/3/1.
 */
public class AddNoteToDB {

    private SQLiteDatabase dbWrite;
    private DataBase dataBase;

    private Context context;
    private String date,title,content,userName;

    public AddNoteToDB(Context _context,String _date,String _title,String _content,String userName){

        dataBase = new DataBase(_context);
        dbWrite = dataBase.getWritableDatabase();

        this.context = _context;
        this.date = _date;
        this.title = _title;
        this.content = _content;
        this.userName = userName;
    }
    public void addToDB(boolean tag){
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("title", title);
        values.put("content", content);
        values.put("ID", userName);
        dbWrite.insert("NoteContent", null, values);
        if (tag){
            new AddNoteToWebDB(context,title,content,date,userName);//在此将数据存储到云端
        }
    }


}
