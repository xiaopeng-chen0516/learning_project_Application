package com.mingrisoft.mrshop.entity;


/**
 * 作者： LYJ
 * 功能： 商品
 * 创建日期： 2017/5/9
 */
//public class Commodity extends Bean{
public class Commodity{
    private String id;//商品ID
    private String title;//名称
    private String description;//摘要
    private double price;//价钱
    private String imageUrls;//商品照片
    private String classification;//推荐分类
    private int type;//商品类型
    private String typeName;//商品类型名称
    private String merchant;//商家


    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("商品号：").append(id).append("\n")
                .append("名称：").append(title).append("\n")
                .append("摘要：").append(description).append("\n")
                .append("价钱：").append(price).append("\n")
                .append("商品照片：").append(imageUrls).append("\n")
                .append("推荐分类：").append(classification).append("\n")
                .append("商品类型：").append(type).append("\n")
                .append("商品类型名称：").append(typeName).append("\n")
                .append("商家：").append(merchant).append("\n");
        return stringBuilder.toString();
    }
}
