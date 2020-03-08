package com.mingrisoft.mrshop.adapter.recylerview.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.mingrisoft.mrshop.adapter.recylerview.listener.OnItemClickListener;
import com.mingrisoft.mrshop.adapter.recylerview.listener.OnLongClickListener;

import java.util.List;

/**
 * 作者： LYJ
 * 功能： 适配器基类
 * 创建日期： 2017/5/9
 */

public abstract class MyRecyclerViewAdapter<T ,VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>{
    protected List<T> mList;//数据源
    protected Context mContext;//上下文
    protected LayoutInflater mInflater;//布局填充器
    private OnItemClickListener listener;//item点击监听
    private OnLongClickListener longListener;//item长安监听
    private int dataLength;//数据长度
    /**
     * 构造器
     * @param context
     * @param list
     */
    public MyRecyclerViewAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
        this.dataLength = list.size();
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        onBindView(holder,position);//绑定控件
        /**
         * 设置点击事件
         */
        if (null != listener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.itemView, position);
                }
            });
        }
        /**
         * 设置长按事件
         */
        if (null != longListener){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
    }

    /**
     * 绑定控件
     * @param holder
     * @param position
     */
    protected abstract void onBindView(VH holder, int position);

    /**
     * 获取数据源数量
     * @return
     */
    @Override
    public int getItemCount() {
        return dataLength = mList.size();
    }


    /**
     * 设置item点击事件
     */
    public final void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     */
    public final void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.longListener = onLongClickListener;
    }
}
