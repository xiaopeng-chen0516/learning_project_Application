package com.mingrisoft.mrshop.entity;

/**
 * 作者： LYJ
 * 功能： 选中状态和内容
 * 创建日期： 2017/5/25
 */

public class ShapeType {
    private String name;//名称
    private boolean isSelected;//是否选中

    public ShapeType(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
