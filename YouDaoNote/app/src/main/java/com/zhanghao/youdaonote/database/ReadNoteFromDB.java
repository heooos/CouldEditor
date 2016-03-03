package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.adapter.ItemBean;
import com.zhanghao.youdaonote.database.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZH on 2016/3/1.
 */
public class ReadNoteFromDB {

    private SQLiteDatabase dbRead;
    private DataBase dataBase;

    private List<ItemBean> list;

    public ReadNoteFromDB(Context context){

        list = new ArrayList<>();
        dataBase = new DataBase(context);
        dbRead = dataBase.getReadableDatabase();
    }

    public List<ItemBean> readNote(){

        Cursor cursor = dbRead.rawQuery("select * from NoteContent order by _id desc",null);
        String title,content,date;
        while (cursor.moveToNext()){
            title = cursor.getString(cursor.getColumnIndex("title"));
            content = cursor.getString(cursor.getColumnIndex("content"));
            date = cursor.getString(cursor.getColumnIndex("date"));
            list.add(new ItemBean(R.drawable.logo,title,content,date));
        }
        return list;
    }

}
