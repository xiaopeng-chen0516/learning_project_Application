package com.mingrisoft.mrshop.adapter.listener;

import android.support.annotation.Nullable;

import com.mingrisoft.mrshop.entity.GoodsCart;

import java.util.List;

/**
 * 作者： LYJ
 * 功能： 购物车的监听器
 * 创建日期： 2017/5/18
 */

public interface ShopCartListener {
    void getAllCartMessage(@Nullable List<GoodsCart> carts, Double selectPrice, int selectCount, boolean isCheckAll);
}
