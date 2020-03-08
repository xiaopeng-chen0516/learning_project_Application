package com.mingrisoft.mrshop.fragment;

import android.view.View;
import android.widget.TextView;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.fragment.base.BaseFragment;
import com.mingrisoft.mrshop.weight.LoginDialog;

/**
 * 作者： LYJ
 * 功能： 个人中心
 * 创建日期： 2017/5/3
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private TextView login;//登录/注册文本框
    private LoginDialog loginDialog;//添加弹窗
    @Override
    protected int setLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        login = (TextView) view.findViewById(R.id.custom_login);
        loginDialog = new LoginDialog(view.getContext());

    }
    /**
     * 设置监听
     */
    @Override
    public void setListener() {
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_login://登录注册文本框
                loginDialog.show();
                break;
            case R.id.cart_delete://删除商品
                break;
        }
     }
}
