package com.zhanghao.youdaonote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.zhanghao.youdaonote.tool.DateTool;
import com.zhanghao.youdaonote.TApplication;
import com.zhanghao.youdaonote.entity.NoteTable;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ZH on 2016/3/6.
 */
public class QueryNoteFromWebDB {

    private final Context context;
    private  AddNoteToDB addNoteToDB;
    private IRefreshListener iRefreshListener;
    private String userName;
    private QueryNoteFromDB queryNoteFromDB;

    public QueryNoteFromWebDB(final Context context){
        this.context = context;
        queryNoteFromDB = new QueryNoteFromDB(context);
        if (TApplication.instance.hasCurrentUser()){
            userName = new BmobUser().getCurrentUser(context).getUsername();
        }
    }

    /**
     * 同步云端数据到本地！ 将当前用户id作为查询字。将所有数据同步到本地。
     */
    public void synchroData() {

        BmobQuery<NoteTable> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", userName);
        query.setLimit(50);
        query.findObjects(context, new FindListener<NoteTable>() {
            @Override
            public void onSuccess(List<NoteTable> list) {

                for (NoteTable noteTable:list){
                    Cursor cursor1 = queryNoteFromDB.getNoDowloadData(noteTable.getObjectId());
                    Cursor cursor2 = queryNoteFromDB.queryByDate(noteTable.getNoteDate());
                    if ((!cursor1.moveToNext()) && (!cursor2.moveToNext())){
                        addNoteToDB = new AddNoteToDB(context);
                        addNoteToDB.addToDB(noteTable.getNoteDate(),noteTable.getNoteTitle(),noteTable.getNoteContent(),noteTable.getIsReload(),noteTable.getObjectId(),userName);
                    }
                }
                iRefreshListener.onRefresh();
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }

    public void setInterface(IRefreshListener iRefreshListener){
        this.iRefreshListener = iRefreshListener;
    }
    /**
     * 刷新数据接口
     */
    public interface IRefreshListener{
        void onRefresh();
    }

    public void QueryNoteAndUpdate(final String objId, final String title, final String content){
        BmobQuery<NoteTable> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objId);
        query.findObjects(context, new FindListener<NoteTable>() {
            @Override
            public void onSuccess(List<NoteTable> list) {
                for (NoteTable noteTable:list){
                    Log.d("noteTable",noteTable.getObjectId());
                    new UpdateNoteToWebDB(context).update(noteTable.getObjectId(), title, content, new DateTool().getCurrentDate(1));
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public void QueryAndSetObjId(final String date){
        BmobQuery<NoteTable> query = new BmobQuery<>();
        query.addWhereEqualTo("noteDate", date);
        query.findObjects(context, new FindListener<NoteTable>() {
            @Override
            public void onSuccess(List<NoteTable> list) {
                for (NoteTable noteTable:list){
                    ContentValues values = new ContentValues();
                    values.put("objectId",noteTable.getObjectId());
                    new UpdateNoteToDB(context).updateForSynchronization(values,date);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

}
