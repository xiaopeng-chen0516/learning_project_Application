package com.mingrisoft.mrshop.fragment.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mingrisoft.mrshop.view.DefaultTitleBar;


/**
 * 作者： LYJ
 * 功能： 带有标题栏的Fragment
 * 创建日期： 2017/5/4
 */

public abstract class TitleFragment extends BaseFragment {

    //默认根布局的容器
    protected int mLayoutWay;
    protected DefaultTitleBar titleView;//标题栏
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        beforeCreateView();//创建视图之前的操作
        if (null == titleView){
            //默认的标题栏
            titleView = new DefaultTitleBar(mContext);
        }
        return createViewWithLayoutWay(inflater);
    }

    /**
     * 创建视图之前的操作
     *
     */
    protected void beforeCreateView() {
        mLayoutWay = TitleLayout.LINEAR_LAYOUT;//默认界面风格

    }

    /**
     * 根据布局方式创建视图
     */
    private View createViewWithLayoutWay(LayoutInflater inflater) {
        View rootView = null;
        View bodyView = inflater.inflate(setLayoutID(),null);
        switch (mLayoutWay) {
            case TitleLayout.LINEAR_LAYOUT://创建线性布局
                rootView = new LinearLayout(mContext);
                ((LinearLayout) rootView).setOrientation(LinearLayout.VERTICAL);
                ((LinearLayout) rootView).addView(titleView);//添加标题栏
                ((LinearLayout) rootView).addView(bodyView);//添加内容
//                LinearLayout.LayoutParams tParamsL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        DensityUtils.dp2px(mContext,TitleLayout.ACTION_BAR_SIZE));
                LinearLayout.LayoutParams tParamsL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        TitleLayout.ACTION_BAR_SIZE);
                titleView.setLayoutParams(tParamsL);
                LinearLayout.LayoutParams bParamsL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                bodyView.setLayoutParams(bParamsL);
                break;
            case TitleLayout.RELATIVE_LAYOUT://创建相对布局
                rootView = new RelativeLayout(mContext);
                ((RelativeLayout) rootView).addView(bodyView);//添加内容
                ((RelativeLayout) rootView).addView(titleView);//添加标题栏
//                RelativeLayout.LayoutParams tParamsR = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                        DensityUtils.dp2px(mContext,TitleLayout.ACTION_BAR_SIZE));
                RelativeLayout.LayoutParams tParamsR = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        TitleLayout.ACTION_BAR_SIZE);
                titleView.setLayoutParams(tParamsR);
                RelativeLayout.LayoutParams bParamsR = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                bodyView.setLayoutParams(bParamsR);
                break;
        }

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(params);
        initView(bodyView);//初始化控件
        setListener();//设置监听
        setFunction();//设置功能
        return rootView;//返回视图
    }

    /**
     * 默认隐藏返回键
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setFunction() {
        titleView.backBtnIsShow(false);
    }
}
