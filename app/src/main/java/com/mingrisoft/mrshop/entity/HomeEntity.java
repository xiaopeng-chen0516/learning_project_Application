package com.mingrisoft.mrshop.entity;

import java.util.List;

/**
 * 作者： LYJ
 * 功能： 首页的数据
 * 创建日期： 2017/5/9
 */

public class HomeEntity {
    private String[] bannerUrls;//banner图片数据
    private String[] typeNames;//分类名称
    private List<Commodity> commoditys;//商品列表

    public String[] getBannerUrls() {
        return bannerUrls;
    }

    public void setBannerUrls(String[] bannerUrls) {
        this.bannerUrls = bannerUrls;
    }

    public String[] getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(String[] typeNames) {
        this.typeNames = typeNames;
    }

    public List<Commodity> getCommoditys() {
        return commoditys;
    }

    public void setCommoditys(List<Commodity> commoditys) {
        this.commoditys = commoditys;
    }
}
