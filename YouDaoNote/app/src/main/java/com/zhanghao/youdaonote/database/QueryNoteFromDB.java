package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.adapter.ItemBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by ZH on 2016/3/1.
 */
public class QueryNoteFromDB {

    private SQLiteDatabase dbRead;
    private DataBase dataBase;
    private Context context;

    private List<ItemBean> list;

    public QueryNoteFromDB(Context context){

        list = new ArrayList<>();
        dataBase = new DataBase(context);
        dbRead = dataBase.getReadableDatabase();
        this.context = context;
    }

    public List<ItemBean> readNote(){

        Cursor cursor = dbRead.rawQuery("select * from NoteContent where ID = ? order by _id desc",new String[]{BmobUser.getCurrentUser(context).getUsername()});
        String title,content,date;
        while (cursor.moveToNext()){
            title = cursor.getString(cursor.getColumnIndex("title"));
            content = cursor.getString(cursor.getColumnIndex("content"));
            date = cursor.getString(cursor.getColumnIndex("date"));
            list.add(new ItemBean(R.drawable.logo,title,content,date));
        }
        cursor.close();
        return list;
    }

    public String readContent(String date){

        Cursor cursor = dbRead.rawQuery("select * from NoteContent where date = ?", new String[]{date});
        while (cursor.moveToNext()){
            return cursor.getString(cursor.getColumnIndex("content"));
        }
        cursor.close();
        return null;
    }

}
