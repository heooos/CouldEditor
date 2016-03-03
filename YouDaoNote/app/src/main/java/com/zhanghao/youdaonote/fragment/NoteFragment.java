package com.zhanghao.youdaonote.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.activity.LoginActivity;
import com.zhanghao.youdaonote.activity.NoteEditActivity;
import com.zhanghao.youdaonote.activity.NoteShowActivity;
import com.zhanghao.youdaonote.adapter.CustomAdapter;
import com.zhanghao.youdaonote.adapter.ItemBean;
import com.zhanghao.youdaonote.database.DeleteNoteFromDB;
import com.zhanghao.youdaonote.database.ReadNoteFromDB;

import java.util.List;

/**
 * Created by ZH on 2016/2/21.
 */
public class NoteFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private FloatingActionButton fabEdit;
    private ImageView contactImg;
    private CustomAdapter adapter;
    private ReadNoteFromDB readNoteFromDB;
    private List<ItemBean> list;
    private  View firstView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firstView = inflater.inflate(R.layout.note_fragment,container,false);

        init(firstView);
        initEvent();
        dataRefresh();
        return firstView;
    }


    private void initEvent() {
        contactImg.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        fabEdit.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_TOUCH_SCROLL:

                        Log.d("NoteFragment", view.getScrollY()+"");

                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void init(View firstView) {

        fabEdit = (FloatingActionButton) firstView.findViewById(R.id.fab);
        contactImg = (ImageView) firstView.findViewById(R.id.contact_img);
        listView = (ListView) firstView.findViewById(R.id.noteList);
        dataRefresh();
    }

    private void dataRefresh() {
        readNoteFromDB = new ReadNoteFromDB(getContext());
        list = readNoteFromDB.readNote();
        adapter = new CustomAdapter(getContext(),list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent editIntent = new Intent(getContext(),NoteEditActivity.class);
                editIntent.putExtra("className","NoteFragment");
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
        showIntent.putExtra("content",bean.noteContent);
        showIntent.putExtra("date",bean.noteDate);
        startActivity(showIntent);
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        showPopupWindows(position);

        return true;
    }

    private void showPopupWindows(final int itemPosition) {

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_layout,null);
        Button cancel = (Button) contentView.findViewById(R.id.popupWindows_cancel);
        Button rename = (Button) contentView.findViewById(R.id.popupWindows_rename);
        Button delete = (Button) contentView.findViewById(R.id.popupWindows_delete);

        TextView tv_showName = (TextView) contentView.findViewById(R.id.popupWindows_fileName);

        final PopupWindow popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
       // popupWindow.showAsDropDown(view, view.getMeasuredWidth() / 2, 0);
        popupWindow.showAtLocation(firstView, Gravity.BOTTOM, 0, 0);

        tv_showName.setText("文件名：" + list.get(itemPosition).noteTitle);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteNoteFromDB delete = new DeleteNoteFromDB(getContext(),list.get(itemPosition).noteDate);
                delete.delete();
                list.remove(itemPosition);
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        Log.d("NoteFragment", "onResume()");
        dataRefresh();
        super.onResume();
    }

}
