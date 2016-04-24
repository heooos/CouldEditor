package com.zhanghao.youdaonote.entity;

import cn.bmob.v3.BmobObject;

/**
 *
 * Created by ZH on 2016/3/3.
 */
public class NoteTable extends BmobObject {

    private String noteContent;
    private String noteTitle;
    private String noteDate;
    private String userName;
    private int isReload;
    private String webUri;

    public String getWebUri() {
        return webUri;
    }
    public void setWebUri(String webUri) {
        this.webUri = webUri;
    }
    public int getIsReload() {
        return isReload;
    }
    public void setIsReload(int isReload) {
        this.isReload = isReload;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
    public void setNoteDate(String date) {
        this.noteDate = date;
    }
    public String getUserName() {
        return userName;
    }
    public String getNoteContent() {
        return noteContent;
    }
    public String getNoteTitle() {
        return noteTitle;
    }
    public String getNoteDate() {
        return noteDate;
    }

}
