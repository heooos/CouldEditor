package com.zhanghao.youdaonote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhanghao.youdaonote.R;

/**
 * Created by ZH on 2016/2/21.
 */
public class SettingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View secondView = inflater.inflate(R.layout.setting_fragment,container,false);


        return secondView;
    }
}
