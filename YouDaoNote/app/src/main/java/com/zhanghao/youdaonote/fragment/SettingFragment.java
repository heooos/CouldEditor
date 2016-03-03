package com.zhanghao.youdaonote.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.activity.LoginActivity;

/**
 * Created by ZH on 2016/2/21.
 */
public class SettingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View secondView = inflater.inflate(R.layout.setting_fragment,container,false);
        RelativeLayout layout = (RelativeLayout) secondView.findViewById(R.id.setting_login);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/2/29
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        return secondView;
    }
}
