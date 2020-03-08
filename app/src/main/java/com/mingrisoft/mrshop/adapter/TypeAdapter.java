package com.mingrisoft.mrshop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.entity.ShapeType;

import java.util.List;

/**
 * 作者： LYJ
 * 功能： 类型适配器
 * 创建日期： 2017/5/24
 */

public class TypeAdapter extends BaseAdapter{
    private LayoutInflater mInflater;//布局填充器
    private List<ShapeType> mList;//数据结合

    /**
     * 构造器
     * @param mContext
     * @param mList
     */
    public TypeAdapter(Context mContext, List<ShapeType> mList) {
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);//this不用必须添加（看情景）
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_type_name,parent,false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.type_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mList.get(position).getName());
        String tag = mList.get(position).getName();
        viewHolder.name.setTag(tag);
        final boolean back = mList.get(position).isSelected();
        if (TextUtils.equals(tag, (CharSequence) viewHolder.name.getTag())){
            if (back){
                viewHolder.name.setBackgroundResource(R.drawable.type_item_gray);
            }else {
                viewHolder.name.setBackgroundResource(R.drawable.type_item);
            }
        }
        return convertView;
    }

    /**
     * 复用类
     */
    static class ViewHolder{
        TextView name;
    }

    /**
     * 设置选中的选项
     * @param position
     */
    public void setSelectItem(int position){
        for (int i = 0;i < mList.size();i++){
            if (position == i){
                mList.get(position).setSelected(true);
                continue;
            }
            mList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }
}
