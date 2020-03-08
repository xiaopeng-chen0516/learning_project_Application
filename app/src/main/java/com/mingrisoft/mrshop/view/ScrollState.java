package com.mingrisoft.mrshop.view;

/**
 * 作者： LYJ
 * 功能： 滑动状态(习惯使用静态常量，不喜欢使用枚举)
 * 全看个人喜好
 * 创建日期： 2017/5/10
 */

public class ScrollState {
    public static final int SCROLL_RUN = 1;//跟随手势滑动
    public static final int SCROLL_FLY = 2;//惯性滑动
    public static final int SCROLL_STOP = 0;//滑动停止
}
