package com.zhanghao.youdaonote.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.activity.LoginActivity;
import com.zhanghao.youdaonote.activity.NoteEditActivity;
import com.zhanghao.youdaonote.activity.NoteShowActivity;
import com.zhanghao.youdaonote.adapter.CustomAdapter;
import com.zhanghao.youdaonote.adapter.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZH on 2016/2/21.
 */
public class NoteFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private FloatingActionButton fabEdit;
    private ImageView contactImg;
    private CustomAdapter adapter;
    private List<ItemBean> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View firstView = inflater.inflate(R.layout.note_fragment,container,false);

        init(firstView);
        initEvent();
        initData();
        return firstView;
    }

    private void initData() {
        adapter = new CustomAdapter(getContext(),list);
        listView.setAdapter(adapter);
    }

    private void initEvent() {
        contactImg.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        fabEdit.setOnClickListener(this);
    }

    private void init(View firstView) {

        list = new ArrayList<>();
        list.add(new ItemBean(R.drawable.logo,"你好好好好好好好好","2016-02-28 01:48"));
        fabEdit = (FloatingActionButton) firstView.findViewById(R.id.fab);
        contactImg = (ImageView) firstView.findViewById(R.id.contact_img);
        listView = (ListView) firstView.findViewById(R.id.noteList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent editIntent = new Intent(getContext(),NoteEditActivity.class);
                startActivity(editIntent);
                break;
            case R.id.contact_img:
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemBean bean = list.get(position);
        Intent showIntent = new Intent(getContext(),NoteShowActivity.class);
        showIntent.putExtra("title",bean.noteTitle);
        startActivity(showIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        list.remove(position);
        adapter.notifyDataSetChanged();
        return false;
    }
}
