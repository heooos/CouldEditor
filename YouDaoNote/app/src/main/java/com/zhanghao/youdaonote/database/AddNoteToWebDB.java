package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.widget.Toast;

import com.zhanghao.youdaonote.entity.NoteTable;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ZH on 2016/3/6.
 */
public class AddNoteToWebDB {

    private String title,content,date,userName;
    private Context context;

    public AddNoteToWebDB(Context context,String title,String content,String date,String userName){

        this.context = context;
        this.title =title;
        this.content = content;
        this.date = date;
        this.userName = userName;
        addToWeb();
    }

    private void addToWeb() {
        NoteTable noteTable = new NoteTable();
        noteTable.setNoteTitle(title);
        noteTable.setNoteContent(content);
        noteTable.setNoteDate(date);
        noteTable.setUserName(userName);
        noteTable.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "网络端保存成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

}
