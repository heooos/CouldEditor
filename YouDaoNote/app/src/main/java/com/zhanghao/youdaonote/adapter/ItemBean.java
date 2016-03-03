package com.zhanghao.youdaonote.adapter;

/**
 * Created by ZH on 2016/2/25.
 */
public class ItemBean {

    public int icon;
    public String noteTitle;
    public String noteDate;
    public String noteContent;

    public ItemBean(int icon, String noteTitle,String noteContent,String noteDate){
        this.icon = icon;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteDate = noteDate;
    }

}
