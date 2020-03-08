package com.mingrisoft.mrshop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.activity.base.TitleActivity;
import com.mingrisoft.mrshop.adapter.CommodityAdapter;
import com.mingrisoft.mrshop.adapter.recylerview.base.AddHeadAndFootAdapter;
import com.mingrisoft.mrshop.adapter.recylerview.listener.OnItemClickListener;
import com.mingrisoft.mrshop.entity.Commodity;
import com.mingrisoft.mrshop.entity.base.Result;
import com.mingrisoft.mrshop.network.HttpCode;
import com.mingrisoft.mrshop.network.HttpHelper;
import com.mingrisoft.mrshop.network.HttpUtils;
import com.mingrisoft.mrshop.utils.StaticUtils;
import com.mingrisoft.mrshop.utils.ToastUtils;
import com.mingrisoft.mrshop.view.CustomScrollListener;
import com.mingrisoft.mrshop.view.LoadMoreView;
import com.mingrisoft.mrshop.view.listener.LoadMoreListener;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品列表
 * 由于数据不够多，这里的下拉刷新和上来加只是实现个效果
 * 如果想要看具体的实现步骤，请参照首页，首页的功能是完整的
 */
public class ShopTypeActivity extends TitleActivity implements OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,LoadMoreListener {
    private RecyclerView recyclerView;//列表
    private CommodityAdapter commodityAdapter;//商品列表适配器
    private List<Commodity> commodityList;//商品列表数据
    private SwipeRefreshLayout refreshLayout;//下拉刷新
    private String typeName;//类型名称
    private AddHeadAndFootAdapter adapter;//添加头部
    private CustomScrollListener scrollListener;//滑动监听
    private boolean isFirstLoad = true;//是否为首次加载
    private LoadMoreView loadMore;//加载更多
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HttpCode.SHOP_TYPE://请求分类数据
                    Logger.json((String) msg.obj);
                    Type type = new TypeToken<Result<List<Commodity>>>() {
                    }.getType();
                    Result<List<Commodity>> result = new Gson()
                            .fromJson(String.valueOf(msg.obj), type);
                    if (TextUtils.equals(HttpCode.SUCCESS, result.getReason())) {
                        initDataFromNet(result);//初始化界面
                    }
                    break;
                case HttpCode.ERROR://请求错误
                    ToastUtils.shortToast(ShopTypeActivity.this, "连接错误！");
                    //当数据为空后可重新加载数据
                    if (null == commodityList && isFirstLoad == false) {
                        isFirstLoad = true;
                    }
                    break;
                case HttpCode.DOWN_REFRESH://刷新
                    break;
                case HttpCode.DOWN_MORE://加载更多
                    loadMore.setState(LoadMoreView.LoadState.NONE);
                    break;
            }
            if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
            return false;
        }
    });

    /**
     * 加载数据
     * @param result
     */
    private void initDataFromNet(Result<List<Commodity>> result) {
        commodityList = result.getResult();
        //创建适配器
        commodityAdapter = new CommodityAdapter(this, commodityList);
        //设置列表显示效果
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //绑定适配器
        adapter = new AddHeadAndFootAdapter(commodityAdapter);
        //绑定适配器
        //添加尾部
        loadMore = new LoadMoreView(this);
        adapter.addFootView(loadMore);
        //设置适配器
        recyclerView.setAdapter(adapter);
        //点击监听
        commodityAdapter.setOnItemClickListener(this);
    }

    /**
     * 设置界面的布局
     *
     * @return
     */
    @Override
    protected int setLayoutID() {
        return R.layout.activity_shop_type;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.shop_list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
    }

    /**
     * 设置监听
     */
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
        Intent intent = getIntent();//获取传值
        typeName = intent.getStringExtra(StaticUtils.SHOP_TYPE);
        titleBar.setTitle(typeName);//设置标题
        refreshLayout.setColorSchemeColors(Color.RED);
        refreshLayout.setProgressViewOffset(false, 0, 100);//避免提前调用无加载效果
        refreshLayout.setRefreshing(true);//加载等待
        onRefresh();//开始加载
    }

    /**
     * 下载网络数据
     */
    private void downLoadDataFromNet(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        HttpUtils.HttpPost(handler, HttpHelper.GOODS_TYPE, map, HttpCode.SHOP_TYPE);
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
    private Thread threadRefresh;//下拉刷新
    private Thread threadMore;//加载更多
    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        //加载数据
        if (isFirstLoad){
            downLoadDataFromNet(typeName);
            isFirstLoad = !isFirstLoad;
        }
        //模拟刷新
        threadRefresh = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    handler.sendEmptyMessage(HttpCode.DOWN_REFRESH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadRefresh.start();
    }

    /**
     * 界面销毁
     */
    @Override
    protected void onDestroy() {
        handler.removeCallbacks(threadRefresh);
        handler.removeCallbacks(threadMore);
        super.onDestroy();
    }

    @Override
    public void loadMore() {
        loadMore.setState(LoadMoreView.LoadState.LOAD);//显示脚布局
        //模拟加载更多
        threadMore = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    handler.sendEmptyMessage(HttpCode.DOWN_MORE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadMore.start();
    }
}
