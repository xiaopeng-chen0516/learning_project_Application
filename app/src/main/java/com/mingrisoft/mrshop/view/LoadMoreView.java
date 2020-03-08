package com.mingrisoft.mrshop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mingrisoft.mrshop.R;

/**
 * 作者： LYJ
 * 功能： 加载更多布局效果
 * 创建日期： 2017/5/10
 */

public class LoadMoreView extends FrameLayout{
    private LinearLayout load,none;
    private LoadState state = LoadState.LOAD;
    public LoadMoreView(Context context) {
        super(context);
        initView(context);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_load_more,this);
        load = (LinearLayout) view.findViewById(R.id.load_more_load);//加载更多
        none = (LinearLayout) view.findViewById(R.id.load_more_none);//无
        changeLoadShow();
    }
    public enum LoadState{
        LOAD,NONE
    }

    public void setState(LoadState loadState){
        this.state = loadState;
        setVisibility(VISIBLE);
        changeLoadShow();
    }

    private void changeLoadShow(){
        switch (state){
            case LOAD:
                load.setVisibility(VISIBLE);
                none.setVisibility(GONE);
                break;
            case NONE:
                load.setVisibility(GONE);
                none.setVisibility(VISIBLE);
                break;
        }
    }
}
