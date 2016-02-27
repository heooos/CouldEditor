package com.zhanghao.youdaonote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhanghao.youdaonote.R;

import java.util.List;

/**
 * Created by ZH on 2016/2/26.
 */
public class CustomAdapter extends BaseAdapter {

    private List<ItemBean> itemBeanList;
    private LayoutInflater inflater;

    public CustomAdapter(Context context,List<ItemBean> list){

        itemBeanList =list;
        inflater = LayoutInflater.from(context);

    }



    @Override
    public int getCount() {
        return itemBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_cell_layout,null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.logo);
            viewHolder.title = (TextView) convertView.findViewById(R.id.listView_title);
            viewHolder.date = (TextView) convertView.findViewById(R.id.listView_data);
            convertView.setTag(viewHolder); //建立convertView与ViewHolder的连接
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ItemBean bean = itemBeanList.get(position);
        viewHolder.image.setImageResource(bean.icon);
        viewHolder.title.setText(bean.noteTitle);
        viewHolder.date.setText(bean.noteDate);
        return convertView;

    }

    /**
     * 视图缓存
     */
    class ViewHolder{
        public ImageView image;
        public TextView title;
        public TextView date;
    }
}
