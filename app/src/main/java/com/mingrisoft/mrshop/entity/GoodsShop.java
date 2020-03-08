package com.mingrisoft.mrshop.entity;

//import com.mingrisoft.mrshop.entity.base.Bean;

import java.util.List;

/**
 * 作者： LYJ
 * 功能： 购物车商品类型
 * 创建日期： 2017/5/16
 */

public class GoodsShop{
//public class GoodsShop extends Bean {
    private String merchant;//商铺名称
    private List<GoodsCart> cartsList;//商品数据
    private boolean isCheckAll;//选择

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public List<GoodsCart> getCartsList() {
        return cartsList;
    }

    public void setCartsList(List<GoodsCart> cartsList) {
        this.cartsList = cartsList;
    }

    public boolean isCheckAll() {
        return isCheckAll;
    }

    public void setIsCheckAll(boolean isCheckAll) {
        this.isCheckAll = isCheckAll;
    }
}
