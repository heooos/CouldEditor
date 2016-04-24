package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.adapter.ItemBean;
import com.zhanghao.youdaonote.constants.Conf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZH on 2016/3/1.
 */
public class QueryNoteFromDB {

    private SQLiteDatabase dbRead;
    private DataBase dataBase;

    private List<ItemBean> list;

    public QueryNoteFromDB(Context context){

        list = new ArrayList<>();
        dataBase = new DataBase(context);
        dbRead = dataBase.getReadableDatabase();
    }

    public List<ItemBean> readNote(){
        Cursor cursor = dbRead.rawQuery("select * from NoteContent order by date desc",null);
        String title,content,date;
        int isReload;
        while (cursor.moveToNext()){
            title = cursor.getString(cursor.getColumnIndex("title"));
            content = cursor.getString(cursor.getColumnIndex("content"));
            date = cursor.getString(cursor.getColumnIndex("date"));
            isReload = cursor.getInt(cursor.getColumnIndex("isReload"));
            list.add(new ItemBean(R.drawable.logo,title,content,date,isReload));
        }
        cursor.close();
        return list;
    }

    public Cursor getNoReloadData(int tag){
        switch (tag){
            case Conf.STATE_NOSYNCHRONIZATION:
                Cursor cursor0 = dbRead.rawQuery("select * from NoteContent where isReload = ? order by date desc",new String[]{String.valueOf(Conf.STATE_NOSYNCHRONIZATION)});
                return cursor0;
            case Conf.STATE_EDIT:
                Cursor cursor3 = dbRead.rawQuery("select * from NoteContent where isReload = ? order by date desc",new String[]{String.valueOf(Conf.STATE_EDIT)});
                return cursor3;
        }
        return null;
    }

    public Cursor getNoDowloadData(String objId){
        Cursor cursor = dbRead.rawQuery("select * from NoteContent where objectId = ? order by date desc",new String[]{objId});
        return cursor;
    }

    public Cursor queryByDate(String date){
        Cursor cursor = dbRead.rawQuery("select * from NoteContent where date = ? order by date desc",new String[]{date});
        return cursor;
    }

    public String readContent(String date){

        Cursor cursor = dbRead.rawQuery("select * from NoteContent where date = ?", new String[]{date});
        while (cursor.moveToNext()){
            return cursor.getString(cursor.getColumnIndex("content"));
        }
        cursor.close();
        return null;
    }

    public Cursor queryAll(){
        return dbRead.rawQuery("select * from NoteContent",null);
    }

}
