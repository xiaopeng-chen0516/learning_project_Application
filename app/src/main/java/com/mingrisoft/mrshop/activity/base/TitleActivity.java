package com.mingrisoft.mrshop.activity.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mingrisoft.mrshop.view.DefaultTitleBar;

public abstract class TitleActivity extends BaseActivity {
    protected DefaultTitleBar titleBar;//标题栏
    private View body;//界面主体内容
    private LinearLayout mParentView;//容器

    @Override
    protected void onCreateCustom() {
        initParentView();//初始化界面的布局
        initView();//初始化控件
        setListener();//设置监听
        setFunction();//设置功能
    }

    /**
     * 初始化根布局
     */
    public void initParentView(){
        mParentView = new LinearLayout(this);//创建一个线性布局的对象
        mParentView.setOrientation(LinearLayout.VERTICAL);//设置内容的排列方式
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);//设置布局大小
        mParentView.setLayoutParams(params);//设置布局大小
        addTitleBar();//添加标题栏
        addBody();//添加自定义布局
        setContentView(mParentView);
    }

    /**
     * 添加设计的界面主体内容
     */
    private void addBody() {
        body = getLayoutInflater().inflate(setLayoutID(),mParentView,false);
        mParentView.addView(body);
    }
    /**
     * 添加标题栏
     */
    private void addTitleBar(){
        if (null == titleBar){
            titleBar = new DefaultTitleBar(this);
        }
        mParentView.addView(titleBar);//将标题栏添加到窗口中
    }
}
