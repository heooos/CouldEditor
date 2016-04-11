package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ZH on 2016/3/1.
 */
public class DeleteNoteFromDB {

    private SQLiteDatabase dbWrite;
    private DataBase dataBase;
    private Context context;

    public DeleteNoteFromDB(Context context){
        dataBase = new DataBase(context);
        dbWrite = dataBase.getWritableDatabase();
        this.context = context;
    }

    /**
     * 通过时间作为查询条件，进行数据删除。
     * 本地删除后，通过网络端查询，将网络端数据进行删除！
     * @param date
     */
    public void delete(String date){
        Log.d("deleteNoteFromDB", date);
        Cursor cursor = new QueryNoteFromDB(context).queryByDate(date);
        while (cursor.moveToNext()){
            if (cursor.getString(cursor.getColumnIndex("objectId"))!=null){
                new DeleteNoteFromWebDB(context).delete(cursor.getString(cursor.getColumnIndex("objectId")));
            }
        }
        dbWrite.delete("NoteContent", "date = ?", new String[]{date});
    }


    public void deleteAll(String userName){
        dbWrite.delete("NoteContent", "ID = ?", new String[]{userName});
    }
}
