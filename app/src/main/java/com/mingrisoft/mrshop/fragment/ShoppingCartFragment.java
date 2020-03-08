package com.mingrisoft.mrshop.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.activity.GoodsDetailsActivity;
import com.mingrisoft.mrshop.activity.MainActivity;
import com.mingrisoft.mrshop.adapter.ShopCartAdapter;
import com.mingrisoft.mrshop.adapter.listener.ShopCartListener;
import com.mingrisoft.mrshop.db.DBStateCode;
import com.mingrisoft.mrshop.db.DaoUtils;
import com.mingrisoft.mrshop.entity.CartViewState;
import com.mingrisoft.mrshop.entity.GoodsCart;
import com.mingrisoft.mrshop.entity.GoodsShop;
import com.mingrisoft.mrshop.fragment.base.TitleFragment;
import com.mingrisoft.mrshop.utils.FormatUtils;
import com.mingrisoft.mrshop.utils.StaticUtils;
import com.mingrisoft.mrshop.utils.ToastUtils;
import com.mingrisoft.mrshop.weight.PayDialog;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： LYJ
 * 功能： 购物车
 * 创建日期： 2017/5/3
 */

public class ShoppingCartFragment extends TitleFragment
        implements ExpandableListView.OnGroupClickListener,
        ShopCartListener, View.OnClickListener,ExpandableListView.OnChildClickListener {

    private boolean titleBarIsShow;//显示标题栏
    private boolean isInit = true;//是否是初始化
    private DaoUtils daoUtils;//数据库操作类
    private List<GoodsShop> carts;//购物车数据
    private ExpandableListView listView;//购物车列表
    private TextView allPrice;//总钱数
    private TextView allCount;//总数量
    private ShopCartAdapter shopCartAdapter;//购物车列表适配器
    private CheckBox selectAll;//选择全部
    private RelativeLayout empty;//空
    private TextView rightView;//右侧的编辑按钮
    private Button cartCommit;//提交订单
    private LinearLayout setBarOne;//设置栏1
    private LinearLayout setBarTwo;//设置栏2
    private Button share, focus, delete;//分享、关注、删除
    private String saveStr, oldStr;//保存数据
    private PayDialog payDialog;//添加支付弹窗
    private LocalBroadcastManager localBroadcastManager;//本地广播管理器

    /**
     * 设置视图的布局
     *
     * @return
     */
    @Override
    protected int setLayoutID() {
        return R.layout.fragment_shoppingcart;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    @Override
    public void initView(View view) {
        listView = (ExpandableListView) view.findViewById(R.id.cart_list);
        allPrice = (TextView) view.findViewById(R.id.all_price);
        allCount = (TextView) view.findViewById(R.id.all_count);
        selectAll = (CheckBox) view.findViewById(R.id.cart_all);
        empty = (RelativeLayout) view.findViewById(R.id.empty);
        setBarOne = (LinearLayout) view.findViewById(R.id.set_bar_1);
        setBarTwo = (LinearLayout) view.findViewById(R.id.set_bar_2);
        cartCommit = (Button) view.findViewById(R.id.cart_commit);
        share = (Button) view.findViewById(R.id.cart_share);
        focus = (Button) view.findViewById(R.id.cart_shop_focus);
        delete = (Button) view.findViewById(R.id.cart_delete);
        payDialog = new PayDialog(view.getContext());       //创建支付弹窗
    }

    /**
     * 设置监听
     */
    @Override
    public void setListener() {
        listView.setOnGroupClickListener(this);
        listView.setOnChildClickListener(this);
        selectAll.setOnClickListener(this);
        cartCommit.setOnClickListener(this);
        share.setOnClickListener(this);
        delete.setOnClickListener(this);
        focus.setOnClickListener(this);
    }

    /**
     * 设置功能
     */
    @Override
    public void setFunction() {
        titleView.setTitle(getString(R.string.mr_shoppingcart));
        titleView.backBtnIsShow(titleBarIsShow);
        titleView.setTitleBarBackgroundColor(Color.WHITE);
        titleView.setTitleColor(Color.BLACK);
        titleView.setBackBtnIcon(R.drawable.title_back_btn2);
        addTitleRightView();//添加标题栏右侧的按钮
        //添加数据为空时显示的界面效果视图
        listView.setEmptyView(empty);
        daoUtils = new DaoUtils(mContext);//初始化数据库操作类
        if (isInit) {//第一次加载界面
            addDataToView();//将数据展示到界面中
            isInit = false;//设为false
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        //支付窗口关闭时
        payDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(getActivity().getLocalClassName().equals("activity.MainActivity")) {

                    MainActivity activity = (MainActivity) getActivity();
                    activity.radioGroup.check(R.id.mr_shoppingmall);  //切换到首页


                }else  if(getActivity().getLocalClassName()
                        .equals("activity.ShoppingCartActivity")){
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);    //返回到主界面
                }
                Intent intent = new Intent(StaticUtils.THING_COUNT)//发送广播的意图更新购物车数量标记
                        .putExtra(StaticUtils.NOW_COUNT, 0);
                localBroadcastManager.sendBroadcast(intent);//发送广播*/
                daoUtils.deleteAll(StaticUtils.CART_TABLE);     //清空购物车
                /////////////////////////////////////////////////////////////////////////
            }
        });

    }
    private boolean isMake;//编辑

    /**
     * 添加标题栏中的右侧的按钮
     */
    private void addTitleRightView() {
        rightView = new TextView(mContext);
        rightView.setText("编辑");
        rightView.setGravity(Gravity.CENTER);
        rightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        titleView.addRightView(rightView);//向标题栏中添加控件
    }

    /**
     * 设置显示的内容和字体颜色
     *
     * @param message
     * @param color
     */
    private void setRightTextViewShow(String message, @ColorRes int color) {
        rightView.setText(message);//设置显示文字
        rightView.setTextColor(ContextCompat.getColor(mContext, color));//设置字体颜色
    }

    /**
     * 将数据展示到界面中
     */
    private void addDataToView() {
        showBottomBar(false);
        isMake = false;
        setRightTextViewShow(StaticUtils.EDITOR, R.color.default_text_color);
        //加载数据到购物车列表中
        List<GoodsCart> list = daoUtils.getResultListAll(StaticUtils.CART_TABLE, GoodsCart.class);
        initCartData(list);//初始化购物车数据
        shopCartAdapter = new ShopCartAdapter(carts, mContext);
        listView.setAdapter(shopCartAdapter);
        for (int i = 0; i < carts.size(); i++) {//默认全展开
            listView.expandGroup(i);
        }
        shopCartAdapter.setShopCartListener(this);
        shopCartAdapter.getCustomSelectData();
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMake) {//完成
                    initCartData(daoUtils.getResultListAll(StaticUtils.CART_TABLE, GoodsCart.class));
                    makeNewSaveJsonMessage();//验证修改后的数据生成新的保存数据
                    setEditorEndShow();//设置修改之后的显示效果
                    setRightTextViewShow(StaticUtils.EDITOR, R.color.default_text_color);
                } else {//编辑
                    saveStr = new Gson().toJson(carts);//保存数据为JSON
                    selectAll.setChecked(false);//解除全选按钮的选中状态
                    shopCartAdapter.changAllDataSelectState(false);
                    isMake = true;
                    setRightTextViewShow(StaticUtils.COMPLETE, R.color.black);
                }
                showBottomBar(isMake);
            }
        });
    }

    /**
     * 设置界面编辑结束后的数据展示效果
     */
    private void setEditorEndShow() {
        Type type = new TypeToken<List<GoodsShop>>() {
        }.getType();//设置解析的数据类型
        List<GoodsShop> saveList = new Gson().fromJson(saveStr, type);
        carts.clear();//清空数据
        carts.addAll(saveList);//填充数据
        shopCartAdapter.notifyDataSetChanged();//通知适配器数据更新
        shopCartAdapter.getCustomSelectData();//回去改变结果
        isMake = false;
    }

    /**
     * 设置底部栏的显示效果
     *
     * @param isOpen
     */
    private void showBottomBar(boolean isOpen) {
        if (isOpen) {
            setBarOne.setVisibility(View.GONE);
            cartCommit.setVisibility(View.GONE);
            setBarTwo.setVisibility(View.VISIBLE);
        } else {
            setBarOne.setVisibility(View.VISIBLE);
            cartCommit.setVisibility(View.VISIBLE);
            setBarTwo.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化购物车数据
     *
     * @param list
     */
    private void initCartData(List<GoodsCart> list) {
        if (null == carts) {
            carts = new ArrayList<>();//商品类型数据结合
        } else {
            carts.clear();//清空数据集合
        }
        Cursor cursor = daoUtils.getResultCursor(StaticUtils.CART_TABLE,//分组查询
                new String[]{"merchant"}, null, null, "merchant", null, null);
        while (cursor.moveToNext()) {
            GoodsShop goodsShop = new GoodsShop();
            goodsShop.setMerchant(cursor.getString(cursor.getColumnIndex("merchant")));
            goodsShop.setCartsList(new ArrayList<GoodsCart>());//初始化集合数据
            carts.add(goodsShop);//添加购物车商品类型
        }
        cursor.close();//关闭游标
        daoUtils.closedDB();//关闭数据库连接
        for (GoodsCart goodsCart : list) {//循环处理数据
            String shopName = goodsCart.getMerchant();//获取店铺名称
            int shopCount = goodsCart.getCount();//获取选中的数量
            goodsCart.setViewState(new CartViewState(
                    true, shopCount > 1 ? true : false, false));//初始化控件的状态
            int position = getIndex(shopName);//索引值
            if (position == -1) {
                return;//等于-1说明出现问题
            } else {//将指定的数据添加到指定的数据集合中
                carts.get(position).getCartsList().add(goodsCart);//添加数据
            }
        }
        oldStr = new Gson().toJson(carts);//保存数据
    }

    /**
     * 获取指定资源index索引值
     *
     * @param merchant
     * @return
     */
    private int getIndex(String merchant) {
        int index = -1;//索引值
        for (int i = 0; i < carts.size(); i++) {
            //判断店铺名称是否存在
            if (TextUtils.equals(merchant, carts.get(i).getMerchant())) {
                index = i;//获取指定数据的索引值
                break;//退出循环
            }
        }
        return index;
    }

    /**
     * 设置返回键显示
     *
     * @param isShow
     */
    public void setBackBtnShow(boolean isShow) {
        this.titleBarIsShow = isShow;
    }

    /**
     * fragment显示发生改变调用此处
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {//显示在最上边
            addDataToView();//将数据展示到界面中
        } else {
            dataReply();//数据恢复
        }
    }

    /**
     * 数据恢复
     */
    private void dataReply() {
        if (isMake) {
//                Log.w("数据恢复", "onHiddenChanged: ");
            setEditorEndShow();//设置修改之后的显示效果
            //将数据库中指定的数据恢复到为修改之前
            for (int i = 0; i < carts.size(); i++) {
                List<GoodsCart> goods = carts.get(i).getCartsList();
                for (int j = 0; j < goods.size(); j++) {
                    String childID = goods.get(i).get_id();
                    int childCount = goods.get(i).getCount();
                    //更新数据
                    ContentValues values = new ContentValues();
                    values.put("count", childCount);
                    int result = daoUtils.update(StaticUtils.CART_TABLE,
                            values, "_id = ?", new String[]{childID});
                    //此时相当于在界面被覆盖时，修改数据失败
                    if (result == DBStateCode.OPERATION_FAILURE) {
                        Log.w("修改数据失败", "这里可以做一下数据的本地保存");
                        return;
                    }
                }
            }
        }
    }

    /**
     * 设置点击时列表永远是展开的
     *
     * @param parent
     * @param v
     * @param groupPosition
     * @param id
     * @return
     */
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    private List<GoodsCart> mCarts;//选中的物品的数据

    /**
     * 获取选中商品的总价钱和总数量
     *
     * @param carts       购物车中的数据
     * @param selectPrice 选中商品的总价
     * @param selectCount 选中商品的总数
     * @param isCheckAll  是否全选择
     */
    @Override
    public void getAllCartMessage(@Nullable List<GoodsCart> carts, Double selectPrice, int selectCount, boolean isCheckAll) {
        allPrice.setText(StaticUtils.PRICE + FormatUtils.getKeepTwoDecimalPlaces(selectPrice));//设置总价钱
        allCount.setText(StaticUtils.COUNT + selectCount);//设置总数量
        this.mCarts = carts;//获取选中的数据结果
        selectAll.setChecked(isCheckAll);//设置全选按钮的选中状态
        Logger.json(new Gson().toJson(carts));
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_all://全选按钮
                boolean isChecked = ((CompoundButton) v).isChecked();
                shopCartAdapter.changAllDataSelectState(isChecked);
                break;
            case R.id.cart_delete://删除商品
                int result = DBStateCode.OPERATION_FAILURE;//删除数据返回的结果
                if (mCarts.size() > 0) {
                    //批量删除选中的数据
                    for (int i = 0; i < mCarts.size(); i++) {
                        result = daoUtils.delete(StaticUtils.CART_TABLE, "_id = ?"
                                , new String[]{mCarts.get(i).get_id()});
                        if (result == DBStateCode.OPERATION_FAILURE) break;
                    }
                }
                //根据数据库的成功和失败来更改界面中展示的数据
                switch (result) {
                    case DBStateCode.OPERATION_FAILURE://删除失败
                        ToastUtils.shortToast(mContext, "删除数据失败！");
                        break;
                    case DBStateCode.OPERATION_SUCCESS://删除成功
                        //查询修改数据后的购物车数据
                        initCartData(daoUtils.getResultListAll(StaticUtils.CART_TABLE, GoodsCart.class));
                        shopCartAdapter.notifyDataSetChanged();//通知适配器数据更新
                        shopCartAdapter.getCustomSelectData();//回去改变结果
                        //验证修改后的数据生成新的保存数据
                        makeNewSaveJsonMessage();
                        break;
                }
                break;
            case R.id.cart_share://分享
            case R.id.cart_shop_focus://关注商品
                ToastUtils.shortToast(mContext, "功能即将开放，敬请期待！");
                break;
            case R.id.cart_commit://结算
                if(mCarts.size() > 0){
                    payDialog.show();       //显示支付弹窗
                }else{
                    ToastUtils.shortToast(getContext(),"请先选择要购买商品！");
                    return;
                }
                break;
        }
    }

    /**
     * 生成性的保存数据
     */
    private void makeNewSaveJsonMessage() {
        //解析保存数据
        Type type = new TypeToken<List<GoodsShop>>() {}.getType();//设置解析的数据类型
        List<GoodsShop> saveList = new Gson().fromJson(saveStr, type);
        List<GoodsShop> oldList = new Gson().fromJson(oldStr, type);
        //循环当前修改后的购物车数据集合
        for (int cart = 0; cart < oldList.size(); cart++) {
            //正常应该有ID来代表对应的店铺，这里为了方便就以店铺的名称作为唯一标识
            String shopName = oldList.get(cart).getMerchant();//店铺名称
            for (int save = 0; save < saveList.size(); save++) {
                //判断购物车中是否还有这家店铺的商品数据
                if (TextUtils.equals(saveList.get(save).getMerchant(), shopName)) {
                    //设置商铺的保存状态
                    oldList.get(cart).setIsCheckAll(saveList.get(save).isCheckAll());
                    //获取数据
                    List<GoodsCart> childCarts = oldList.get(cart).getCartsList();
                    for (int cChild = 0; cChild < childCarts.size(); cChild++) {
                        String shopID = childCarts.get(cChild).get_id();//商品ID
                        for (int sChild = 0; sChild < saveList.get(save).getCartsList().size(); sChild++) {
                            List<GoodsCart> childSave = saveList.get(save).getCartsList();
                            //比较店铺中子项数据
                            if (TextUtils.equals(shopID, childSave.get(sChild).get_id())) {
                                //保存控件的选中状态状态
                                childCarts.get(cChild).getViewState()
                                        .setCheckViewState(childSave.get(sChild).getViewState().isCheckViewState());
                                break;//跳出本次循环
                            }
                        }
                    }
                    break;//跳出本次循环
                }
            }
        }
        saveStr = new Gson().toJson(oldList);//保存新的数据
    }

    /**
     * 保证数据的正确性
     */
    @Override
    public void onPause() {
        dataReply();//数据恢复
//        Log.i("onPause: ", "购物车");
        super.onPause();
    }

    /**
     * 购物车列表子项的点击事件
     * @param parent
     * @param v
     * @param groupPosition
     * @param childPosition
     * @param id
     * @return
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        GoodsCart cartShop = carts.get(groupPosition).getCartsList().get(childPosition);
        //创建跳转界面的意图
        Intent startTo = activity(GoodsDetailsActivity.class);
        //传递序列化的对象
        startTo.putExtra(StaticUtils.SHOPID,cartShop.get_id())
                .putExtra(StaticUtils.SHOPIMAGE,cartShop.getImage_url())
                .putExtra(StaticUtils.CODE,StaticUtils.CART_CODE);
        //跳转界面
        startActivityForResult(startTo,StaticUtils.REQUEST_TO_RESULT_1);
        return false;
    }

    /**
     * 返回当前界面
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.w("onActivityResult ", "返回到当前界面！");
        addDataToView();//将数据展示到界面中
    }
}
