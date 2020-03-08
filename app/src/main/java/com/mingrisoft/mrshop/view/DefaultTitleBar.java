package com.mingrisoft.mrshop.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingrisoft.mrshop.R;

/**
 * 作者： LYJ
 * 功能： 默认的标题栏
 * 创建日期： 2017/5/4
 */

public class DefaultTitleBar extends FrameLayout {
    private ImageButton titleBack;
    private TextView titleName;
    private LinearLayout titleRight;
    private RelativeLayout titleGroup;

    public DefaultTitleBar(Context context) {
        super(context);
        initialize(context);//初始化
    }

    public DefaultTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);//初始化
    }

    /**
     * 初始化
     */
    private void initialize(Context context) {
        View view = View.inflate(context, R.layout.default_title_bar_, this);
        //初始化控件
        titleBack = (ImageButton) view.findViewById(R.id.title_back);
        titleName = (TextView) view.findViewById(R.id.title_txt);
        titleRight = (LinearLayout) view.findViewById(R.id.title_right);
        titleGroup = (RelativeLayout) view.findViewById(R.id.title_group);
        addListenerToView();
    }

    /**
     * 关闭当前界面
     */
    private void addListenerToView() {
        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(@StringRes int title) {
        titleName.setText(title);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        titleName.setText(title);
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public void setTitleColor(@ColorInt int color) {
        titleName.setTextColor(color);
    }

    /**
     * 设置是否显示返回键
     *
     * @param isShow
     */
    public void backBtnIsShow(boolean isShow) {
        int visibility = isShow ? VISIBLE : GONE;
        titleBack.setVisibility(visibility);
    }

    /**
     * 设置标题栏的背景颜色
     *
     * @param color
     */
    public void setTitleBarBackgroundColor(@ColorInt int color) {
        titleGroup.setBackgroundColor(color);
    }

    /**
     * 设置标题栏的背景颜色
     *
     * @param values
     */
    public void setTitleBarBackgroundAlpha(float values) {
        titleGroup.setAlpha(values);
    }

    /**
     * 设置返回键的图标
     *
     * @param res
     */
    public void setBackBtnIcon(@DrawableRes final int res) {
        titleBack.setImageResource(res);
    }

    /**
     * 添加右侧按钮
     * @param view
     */
    public void addRightView(@NonNull View view){
        if (titleRight.getChildCount() < 3 && !(view instanceof ViewGroup)){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            view.setLayoutParams(params);
            titleRight.addView(view);
        }
    }
}
