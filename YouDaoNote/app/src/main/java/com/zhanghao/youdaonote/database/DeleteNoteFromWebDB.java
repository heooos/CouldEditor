package com.zhanghao.youdaonote.database;

import android.content.Context;

import com.zhanghao.youdaonote.entity.NoteTable;

import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by ZH on 2016/3/6.
 */
public class DeleteNoteFromWebDB {

    private Context context;

    public DeleteNoteFromWebDB(Context context){
        this.context = context;
    }

    public void delete(String objId){
        NoteTable table = new NoteTable();
        table.setObjectId(objId);
        table.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(int i, String s) {
            }
        });

    }


}
