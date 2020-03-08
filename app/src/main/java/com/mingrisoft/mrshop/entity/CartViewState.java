package com.mingrisoft.mrshop.entity;

/**
 * 作者： LYJ
 * 功能： 购物车中控件的状态
 * 创建日期： 2017/5/22
 */

public class CartViewState {
    private boolean addViewState;//添加数量控件的状态
    private boolean cutViewState;//减少数量控件的状态
    private boolean checkViewState;//选择控件的状态

    public CartViewState() {
    }

    public CartViewState(boolean addViewState, boolean cutViewState, boolean checkViewState) {
        this.addViewState = addViewState;
        this.cutViewState = cutViewState;
        this.checkViewState = checkViewState;
    }

    public boolean isAddViewState() {
        return addViewState;
    }

    public void setAddViewState(boolean addViewState) {
        this.addViewState = addViewState;
    }

    public boolean isCutViewState() {
        return cutViewState;
    }

    public void setCutViewState(boolean cutViewState) {
        this.cutViewState = cutViewState;
    }

    public boolean isCheckViewState() {
        return checkViewState;
    }

    public void setCheckViewState(boolean checkViewState) {
        this.checkViewState = checkViewState;
    }

    @Override
    public String toString() {
        return "checkViewState(控件选中状态)：" + checkViewState;
    }
}
