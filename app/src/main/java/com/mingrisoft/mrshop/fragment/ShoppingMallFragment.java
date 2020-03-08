package com.mingrisoft.mrshop.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mingrisoft.mrshop.activity.GoodsDetailsActivity;
import com.mingrisoft.mrshop.activity.ShopTypeActivity;
import com.mingrisoft.mrshop.adapter.CommodityAdapter;
import com.mingrisoft.mrshop.adapter.recylerview.base.AddHeadAndFootAdapter;
import com.mingrisoft.mrshop.adapter.recylerview.listener.OnItemClickListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.entity.Commodity;
import com.mingrisoft.mrshop.entity.HomeEntity;
import com.mingrisoft.mrshop.entity.base.Result;
import com.mingrisoft.mrshop.fragment.base.TitleFragment;
import com.mingrisoft.mrshop.fragment.base.TitleLayout;
import com.mingrisoft.mrshop.network.HttpCode;
import com.mingrisoft.mrshop.network.HttpHelper;
import com.mingrisoft.mrshop.network.HttpUtils;
import com.mingrisoft.mrshop.utils.GetViewTextUtils;
import com.mingrisoft.mrshop.utils.GlideImageLoader;
import com.mingrisoft.mrshop.utils.StaticUtils;
import com.mingrisoft.mrshop.utils.ToastUtils;
import com.mingrisoft.mrshop.view.CustomScrollListener;
import com.mingrisoft.mrshop.view.LoadMoreView;
import com.mingrisoft.mrshop.view.listener.LoadMoreListener;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 作者： LYJ
 * 功能： 商城
 * 创建日期： 2017/5/3
 */

public class ShoppingMallFragment extends TitleFragment implements
        SwipeRefreshLayout.OnRefreshListener, LoadMoreListener,
        OnItemClickListener, View.OnClickListener {
    private RecyclerView recyclerView;//列表
    private SwipeRefreshLayout refreshLayout;//下拉刷新
    private AddHeadAndFootAdapter adapter;//添加头部
    private CommodityAdapter commodityAdapter;//商品列表适配器
    private List<Commodity> commodityList;//商品列表数据
    private LoadMoreView loadMore;//加载更多
    private int page;//请求页数
    private boolean isFirstLoad = true;//是否为首次加载
    private Random random = new Random();//随机数生成器
    private int downLoadState;//加载网络数据的状态
    private CustomScrollListener scrollListener;//滑动监听
    private Handler handler = new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HttpCode.HOME://获取首页的请求结果
//                    Logger.json(String.valueOf(msg.obj));
                    Type type = new TypeToken<Result<HomeEntity>>() {
                    }.getType();
                    Result<HomeEntity> result = new Gson()
                            .fromJson(String.valueOf(msg.obj), type);
                    //判断返回数据
                    if (TextUtils.equals(HttpCode.SUCCESS, result.getReason())) {
                        switch (downLoadState) {
                            case HttpCode.DOWN_INIT://初始化
                                initDataFromNet(result);//初始化界面
                                break;
                            case HttpCode.DOWN_REFRESH://刷新
                                refreshDataFromNet(result);//刷新数据
                                break;
                            case HttpCode.DOWN_MORE://加载更多
                                moreDataFromNet(result);//加载更多
                                break;
                        }
                    } else if (TextUtils.equals(HttpCode.FAILURE, result.getReason())) {
                        if (downLoadState == HttpCode.DOWN_MORE) {
                            loadMore.setState(LoadMoreView.LoadState.NONE);
                            page--;//恢复加载之前
                        }
                    }
                    break;
                case HttpCode.ERROR:
                    ToastUtils.shortToast(mContext, "连接错误！");
                    //当数据为空后可重新加载数据
                    if (null == commodityList && isFirstLoad == false) {
                        isFirstLoad = true;
                    }
                    break;
            }
            if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
            return false;
        }
    });
    private List<String> banners;//广告图片
    private Banner banner;//轮播

    private RadioButton type1, type2, type3, type4;//类别选择按钮
    /**
     * 设置广告位图片的接口数据
     */
    private void setBannerImageUrlList(String[] urls) {
        banners = new ArrayList<>();//初始化数据集合
        for (int i = 0, length = urls.length; i < length; i++) {
            banners.add(HttpHelper.HTTP_URL + urls[i]);//添加数据
        }
    }

    /**
     * 设置界面的布局文件
     *
     * @return
     */
    @Override
    protected int setLayoutID() {
        return R.layout.fragment_shoppingmall;
    }

    /**
     * 设置界面的显示效果
     */
    @Override
    protected void beforeCreateView() {
        mLayoutWay = TitleLayout.LINEAR_LAYOUT;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.shop_list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
    }

    @Override
    public void setListener() {
        scrollListener = new CustomScrollListener();
        scrollListener.setLoadMoreListener(this);
        recyclerView.addOnScrollListener(scrollListener);
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * 设置功能
     */
    @Override
    public void setFunction() {
        super.setFunction();
        //设置标题
        titleView.setTitle(R.string.mr_shoppingmall);
        page = HttpCode.START_PAGE;//初始化页数
        recyclerView.setHasFixedSize(true);
        refreshLayout.setColorSchemeColors(Color.RED);
        refreshLayout.setProgressViewOffset(false, 0, 100);//避免提前调用无加载效果
        refreshLayout.setRefreshing(true);//加载等待
        onRefresh();//开始加载
    }

    /**
     * 恢复为活动状态时
     */
    @Override
    public void onResume() {
        super.onResume();
        if (null != banner) {
            banner.startAutoPlay();//开启轮播
        }
    }

    /**
     * 界面失去焦点时
     */
    @Override
    public void onPause() {
        super.onPause();
        if (null != banner) {
            banner.stopAutoPlay();//关闭轮播
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if (!isFirstLoad) {
            //生成随机数用于刷新界面的数据
            int newPage;
            while (true) {
                //保证每次刷新的页数都跟之前的不一样
                newPage = random.nextInt(HttpCode.RANDOM_PAGE) + HttpCode.START_PAGE;
                if (page != newPage) {//如果不一样就重新加载数据
                    //赋值新的页数，跳出循环
                    page = newPage;
                    break;
                }
            }
            downLoadState = HttpCode.DOWN_REFRESH;//刷新
            loadDataFromNet();//下载网络数据
            return;
        }
        downLoadState = HttpCode.DOWN_INIT;//初始化界面
        loadDataFromNet();//下载网络数据
        isFirstLoad = !isFirstLoad;//第一次加载结束
    }

    /**
     * 下载网络数据
     */
    private void loadDataFromNet() {
        Map<String, Object> map = new HashMap<>();
        map.put(HttpCode.PAGE, page);//数据的页数
        map.put(HttpCode.ROW, HttpCode.PAGE_ROW);//数据的条数
        HttpUtils.HttpPost(handler, HttpHelper.HOME_URL, map, HttpCode.HOME);//下载商城数据
    }

    /**
     * 初始化界面的数据
     *
     * @param result
     */
    private void initDataFromNet(Result<HomeEntity> result) {
        commodityList = result.getResult().getCommoditys();
        //创建适配器
        commodityAdapter = new CommodityAdapter(mContext, commodityList);
        //设置列表显示效果
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        //绑定适配器
        adapter = new AddHeadAndFootAdapter(commodityAdapter);
        //设置头布局
        View headView = LayoutInflater.from(mContext)
                .inflate(R.layout.headview_commodity, recyclerView, false);
        //添加头部
        adapter.addHeaderView(headView);
        //添加尾部
        loadMore = new LoadMoreView(mContext);
        adapter.addFootView(loadMore);
        //设置适配器
        recyclerView.setAdapter(adapter);
        commodityAdapter.setOnItemClickListener(this);
        /***************************广告轮播*****************************/
        setBannerImageUrlList(result.getResult().getBannerUrls());//设置广告位数据
        //设置广告轮播
        banner = (Banner) headView.findViewById(R.id.commodity_banner);
                //加载轮播图
        banner.setImages(banners)
                .setImageLoader(new GlideImageLoader())
                .start();
        /***************************推荐分类*****************************/
        //初始化分类按钮
        type1 = (RadioButton) headView.findViewById(R.id.shop_type1);
        type2 = (RadioButton) headView.findViewById(R.id.shop_type2);
        type3 = (RadioButton) headView.findViewById(R.id.shop_type3);
        type4 = (RadioButton) headView.findViewById(R.id.shop_type4);
        //设置按钮的点击监听
        type1.setOnClickListener(this);
        type2.setOnClickListener(this);
        type3.setOnClickListener(this);
        type4.setOnClickListener(this);
        //设置显示的文字(这张图片也应该是下载下来的，这里知道就行)
        String type[] = result.getResult().getTypeNames();//获取类型数据
        type1.setText(type[0]);
        type2.setText(type[1]);
        type3.setText(type[2]);
        type4.setText(type[3]);

    }

    /**
     * 刷新界面的数据
     *
     * @param result
     */
    private void refreshDataFromNet(Result<HomeEntity> result) {
        commodityList.clear();//清空数据
        commodityList.addAll(result.getResult().getCommoditys());//添加新的数据
        adapter.notifyDataSetChanged();//通知适配器更新数据
    }


    /**
     * 加载更多
     *
     * @param result
     */
    private void moreDataFromNet(final Result<HomeEntity> result) {
        List<Commodity> newList = result.getResult().getCommoditys();
        int newItemSize = newList.size();
        int nowListSize = commodityList.size();
        commodityList.addAll(newList);//添加数据
        commodityAdapter.notifyItemRangeInserted(nowListSize, newItemSize);
        loadMore.setVisibility(View.GONE);//隐藏脚布局
    }

    /**
     * 加载更多
     */
    @Override
    public void loadMore() {
        loadMore.setState(LoadMoreView.LoadState.LOAD);//显示脚布局
        downLoadState = HttpCode.DOWN_MORE;//加载更多
        page++;//获取下一页的数据
        Log.w("页数", page + "");
        loadDataFromNet();//获取网络数据
    }

    /**
     * 点击事件
     *
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        //创建跳转界面的意图
        Intent startTo = activity(GoodsDetailsActivity.class);
        //传递序列化的对象
        startTo.putExtra(StaticUtils.SHOPID, commodityList.get(position).getId())
                .putExtra(StaticUtils.SHOPIMAGE, commodityList.get(position).getImageUrls());
        //跳转界面
        startActivity(startTo);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_type1://服饰内衣
            case R.id.shop_type2://精品鞋靴
            case R.id.shop_type3://美妆个护
            case R.id.shop_type4://食品饮料
                String showType = GetViewTextUtils.getTextFromView(((CompoundButton) v));
                Intent intent = activity(ShopTypeActivity.class);
                intent.putExtra(StaticUtils.SHOP_TYPE,showType);
                startActivity(intent);//跳转界面
                break;
        }
    }
}



