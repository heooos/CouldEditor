package com.zhanghao.youdaonote.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZH on 2016/4/18.
 */
public class IsIncludeFile {
    public Matcher isIncludePath(String str){
        Pattern p = Pattern.compile("((?:\\/[\\w\\.\\-]+)+)");
        Matcher m = p.matcher(str);
        return m;
    }
}
