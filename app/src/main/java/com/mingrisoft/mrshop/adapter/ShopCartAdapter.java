package com.mingrisoft.mrshop.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.adapter.listener.ShopCartListener;
import com.mingrisoft.mrshop.db.DBStateCode;
import com.mingrisoft.mrshop.db.DaoUtils;
import com.mingrisoft.mrshop.entity.CartViewState;
import com.mingrisoft.mrshop.entity.GoodsCart;
import com.mingrisoft.mrshop.entity.GoodsShop;
import com.mingrisoft.mrshop.utils.FormatUtils;
import com.mingrisoft.mrshop.utils.GetViewTextUtils;
import com.mingrisoft.mrshop.utils.StaticUtils;
import com.mingrisoft.mrshop.weight.ChangeDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： LYJ
 * 功能： 购物车数据适配器
 * 创建日期： 2017/5/16
 */

public class ShopCartAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;//布局填充器
    private List<GoodsShop> mList;//数据
    private Context mContext;//上下文
    private DaoUtils daoUtils;//数据库操作类
    private LocalBroadcastManager broadcastManager;//本地广播管理器

    /**
     * 构造器
     *
     * @param mList
     * @param context
     */
    public ShopCartAdapter(List<GoodsShop> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
        this.daoUtils = new DaoUtils(context);
        this.mInflater = LayoutInflater.from(context);
        this.broadcastManager = LocalBroadcastManager.getInstance(context);
    }

    /**
     * 分类的数量
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        return mList.size();
    }

    /**
     * 字item的数量
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getCartsList().size();
    }

    /**
     * 返回指定的容器
     *
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    /**
     * 返回指定容器的group的child
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getCartsList().get(childPosition);
    }

    /**
     * 返回group的索引
     *
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 返回child的索引
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 孩子是否和组ID是跨基础数据的更改稳定
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 创建父容器的视图
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (null == convertView) {
            holder = new GroupViewHolder();//创建复用类
            convertView = mInflater.inflate(R.layout.item_cart_group, parent, false);
            holder.groupTitle = (TextView) convertView.findViewById(R.id.item_group_shop);
            holder.groupSelect = (CheckBox) convertView.findViewById(R.id.item_group_all);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        final GoodsShop goodsShop = mList.get(groupPosition);//获取指定位置的数据
        holder.groupTitle.setText(goodsShop.getMerchant());//设置店铺的名称
        /**
         * 设置店铺的全选
         */
        //实际情况应该有店铺的id
        holder.groupSelect.setTag(goodsShop.getMerchant());//此处先用店铺来当唯一标识
        /**
         * 用点击事件代替复选框的选中状态
         */
        holder.groupSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = goodsShop.isCheckAll() ? false : true;
                if (TextUtils.equals(goodsShop.getMerchant(), (CharSequence) v.getTag())) {
                    goodsShop.setIsCheckAll(isChecked);//设置店铺的选中状态
                    //获取指定的子列表数据
                    List<GoodsCart> carts = goodsShop.getCartsList();
                    //遍历数据修改数据
                    for (int i = 0; i < carts.size(); i++) {
                        boolean cartSelect = carts.get(i).getViewState().isCheckViewState();
                        if (cartSelect == isChecked) {
                            continue;//跳出本次循环执行下一次循环
                        }
                        carts.get(i).getViewState().setCheckViewState(isChecked);//更新子列表数据
                    }
                    notifyDataSetChanged();//通知适配器数据更新
                    getCustomSelectData();//回调数据
                }
            }
        });
        holder.groupSelect.setChecked(goodsShop.isCheckAll());//设置选中的状态
        return convertView;
    }

    /**
     * 创建子视图
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (null == convertView) {
            holder = new ChildViewHolder();//创建复用类
            convertView = mInflater.inflate(R.layout.item_cart_child, parent, false);
            holder.childTitle = (TextView) convertView.findViewById(R.id.item_child_title);
            holder.childImage = (ImageView) convertView.findViewById(R.id.item_child_image);
            holder.childPrice = (TextView) convertView.findViewById(R.id.item_child_price);
            holder.childCount = (TextView) convertView.findViewById(R.id.item_input_count);
            holder.childSelect = (CheckBox) convertView.findViewById(R.id.item_child_select);
            holder.childAdd = (ImageButton) convertView.findViewById(R.id.item_add_count);
            holder.childCut = (ImageButton) convertView.findViewById(R.id.item_cut_count);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        final GoodsShop goodShop = mList.get(groupPosition);//当前商品分类的数据
        final List<GoodsCart> carts = goodShop.getCartsList();//购物车中当前分类的总数
        final GoodsCart goodsCart = carts.get(childPosition);//获取指定位置的数据
        final int goodCounts = carts.size();//子布局的个数
        holder.childTitle.post(new Runnable() {
            @Override
            public void run() {
                holder.childTitle.setText(goodsCart.getTitle());
            }
        });
        //设置显示的价钱;
        holder.childPrice.setText(FormatUtils.getPriceText(goodsCart.getPrice()));
        //设置显示的数量
        holder.childCount.setText(String.valueOf(goodsCart.getCount()));
        //设置显示的图片
        Glide.with(mContext).load(goodsCart.getImage()).into(holder.childImage);
        /**
         * 设置是否选中的视图效果
         */
        final String tag = goodsCart.get_id();//获取商品的id，该值属于唯一值
        holder.childSelect.setTag(tag);//设置tag
        holder.childSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String tagStr = (String) buttonView.getTag();
                if (TextUtils.equals(tag, tagStr)) {
                    goodsCart.getViewState().setCheckViewState(isChecked);//更新数据
                    boolean result = getSetGroupValues(carts, goodCounts);
                    goodShop.setIsCheckAll(result);//设置group的选中状态
                    notifyDataSetChanged();//通知适配器数据更新
                    getCustomSelectData();//回调数据
                }
            }
        });
        //设置复选框的选中状态
        holder.childSelect.setChecked(goodsCart.getViewState().isCheckViewState());
        /**
         * -----------------------------------------------------------------------
         *
         * --------------------------关于修改购物车中商品数量--------------------------
         *
         * -----------------------------------------------------------------------
         */
        final String shopID = goodsCart.get_id();//获取商品的位移标识ID
        final CartViewState viewState = goodsCart.getViewState();//获取存储控件状态的容器
        holder.childCut.setTag(tag);//设置标记防止错乱
        holder.childAdd.setTag(tag);//设置标记防止错乱
        /**
         * 减少数量
         */
        holder.childCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(tag, (CharSequence) v.getTag())) {
                    String countStr = GetViewTextUtils.getTextFromView(holder.childCount);
                    int nowCount = Integer.parseInt(countStr);//获取当前的数量
                    if (nowCount > 1) nowCount--;//递减
                    setButtonShowState(goodsCart, viewState, shopID, nowCount, false);//修改按钮的显示效果
                }
            }
        });
        /**
         * 增加数量
         */
        holder.childAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(tag, (CharSequence) v.getTag())) {
                    String countStr = GetViewTextUtils.getTextFromView(holder.childCount);
                    int nowCount = Integer.parseInt(countStr);//获取当前的数量
                    if (nowCount < 99) nowCount++;//递增
                    setButtonShowState(goodsCart, viewState, shopID, nowCount, true);//修改按钮的显示效果
                }
            }
        });
        /**
         * 输入数量
         */
        holder.childCount.setTag(tag);//设置标记防止错位
        holder.childCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(tag, (CharSequence) v.getTag())) {
                    String countStr = GetViewTextUtils.getTextFromView(holder.childCount);
                    final int nowCount = Integer.parseInt(countStr);//获取当前的数量
                    ChangeDialog dialog = new ChangeDialog(mContext, nowCount);//修改弹窗
                    dialog.setListener(new ChangeDialog.DecisionListener() {
                        @Override
                        public void result(int count) {
                            if (count == nowCount) return;
                            boolean isAdd = count > nowCount ? true : false;
                            setButtonShowState(goodsCart, viewState, shopID, count, isAdd);//修改按钮的显示效果
                        }
                    });
                    dialog.show();//显示弹窗
                }
            }
        });
        setButtonState(holder.childCut, R.drawable.cut_count, R.drawable.mr_cut_n, viewState.isCutViewState());
        setButtonState(holder.childAdd, R.drawable.add_count, R.drawable.mr_add_n, viewState.isAddViewState());
        return convertView;
    }

    /**
     * 修改数据库中的数据
     *
     * @param id
     * @param count
     * @return
     */
    private int updateCount(String id, int count) {
        ContentValues values = new ContentValues();
        values.put("count", count);
        return daoUtils.update(StaticUtils.CART_TABLE, values, "_id = ?", new String[]{id});//修改指定的数据
    }

    /**
     * 设置按钮的显示效果
     *
     * @param goodsCart
     * @param state
     * @param id
     * @param changeCount
     * @param isAdd
     */
    private void setButtonShowState(GoodsCart goodsCart, CartViewState state, String id, int changeCount, boolean isAdd) {
        setCountChange(goodsCart, state, changeCount);
        int result = updateCount(id, changeCount);//将数据库中的数据修改
        switch (result) {
            case DBStateCode.OPERATION_SUCCESS://成功
                notifyDataSetChanged();//通知适配器数据更新
                getCustomSelectData();//回调数据
                break;
            case DBStateCode.OPERATION_FAILURE://失败
                changeCount = isAdd ? changeCount-- : changeCount++;
                setCountChange(goodsCart, state, changeCount);
                notifyDataSetChanged();//通知适配器数据更新
                getCustomSelectData();//回调数据
                break;
        }
    }

    /**
     * 设置显示的效果和显示的内容
     *
     * @param goodsCart
     * @param state
     * @param changeCount
     */
    private void setCountChange(GoodsCart goodsCart, CartViewState state, int changeCount) {
        goodsCart.setCount(changeCount);//更改数量
        state.setCutViewState(changeCount <= 1 ? false : true);//改变值
        state.setAddViewState(changeCount >= 99 ? false : true);//改变值
    }

    /**
     * 设置按钮的显示状态
     *
     * @param button
     * @param enabledTrueID
     * @param enabledFalseID
     * @param state
     */
    private void setButtonState(ImageButton button, @DrawableRes int enabledTrueID,
                                @DrawableRes int enabledFalseID, boolean state) {
        int drawResId = state ? enabledTrueID : enabledFalseID;
        button.setBackgroundResource(drawResId);//设置控件的显示效果
        button.setEnabled(state);//设置控件的状态
    }

    /**
     * 获取设置Group的结果
     *
     * @param cartList
     * @param dataSize
     * @return
     */
    private boolean getSetGroupValues(List<GoodsCart> cartList, int dataSize) {
        int selectTrueCount = 0;//选中状态的总个数
        int selectFalseCount = 0;//未选中状态的总个数
        //判断当前状态
        for (int i = 0; i < dataSize; i++) {//遍历其中一个分类的数据集合
            if (cartList.get(i).getViewState().isCheckViewState()) selectTrueCount++;//选中个数加一
            else selectFalseCount++;//未选中状态加一
        }
        if (selectTrueCount == dataSize) return true;
        if (selectFalseCount > 0) return false;
        return false;
    }

    /**
     * 设置子控件可点击
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 父布局的复用类
     */
    public static class GroupViewHolder {
        TextView groupTitle;//主布局标题
        CheckBox groupSelect;//选中框
    }

    /**
     * 子布局的复用类
     */
    public static class ChildViewHolder {
        TextView childTitle;//子布局标题
        ImageView childImage;//商品图片
        TextView childPrice;//商品价钱
        TextView childCount;//数量
        CheckBox childSelect;//选中框
        ImageButton childAdd;//添加
        ImageButton childCut;//减少
    }

    /**
     * 设置数据回调接口
     */
    private ShopCartListener shopCartListener;

    /**
     * 设置回调接口
     *
     * @param shopCartListener
     */
    public void setShopCartListener(ShopCartListener shopCartListener) {
        this.shopCartListener = shopCartListener;
    }

    /**
     * 获取用户选择的数据
     */
    public void getCustomSelectData() {
        boolean selectAllState = false;//购物车全选中的状态
        int groupSelectStateCount = 0;//父视图的选中状态的计数
        List<GoodsCart> resultList = new ArrayList<>();
        int groupCount = getGroupCount();//获取父容器的数量
        double selectPrice = StaticUtils.PRICE_NONE;//默认个数
        int selectCount = StaticUtils.COUNT_NONE;//默认个数
        int cartCountAll = 0;//购物车中的商品的总数量
        if (groupCount != 0) {//此时购物车中没有数据
            //有数据，遍历选中的数据，将符合条件的存储集合中
            for (int group = 0; group < groupCount; group++) {
                if (mList.get(group).isCheckAll() == true) groupSelectStateCount++;
                int childCount = getChildrenCount(group);
                //遍历子项
                for (int child = 0; child < childCount; child++) {
                    //获取指定的数据
                    GoodsCart goodsCart = mList.get(group).getCartsList().get(child);
                    //判断当前商品的选中状态
                    if (goodsCart.getViewState().isCheckViewState()) {
                        //累加个数
                        int goodCount = goodsCart.getCount();//获取单个商品的个数
                        selectCount += goodCount;//计数
                        //累加钱数
                        selectPrice += goodCount * goodsCart.getPrice();//计算
                        //将数据添加到新的集合中
                        resultList.add(goodsCart);
                    }
                    cartCountAll += mList.get(group).getCartsList().get(child).getCount();//累加
                }
            }
        }
        //将数据返回
        if (null != shopCartListener) {
            //将全选的状态返回给调用的界面
            if (groupCount == groupSelectStateCount && groupCount != 0) selectAllState = true;
            //回调函数,将调用界面需要的参数，传给改回调接口的实现类
            shopCartListener.getAllCartMessage(resultList, selectPrice, selectCount, selectAllState);
        }
        //发送本地广播
        Intent intent = new Intent(StaticUtils.THING_COUNT)//发送广播的意图
                .putExtra(StaticUtils.NOW_COUNT, cartCountAll);
        broadcastManager.sendBroadcast(intent);
    }

    /**
     * 更改所有数据
     *
     * @param isChecked
     */
    public void changAllDataSelectState(boolean isChecked) {
        for (int group = 0; group < mList.size(); group++) {
            GoodsShop goodsShop = mList.get(group);//获取指定数据
            List<GoodsCart> cartList = goodsShop.getCartsList();//获取子列表数据
            //遍历子数据
            for (int child = 0; child < cartList.size(); child++) {
                cartList.get(child).getViewState().setCheckViewState(isChecked);//更改子项的数据选中状态
            }
            goodsShop.setIsCheckAll(isChecked);//更改住列表数据的选中状态
        }
        notifyDataSetChanged();//通知适配器数据更新
        getCustomSelectData();//回调数据
    }
}
