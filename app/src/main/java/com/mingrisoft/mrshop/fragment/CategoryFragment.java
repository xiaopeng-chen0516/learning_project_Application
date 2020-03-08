package com.mingrisoft.mrshop.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.activity.GoodsDetailsActivity;
import com.mingrisoft.mrshop.adapter.CommodityAdapter;
import com.mingrisoft.mrshop.adapter.TypeAdapter;
import com.mingrisoft.mrshop.adapter.recylerview.listener.OnItemClickListener;
import com.mingrisoft.mrshop.entity.Commodity;
import com.mingrisoft.mrshop.entity.ShapeType;
import com.mingrisoft.mrshop.entity.base.Result;
import com.mingrisoft.mrshop.fragment.base.TitleFragment;
import com.mingrisoft.mrshop.network.HttpCode;
import com.mingrisoft.mrshop.network.HttpHelper;
import com.mingrisoft.mrshop.network.HttpUtils;
import com.mingrisoft.mrshop.utils.StaticUtils;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： LYJ
 * 功能： 分类(主要就是双列表进行联动)
 * 创建日期： 2017/5/3
 */

public class CategoryFragment extends TitleFragment implements
        AdapterView.OnItemClickListener,OnItemClickListener{
    private ListView typeList;//类型列表
    private RecyclerView shopList;//商品列表
    private List<String> typeNames;//类型名称集合
    private TypeAdapter nameAdapter;//类型适配器
    private CommodityAdapter commodityAdapter;//商品列表适配器
    private List<Commodity> commodityList;//商品列表数据
    private List<ShapeType> selectList;//选择类型集合
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HttpCode.TYPE_NAME://类型名称
                    //返回的JSON
                    String resultStr = String.valueOf(msg.obj);
//                    Logger.json(result);
                    Type type = new TypeToken<Result<List<String>>>() {
                    }.getType();
                    Result<List<String>> result = new Gson().fromJson(resultStr,type);
                    if (TextUtils.equals(HttpCode.SUCCESS, result.getReason())) {
                        typeNames = result.getResult();
                        selectList = new ArrayList<>();
                        for (int i = 0;i < typeNames.size();i++){
                            selectList.add(new ShapeType(typeNames.get(i),false));
                        }
                        nameAdapter = new TypeAdapter(mContext,selectList);
                        typeList.setAdapter(nameAdapter);
                        typeList.post(new Runnable() {
                            @Override
                            public void run() {
                                View view = typeList.getChildAt(0);
                                typeList.performItemClick(view, 0, typeList.getItemIdAtPosition(0));
                            }
                        });
                    }
                    break;
                case HttpCode.SHOP_LIST://类型商品数据集合
                    String resultShop = String.valueOf(msg.obj);
                    Logger.json(resultShop);
                    Type typeShop = new TypeToken<Result<List<Commodity>>>() {
                    }.getType();
                    Result<List<Commodity>> resultData = new Gson()
                            .fromJson(resultShop, typeShop);
                    if (TextUtils.equals(HttpCode.SUCCESS, resultData.getReason())) {
                        initDataFromNet(resultData);//初始化界面
                    }
                    break;
            }
            return false;
        }
    });

    /**
     * 加载数据
     * @param resultData
     */
    private void initDataFromNet(Result<List<Commodity>> resultData) {
        if (null == commodityList){
            commodityList = resultData.getResult();
            //创建适配器
            commodityAdapter = new CommodityAdapter(mContext, commodityList,true);
            //设置列表显示效果
            shopList.setLayoutManager(new GridLayoutManager(mContext, 2));
            //设置适配器
            shopList.setAdapter(commodityAdapter);
            //点击监听
            commodityAdapter.setOnItemClickListener(this);
        }else {
            commodityList.clear();
            commodityList.addAll(resultData.getResult());
            commodityAdapter.notifyDataSetChanged();
            shopList.post(new Runnable() {
                @Override
                public void run() {
                    shopList.scrollToPosition(0);
                }
            });
        }

    }

    /**
     * 设置布局
     */
    @Override
    protected int setLayoutID() {
        return R.layout.fragment_category;
    }

    /**
     * 初始化控件
     * @param view
     */
    @Override
    public void initView(View view) {
        typeList = (ListView) view.findViewById(R.id.type_list);
        shopList = (RecyclerView) view.findViewById(R.id.shop_list);
    }

    @Override
    public void setListener() {
        typeList.setOnItemClickListener(this);
    }

    /**
     * 设置功能(此处为了方便数据，竟没有对一些常用资源进行抽取)
     */
    @Override
    public void setFunction() {
        super.setFunction();
        //设置标题栏
        titleView.setTitle("商品分类");
        HttpUtils.HttpGet(handler, HttpHelper.TYPE_LIST, HttpCode.TYPE_NAME);
    }

    /**
     * 点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        nameAdapter.setSelectItem(position);
        downLoadDataFromNet(typeNames.get(position));
    }
    /**
     * 下载网络数据
     */
    private void downLoadDataFromNet(String shopType) {
        Map<String, Object> map = new HashMap<>();
        map.put("shop", shopType);
        HttpUtils.HttpPost(handler, HttpHelper.SHOP_LIST, map, HttpCode.SHOP_LIST);
    }

    /**
     * 点击事件
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
}
