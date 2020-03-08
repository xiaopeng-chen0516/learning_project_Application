package com.mingrisoft.mrshop.fragment.base;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingrisoft.mrshop.listener.FragmentUIListener;


/**
 * 作者： LYJ
 * 功能： fragment基类
 * 创建日期： 2017/5/2
 */

public abstract class BaseFragment extends MPermissionsFragment implements FragmentUIListener {
    protected Context mContext;     //上下文对象
    protected Activity mActivity;   //Activity对象

    /**
     * 当视图被加载到Activity中时候调用
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = (Activity) context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(setLayoutID(),container,false);
        initView(view);//初始化控件
        setListener();//设置监听
        setFunction();//设置功能
        return view;
    }

    /**
     * 设置布局
     * @return
     */
    @LayoutRes
    protected abstract int setLayoutID();

    /**
     * 跳转界面
     * @param clazz
     * @return
     */
    protected final <T extends Activity>Intent activity(Class<T> clazz){
        return new Intent(mActivity,clazz);
    }
    //设置初始化控件的方法
    @Override
    public void initView(View view) {

    }
    //设置监听的方法
    @Override
    public void setListener() {

    }
    //设置功能的方法
    @Override
    public void setFunction() {

    }

}
