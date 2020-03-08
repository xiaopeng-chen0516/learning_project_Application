package com.mingrisoft.mrshop.network;

/**
 * 作者： LYJ
 * 功能： 标记
 * 创建日期： 2017/5/3
 */

public class HttpCode {
    public static final String FAILURE = "失败";
    public static final String SUCCESS= "成功";
    public static final int ERROR = 0;//错误
    public static final int HOME = 1;//请求首页的数据
    public static final int DETAILS = 2;//请求详情的数据
    public static final int SHOP_TYPE = 3;//请求首页推荐分类的数据
    public static final int SHOP_LIST = 4;//请求分类商品的数据
    public static final int TYPE_NAME = 5;//请求分类列表的数据
    public static final int START_PAGE = 1;//起始页数
    public static final int RANDOM_PAGE = 3;//随机页数
    public static final int PAGE_ROW = 10;//数据条数
    public static final String ROW = "row";//请求参数row
    public static final String PAGE = "page";//请求参数page
    public static final int DOWN_REFRESH = 101;//刷新
    public static final int DOWN_INIT = 102;//初始化界线
    public static final int DOWN_MORE = 103;//加载更多

    public static final int LOGIN=701;  //请求登录
}