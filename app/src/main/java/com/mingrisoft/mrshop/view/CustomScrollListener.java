package com.mingrisoft.mrshop.view;

import android.support.v7.widget.RecyclerView;

import com.mingrisoft.mrshop.view.listener.LoadMoreListener;

/**
 * 作者： LYJ
 * 功能： 滑动监听
 * 创建日期： 2017/5/10
 */

public class CustomScrollListener extends RecyclerView.OnScrollListener{
    private int scrollY;
    /**
     * 滑动判断
     * @param recyclerView
     * @param dx
     * @param dy
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        scrollY = dy;
//        Log.i("scrollY", dy + "");
    }

    /**
     * 滑动状态的改变
     * @param recyclerView
     * @param newState
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        boolean isScrollBottom = recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange();

        switch (newState){
            case ScrollState.SCROLL_RUN:
                break;
            case ScrollState.SCROLL_FLY:
                break;
            case ScrollState.SCROLL_STOP:
                if (isScrollBottom){
                    if (null != loadMoreListener ){
                        loadMoreListener.loadMore();
                    }
                }
                break;
        }
    }

    private LoadMoreListener loadMoreListener;
    public void setLoadMoreListener(LoadMoreListener listener){
        this.loadMoreListener = listener;
    }
}
