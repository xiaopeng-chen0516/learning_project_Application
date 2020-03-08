package com.mingrisoft.mrshop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mingrisoft.mrshop.listener.ImageDownLoadListener;

/**
 * 作者： LYJ
 * 功能： 加载图片工具类
 * 创建日期： 2017/5/11
 */

public class DownLoadImageUtils {
    /**
     * 加载图片到指定的控件
     *
     * @param context
     * @param url
     * @param view
     */
    public static void loadToImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).crossFade().into(view);
    }

    /**
     * 下载图片
     *
     * @param context
     * @param url
     * @return
     */
    public static void loadBitmap(Context context, String url,final ImageDownLoadListener imageDownLoadListener) {
        Glide.with(context).load(url).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                imageDownLoadListener.getDownLoadBitmap(resource);
            }
        });
    }
}
