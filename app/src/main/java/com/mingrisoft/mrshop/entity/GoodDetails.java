package com.mingrisoft.mrshop.entity;

//import com.mingrisoft.mrshop.entity.base.Bean;

/**
 * 作者： LYJ
 * 功能： 商品详情界面
 * 创建日期： 2017/5/11
 */
public class GoodDetails{
//public class GoodDetails extends Bean{
    private String id;//商品ID
    private String title;//名称
    private String description;//摘要
    private double nowPrice;//价钱
    private double oldPrice;//原价
    private String imageUrls;//商品照片
    private String merchant;//商家
    private String prompt;//提示
    private String introduction;//商品的说明
    private String brand;//品牌
    private int comment;//评论
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public double getNowPrice() {
        return nowPrice;
    }
    public void setNowPrice(double nowPrice) {
        this.nowPrice = nowPrice;
    }
    public double getOldPrice() {
        return oldPrice;
    }
    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }
    public String getImageUrls() {
        return imageUrls;
    }
    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }
    public String getMerchant() {
        return merchant;
    }
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }
    public String getPrompt() {
        return prompt;
    }
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public int getComment() {
        return comment;
    }
    public void setComment(int comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("商品ID：").append(id).append("\n")
                .append("名称：").append(title).append("\n")
                .append("摘要：").append(description).append("\n")
                .append("价钱：").append(nowPrice).append("\n")
                .append("原价：").append(oldPrice).append("\n")
                .append("商品照片：").append(imageUrls).append("\n")
                .append("商家：").append(merchant).append("\n")
                .append("提示：").append(prompt).append("\n")
                .append("商品的说明：").append(introduction).append("\n")
                .append("品牌：").append(brand).append("\n")
                .append("评论：").append(comment).append("\n");
        return stringBuilder.toString();
    }
}
