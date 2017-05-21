package com.zhanghao.youdaonote.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhanghao.youdaonote.R;
import com.zhanghao.youdaonote.TApplication;
import com.zhanghao.youdaonote.activity.LoginActivity;
import com.zhanghao.youdaonote.activity.NoteEditActivity;
import com.zhanghao.youdaonote.activity.NoteShowActivity;
import com.zhanghao.youdaonote.activity.UserInfoActivity;
import com.zhanghao.youdaonote.adapter.CustomAdapter;
import com.zhanghao.youdaonote.adapter.ItemBean;
import com.zhanghao.youdaonote.constants.Conf;
import com.zhanghao.youdaonote.database.AddNoteToWebDB;
import com.zhanghao.youdaonote.database.DeleteNoteFromDB;
import com.zhanghao.youdaonote.database.QueryNoteFromDB;
import com.zhanghao.youdaonote.database.QueryNoteFromWebDB;
import com.zhanghao.youdaonote.database.UpdateNoteToDB;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 笔记列表显示界面。
 * Created by ZH on 2016/2/21.
 */
public class NoteFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, QueryNoteFromWebDB.IRefreshListener {

    private ListView listView;
    private FloatingActionButton fabEdit;
    private ImageView contactImg, reload;
    public static CustomAdapter adapter;
    private QueryNoteFromDB queryNoteFromDB;
    private List<ItemBean> list;
    private View firstView;
    private PopupWindow popupWindow;
    private Intent loginIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firstView = inflater.inflate(R.layout.note_fragment, container, false);
        loginIntent = new Intent(getContext(), LoginActivity.class);
        initView(firstView);
        initEvent();
        if (TApplication.instance.hasCurrentUser()) {
            synchroDataFromWebDB();
        }
        dataRefresh();
        return firstView;
    }

    private void initView(View firstView) {

        fabEdit = (FloatingActionButton) firstView.findViewById(R.id.fab);
        contactImg = (ImageView) firstView.findViewById(R.id.contact_img);
        reload = (ImageView) firstView.findViewById(R.id.reload);
        listView = (ListView) firstView.findViewById(R.id.noteList);

    }

    private void initEvent() {
        contactImg.setOnClickListener(this);
        reload.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        fabEdit.setOnClickListener(this);
    }

    /**
     * 同步网络端数据
     */
    private void synchroDataFromWebDB() {
        QueryNoteFromWebDB q = new QueryNoteFromWebDB(getContext());
        q.synchroData();
        q.setInterface(this);
    }

    /**
     * 数据源刷新方法
     */
    private void dataRefresh() {
        queryNoteFromDB = new QueryNoteFromDB(getContext());
        list = queryNoteFromDB.readNote();
        adapter = new CustomAdapter(getContext(), list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent editIntent = new Intent(getContext(), NoteEditActivity.class);
                editIntent.putExtra("className", "NoteFragment");
                startActivity(editIntent);
                break;
            case R.id.contact_img:
                Intent userInfoIntent = new Intent(getContext(), UserInfoActivity.class);
                if (TApplication.instance.hasCurrentUser()) {
                    startActivity(userInfoIntent);
                } else {
                    startActivity(loginIntent);
                }

                break;
            case R.id.reload:
                if (TApplication.instance.hasCurrentUser()) {
                    synchroNoReloadData();
                } else {
                    startActivity(loginIntent);
                }
                break;
        }
    }

    /**
     * 查找数据库中未上传的数据，将其上传至服务端。
     */
    private void synchroNoReloadData() {

        final UpdateNoteToDB updateNoteToDB = new UpdateNoteToDB(getContext());
        queryNoteFromDB = new QueryNoteFromDB(getContext());

        final Cursor cursor0 = queryNoteFromDB.getNoReloadData(Conf.STATE_NOSYNCHRONIZATION);
        QueryNoteFromWebDB queryNoteFromWebDB = new QueryNoteFromWebDB(getContext());

        BmobUser user = BmobUser.getCurrentUser(getContext());
        final String userName = user.getUsername();
        //同步笔记
        while (cursor0 != null && cursor0.moveToNext()) {

            ContentValues values = new ContentValues();
            values.put("isReload", Conf.STATE_SYNCHRONIZATION);
            values.put("ID", userName);
            updateNoteToDB.updateForSynchronization(values, cursor0.getString(cursor0.getColumnIndex("date")));
            final String title = cursor0.getString(cursor0.getColumnIndex("title"));
            final String content = cursor0.getString(cursor0.getColumnIndex("content"));
            final String date = cursor0.getString(cursor0.getColumnIndex("date"));
            Pattern p = Pattern.compile("((?:\\/[\\w\\.\\-]+)+)");
            final Matcher m = p.matcher(content);
            if (m.find()) {
                String path = m.group(1);
                Log.d("这是一个Log", path);
                final BmobFile bmobFile = new BmobFile(new File(path));
                bmobFile.uploadblock(getContext(), new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        String url = bmobFile.getFileUrl(getContext());
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("webUri", url);
                        bundle.putString("title", title);
                        bundle.putString("content", content);
                        bundle.putString("date", date);
                        bundle.putString("userName", userName);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        Log.d("成功后的url", url);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getContext(), "上传失败"+ i, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(Integer value) {
                        Log.d("上传进度", value.toString());
                        super.onProgress(value);
                    }
                });
            }

//            new AddNoteToWebDB(getContext(),cursor0.getString(cursor0.getColumnIndex("title")),cursor0.getString(cursor0.getColumnIndex("content")),cursor0.getString(cursor0.getColumnIndex("date")),1,userName);
        }
        cursor0.close();

        Cursor cursor3 = queryNoteFromDB.getNoReloadData(Conf.STATE_EDIT);
        while (cursor3 != null && cursor3.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put("isReload", Conf.STATE_SYNCHRONIZATION);
            updateNoteToDB.updateForSynchronization(values, cursor3.getString(cursor3.getColumnIndex("date")));
            queryNoteFromWebDB.QueryNoteAndUpdate(cursor3.getString(cursor3.getColumnIndex("objectId")), cursor3.getString(cursor3.getColumnIndex("title")), cursor3.getString(cursor3.getColumnIndex("content")));
        }
        cursor3.close();

        dataRefresh();
        Toast.makeText(getContext(), "同步完成", Toast.LENGTH_SHORT).show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("获取到的url", msg.getData().getString("webUri"));
            new UpdateNoteToDB(getContext()).update(msg.getData().getString("webUri"), msg.getData().getString("title"), msg.getData().getString("content"), msg.getData().getString("date"), 1);
            new AddNoteToWebDB(getContext(), msg.getData().getString("webUri"), msg.getData().getString("title"), msg.getData().getString("content"), msg.getData().getString("date"), 1, msg.getData().getString("userName"));
            super.handleMessage(msg);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemBean bean = list.get(position);
        Intent showIntent = new Intent(getContext(), NoteShowActivity.class);
        showIntent.putExtra("title", bean.noteTitle);
        showIntent.putExtra("content", bean.noteContent);
        showIntent.putExtra("date", bean.noteDate);
        // TODO: 2017/2/10 将webUrl传到下一页面
        showIntent.putExtra("webUri",bean.webUri);
        startActivity(showIntent);
    }


    /**
     * 长按弹出系统提示框
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showPopupWindows(position);
        return true;
    }

    private void showPopupWindows(final int itemPosition) {
        PopupWindowListener listener = new PopupWindowListener(itemPosition);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_layout, null);
        Button cancel = (Button) contentView.findViewById(R.id.popupWindows_cancel);
        Button delete = (Button) contentView.findViewById(R.id.popupWindows_delete);

        TextView tv_showName = (TextView) contentView.findViewById(R.id.popupWindows_fileName);

        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        popupWindow.showAtLocation(firstView, Gravity.BOTTOM, 0, 0);
        tv_showName.setText("文件名：" + list.get(itemPosition).noteTitle);
        cancel.setOnClickListener(listener);
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
    class PopupWindowListener implements View.OnClickListener {

        private final int itemPosition;

        public PopupWindowListener(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.popupWindows_cancel:
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
