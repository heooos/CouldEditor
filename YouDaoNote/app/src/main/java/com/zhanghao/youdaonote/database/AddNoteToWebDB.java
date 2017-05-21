package com.zhanghao.youdaonote.database;

import android.content.Context;
import android.widget.Toast;

import com.zhanghao.youdaonote.entity.NoteTable;

import cn.bmob.v3.listener.SaveListener;

/**
 * 数据同步到后台数据库
 * Created by ZH on 2016/3/6.
 */
public class AddNoteToWebDB {

    private String title,content,date,userName;
    private int isReload;
    private Context context;
    private String webUri;

    public AddNoteToWebDB(Context context,String webUri,String title,String content,String date,int isReload,String userName){
        this.isReload = isReload;
        this.context = context;
        this.webUri = webUri;
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
        noteTable.setIsReload(isReload);
        noteTable.setWebUri(webUri);
        noteTable.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "同步成功", Toast.LENGTH_SHORT).show();
                new QueryNoteFromWebDB(context).QueryAndSetObjId(date);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

}
