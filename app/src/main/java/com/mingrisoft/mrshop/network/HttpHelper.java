package com.mingrisoft.mrshop.network;

/**
 * 作者： LYJ
 * 功能： 网络帮助类
 * 创建日期： 2017/5/2
 */

public class HttpHelper {
    //域名
//    public static final String HTTP_URL = "http://192.168.1.66:8080/Shop/";
    public static final String HTTP_URL = "http://z2665o0617.qicp.vip/";
    //首页的数据
    public static final String HOME_URL = HTTP_URL + "Home";
    //商品详情
    public static final String GOODS_DETAILS = HTTP_URL + "GoodsDetails";
    //首页推荐分类
    public static final String GOODS_TYPE = HTTP_URL + "TypeShop";
    //商品分类列表
    public static final String TYPE_LIST = HTTP_URL + "TypeList";
    //商品分类数据
    public static final String SHOP_LIST = HTTP_URL + "ShopType";
    //用户登录
    public static  final String MINE_LOGIN= HTTP_URL +"Login";
}