package com.zhanghao.youdaonote.constants;

/**
 * Created by ZH on 2016/3/1.
 */
public class Conf {

    public static final String APP_ID = "fb12c915c4c1c1010e4b5574d4f6237c";
    public static final short START_TO_LOGIN = 1;
    public static final short START_TO_MAIN = 2;
    public static final int ACTIVITY_CODE = 3;  //请求码

    public static final int NICKNAME_SETTING = 1; //同步后的view类型
    public static final int SEX_SETTING = 2; //未同步的view类型

    public static final int AREA_SETTING = 3;
    public static final int RESUME_SETTING = 4;

    public static final int STATE_NOSYNCHRONIZATION = 0;  //内容未进行同步
    public static final int STATE_SYNCHRONIZATION = 1;   //内容已同步
    public static final int STATE_EDIT = 3;    //已修改
}
