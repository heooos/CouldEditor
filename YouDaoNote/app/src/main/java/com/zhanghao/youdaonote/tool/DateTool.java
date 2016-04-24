package com.zhanghao.youdaonote.tool;

import java.util.Calendar;

/**
 * Created by ZH on 2016/3/7.
 */
public class DateTool {

    /**
     * 用于获取当前系统时间 格式化为yyyy-mm-dd hh:mm:ss
     * @return
     */
    public String getCurrentDate(int tag){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int mon = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        switch (tag){
            case 1:
                String noteDate = year+"-"+mon+"-"+day+" "+hour+":"+min+":"+sec;
                return noteDate;
            case 2:
                String name = year+"_"+mon+"_"+day+"_"+hour+"_"+min+"_"+sec;
                return name;
        }
        return null;
    }
}
