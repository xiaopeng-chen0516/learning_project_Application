package com.mingrisoft.mrshop.entity;

/**
 * 作者： LYJ
 * 功能： 购物车中数据
 * 创建日期： 2017/5/15
 */

public class GoodsCart {
    private String _id;//商品ID编号
    private String title;//标题
    private double price;//价钱
    private String brand;//品牌
    private String image_url;//图片的路径
    private int count;//数量
    private String image;//图片
    private String merchant;//商家
    private CartViewState viewState;//控件状态
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public CartViewState getViewState() {
        return viewState;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setViewState(CartViewState viewState) {
        this.viewState = viewState;
    }

    @Override
    public String toString() {
        return "商品ID：【" + _id +"】商品数量：【" + count +"】";
    }
}
