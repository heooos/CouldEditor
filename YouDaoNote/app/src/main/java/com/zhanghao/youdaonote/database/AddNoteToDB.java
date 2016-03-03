package com.zhanghao.youdaonote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.zhanghao.youdaonote.entity.NoteTable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ZH on 2016/3/1.
 */
public class AddNoteToDB {

    private SQLiteDatabase dbWrite;
    private DataBase dataBase;

    private Context context;
    private String date,title,content;

    public AddNoteToDB(Context _context,String _date,String _title,String _content){

        dataBase = new DataBase(_context);
        dbWrite = dataBase.getWritableDatabase();

        this.context = _context;
        this.date = _date;
        this.title = _title;
        this.content = _content;

    }
    public void addToDB(){
        ContentValues values = new ContentValues();
        values.put("date",date);
        values.put("title", title);
        values.put("content", content);
        dbWrite.insert("NoteContent", null, values);
        addToWeb();
    }

    private void addToWeb() {
        NoteTable noteTable = new NoteTable();
        noteTable.setNoteTitle(title);
        noteTable.setNoteContent(content);
        noteTable.setDate(date);
        noteTable.setUserName(BmobUser.getCurrentUser(context).getUsername());
        noteTable.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "网络段保存成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

}
