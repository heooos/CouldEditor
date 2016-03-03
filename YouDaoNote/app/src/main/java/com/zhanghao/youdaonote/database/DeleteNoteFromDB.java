package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ZH on 2016/3/1.
 */
public class DeleteNoteFromDB {

    private SQLiteDatabase dbWrite;
    private DataBase dataBase;
    private String date;

    public DeleteNoteFromDB(Context context,String date){
        dataBase = new DataBase(context);
        dbWrite = dataBase.getWritableDatabase();
        this.date = date;
        delete();
    }
    public void delete(){
        dbWrite.delete("NoteContent", "date = ?", new String[]{date});
    }
}
