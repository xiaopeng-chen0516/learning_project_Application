package com.mingrisoft.mrshop.weight;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.weight.custom.CustomDialog;

/**
 * 作者： LYJ
 * 功能： 自定义样式弹窗
 * 创建日期： 2017/5/22
 */

public abstract class MineDialog extends CustomDialog{
    /**
     * 控件
     */
    protected TextView title;//标题
    protected Button cancel;//取消按钮
    protected Button decision;//确定按钮
    protected FrameLayout body;//增加的内容
    protected LinearLayout buttonGroup;//按钮的容器
    protected View addView;//添加的内容
    /**
     * 变量
     */
    private String titleStr;//标题内容
    private String cancelStr;//设置取消按钮的文字
    private String decisionStr;//设置确定按钮的文字
    /**
     * 构造器
     * @param context
     */
    public MineDialog(Context context) {
        super(context, true);
    }
    /**
     * 设置创建视图的展示效果
     * @param windowManager
     */
    @Override
    protected void onCreateView(WindowManager windowManager) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        setDialogWidth((int)(outMetrics.widthPixels * 0.9f));
        setDialogHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setAnim(0);//设置动画效果
        setContentView(R.layout.dialog_mine);
        initView();//初始化控件
        setFunction();//设置功能
        setView();//设置控件显示的内容
    }

    /**
     * 设置功能
     */
    protected abstract void setFunction();

    /**
     * 设置控件
     */
    private void setView() {
        if (!titleStr.isEmpty())title.setText(titleStr);
        if (!titleStr.isEmpty())cancel.setText(cancelStr);
        if (!titleStr.isEmpty())decision.setText(decisionStr);
        if (null != addView)body.addView(addView);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        title = (TextView) findViewById(R.id.default_title);
        cancel = (Button) findViewById(R.id.default_cancel);
        decision = (Button) findViewById(R.id.default_decision);
        body = (FrameLayout) findViewById(R.id.default_body);
        buttonGroup = (LinearLayout) findViewById(R.id.default_bottom_group);
        decision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wantToDo();
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 想要做的事
     */
    protected abstract void wantToDo();

    /**
     * 设置标题显示的内容
     * @param titleStr
     */
    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    /**
     * 取消按钮显示的文字
     * @param cancelStr
     */
    public void setCancelStr(String cancelStr) {
        this.cancelStr = cancelStr;
    }

    /**
     * 确定按钮显示的文字
     * @param decisionStr
     */
    public void setDecisionStr(String decisionStr) {
        this.decisionStr = decisionStr;
    }

    /**
     * 设置显示的内容
     * @param addView
     */
    public void setAddView(View addView) {
        this.addView = addView;
    }
}

