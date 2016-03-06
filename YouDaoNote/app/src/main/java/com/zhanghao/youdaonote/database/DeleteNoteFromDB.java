package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ZH on 2016/3/1.
 */
public class DeleteNoteFromDB {

    private SQLiteDatabase dbWrite;
    private DataBase dataBase;

    public DeleteNoteFromDB(Context context){
        dataBase = new DataBase(context);
        dbWrite = dataBase.getWritableDatabase();
    }
    public void delete(String date){
        dbWrite.delete("NoteContent", "date = ?", new String[]{date});
    }

    public void deleteAll(String userName){
        dbWrite.delete("NoteContent", "ID = ?", new String[]{userName});
    }
}
