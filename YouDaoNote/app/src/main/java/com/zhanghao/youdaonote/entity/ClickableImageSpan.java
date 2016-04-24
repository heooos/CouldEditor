package com.zhanghao.youdaonote.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * Created by ZH on 2016/4/19.
 */
public abstract  class ClickableImageSpan extends ImageSpan {


    public ClickableImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public abstract void onClick(View view);
}
