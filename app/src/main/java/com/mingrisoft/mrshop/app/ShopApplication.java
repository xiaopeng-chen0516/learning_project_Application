package com.mingrisoft.mrshop.app;

import android.app.Application;

import com.mingrisoft.mrshop.helper.ShareHelper;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * 作者： LYJ
 * 功能： 全局的单例模式的Application
 * 创建日期： 2017/5/3
 */

public class ShopApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化日志库
        Logger.init("SHOP").logLevel(LogLevel.FULL);
        ShareHelper.init(this);//初始化ShareHelper
    }
}
