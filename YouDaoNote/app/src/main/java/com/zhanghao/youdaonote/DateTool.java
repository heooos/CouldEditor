package com.zhanghao.youdaonote;

import java.util.Calendar;

/**
 * Created by ZH on 2016/3/7.
 */
public class DateTool {

    /**
     * 用于获取当前系统时间 格式化为yyyy-mm-dd hh:mm:ss
     * @return
     */
    public String getCurrentDate(){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int mon = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        String noteDate = year+"-"+mon+"-"+day+" "+hour+":"+min+":"+sec;
        return noteDate;
    }

}
