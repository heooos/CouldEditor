package com.zhanghao.youdaonote.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.zhanghao.youdaonote.TApplication;
import com.zhanghao.youdaonote.activity.LoginActivity;
import com.zhanghao.youdaonote.activity.NoteEditActivity;
import com.zhanghao.youdaonote.activity.NoteShowActivity;
import com.zhanghao.youdaonote.activity.UserInfoActivity;
import com.zhanghao.youdaonote.adapter.CustomAdapter;
import com.zhanghao.youdaonote.adapter.ItemBean;
import com.zhanghao.youdaonote.database.DeleteNoteFromDB;
import com.zhanghao.youdaonote.database.QueryNoteFromDB;
import com.zhanghao.youdaonote.database.QueryNoteFromWebDB;

import java.util.List;

/**
 * Created by ZH on 2016/2/21.
 */
public class NoteFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, QueryNoteFromWebDB.IRefreshListener {

    private ListView listView;
    private FloatingActionButton fabEdit;
    private ImageView contactImg;
    public static CustomAdapter adapter;
    private QueryNoteFromDB queryNoteFromDB;
    private List<ItemBean> list;
    private  View firstView;
    private PopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firstView = inflater.inflate(R.layout.note_fragment,container,false);
        synchroDataFromWebDB();
        init(firstView);
        initEvent();
        dataRefresh();
        return firstView;
    }

    private void synchroDataFromWebDB() {
        QueryNoteFromWebDB q = new QueryNoteFromWebDB(getContext());
        q.synchroData();
        q.setInterface(this);
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
        queryNoteFromDB = new QueryNoteFromDB(getContext());
        list = queryNoteFromDB.readNote();
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
                Intent userInfoIntent = new Intent(getContext(), UserInfoActivity.class);
                if (TApplication.instance.hasCurrentUser()){
                    startActivity(userInfoIntent);
                }else {
                    startActivity(loginIntent);
                }
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
        PopupWindowListener listener=new PopupWindowListener(itemPosition);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_layout,null);
        Button cancel = (Button) contentView.findViewById(R.id.popupWindows_cancel);
        Button rename = (Button) contentView.findViewById(R.id.popupWindows_rename);
        Button delete = (Button) contentView.findViewById(R.id.popupWindows_delete);

        TextView tv_showName = (TextView) contentView.findViewById(R.id.popupWindows_fileName);

        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        popupWindow.showAtLocation(firstView, Gravity.BOTTOM, 0, 0);
        tv_showName.setText("文件名：" + list.get(itemPosition).noteTitle);
        cancel.setOnClickListener(listener);
        rename.setOnClickListener(listener);
        delete.setOnClickListener(listener);
    }


    @Override
    public void onResume() {
        Log.d("NoteFragment", "onResume()");
        dataRefresh();
        super.onResume();
    }

    @Override
    public void onRefresh() {
        dataRefresh();
    }

    /**
     * 内部类处理popupwindows视图的点击事件
     */
    class PopupWindowListener implements View.OnClickListener{

        private final int itemPosition;

        public PopupWindowListener(int itemPosition) {
            this.itemPosition=itemPosition;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.popupWindows_cancel:
                    popupWindow.dismiss();
                    break;
                case R.id.popupWindows_rename:
                    popupWindow.dismiss();
                    break;
                case R.id.popupWindows_delete:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("警告");
                    dialog.setMessage("确定删除此条信息么？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteNoteFromDB delete = new DeleteNoteFromDB(getContext());
                            delete.delete(list.get(itemPosition).noteDate);
                            list.remove(itemPosition);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                    popupWindow.dismiss();
                    break;
            }
        }
    }

}
