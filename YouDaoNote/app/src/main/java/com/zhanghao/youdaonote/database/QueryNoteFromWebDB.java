package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.widget.Toast;

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
    private DeleteNoteFromDB deleteNoteFromDB;
    private IRefreshListener iRefreshListener;

    public QueryNoteFromWebDB(final Context context){
        this.context = context;
    }

    public void synchroData() {
        final String userName = new BmobUser().getCurrentUser(context).getUsername();
        BmobQuery<NoteTable> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", userName);
        query.setLimit(50);
        query.findObjects(context, new FindListener<NoteTable>() {
            @Override
            public void onSuccess(List<NoteTable> list) {
                deleteNoteFromDB = new DeleteNoteFromDB(context);
                deleteNoteFromDB.deleteAll(userName);
                for (NoteTable noteTable:list){
                    addNoteToDB = new AddNoteToDB(context,noteTable.getNoteDate(),noteTable.getNoteTitle(),noteTable.getNoteContent(),userName);
                    addNoteToDB.addToDB(false);
                }
                iRefreshListener.onRefresh();
                Toast.makeText(context, "云端数据同步成功", Toast.LENGTH_SHORT).show();
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

}
