package com.mingrisoft.mrshop.activity;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.activity.base.BaseActivity;
import com.mingrisoft.mrshop.fragment.ShoppingCartFragment;

/**
 * 购物车界面
 */
public class ShoppingCartActivity extends BaseActivity {

    /**
     * 设置布局
     * @return
     */
    @Override
    protected int setLayoutID() {
        return R.layout.activity_shopping_cart; //指定所用布局文件
    }

    /**
     * 设置功能
     */
    @Override
    public void setFunction() {
        ShoppingCartFragment cartFragment = new ShoppingCartFragment(); //实例化购物车的Fragment对象
        cartFragment.setBackBtnShow(true);  //显示返回按钮
        //加载购物车界面
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_shopping_cart,cartFragment)
                .commit();
    }
}
