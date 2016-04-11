package com.zhanghao.youdaonote.database;

import android.content.Context;

import com.zhanghao.youdaonote.constants.Conf;
import com.zhanghao.youdaonote.entity.NoteTable;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ZH on 2016/3/7.
 */
public class UpdateNoteToWebDB {

    private Context context;

    public UpdateNoteToWebDB(Context context){
        this.context = context;
    }

    public void update(String objId,String title,String content,String date){
        NoteTable table = new NoteTable();
        table.setNoteTitle(title);
        table.setNoteContent(content);
        table.setNoteDate(date);
        table.setIsReload(Conf.STATE_SYNCHRONIZATION);
        table.update(context,objId,new UpdateListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

}
