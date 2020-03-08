package com.mingrisoft.mrshop.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.network.HttpHelper;
import com.mingrisoft.mrshop.utils.DownLoadImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： LYJ
 * 功能： 展示图片
 * 创建日期： 2017/5/11
 */

public class ImageAdapter extends PagerAdapter {

    private List<View> views;
    private String[] imageUrls;
    private Context context;
    public ImageAdapter(String[] urls, Context context) {
        this.imageUrls = urls;
        this.context = context;
        views = new ArrayList<>();
        for (int i = 0;i < imageUrls.length;i++){
            View view = LayoutInflater.from(context).inflate(R.layout.item_image,null);
            views.add(view);
        }

    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_picture);
        DownLoadImageUtils.loadToImage(context,//下载图片
                HttpHelper.HTTP_URL + imageUrls[position],imageView);
        container.addView(view);//加载视图
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
