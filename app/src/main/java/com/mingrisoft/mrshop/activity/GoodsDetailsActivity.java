package com.mingrisoft.mrshop.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.activity.base.TitleActivity;
import com.mingrisoft.mrshop.adapter.ImageAdapter;
import com.mingrisoft.mrshop.entity.GoodDetails;
import com.mingrisoft.mrshop.entity.base.Result;
import com.mingrisoft.mrshop.helper.ShareHelper;
import com.mingrisoft.mrshop.network.HttpCode;
import com.mingrisoft.mrshop.network.HttpHelper;
import com.mingrisoft.mrshop.network.HttpUtils;
import com.mingrisoft.mrshop.utils.FormatUtils;
import com.mingrisoft.mrshop.utils.GetViewTextUtils;
import com.mingrisoft.mrshop.utils.StaticUtils;
import com.mingrisoft.mrshop.utils.ToastUtils;
import com.mingrisoft.mrshop.view.BadgeView;
import com.mingrisoft.mrshop.weight.AddGoodsDialog;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品详情界面
 */

public class GoodsDetailsActivity extends TitleActivity
        implements ViewPager.OnPageChangeListener,
        AddGoodsDialog.OnGoodsChangeListener,
        View.OnClickListener {
    private String[] imageUrls;//图片的数组
    private ViewPager images;//展示图片
    private TextView imagePage;//显示页码
    private TextView title;//标题
    private TextView prompt;//提示
    private TextView price;//价格
    private TextView brand;//显示的品牌
    private TextView merchant;//显示的店铺
    private CheckBox focusOn;//关注
    private GoodDetails goodDetails;//商品详情


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HttpCode.DETAILS://详情
                    Type type = new TypeToken<Result<GoodDetails>>() {
                    }.getType();
                    Result<GoodDetails> result = new Gson()
                            .fromJson(String.valueOf(msg.obj), type);
                    if (TextUtils.equals(HttpCode.SUCCESS, result.getReason())) {
                        goodDetails = result.getResult();
                        addDataToView();//将数据展示到视图上
                    } else {
                        ToastUtils.shortToast(GoodsDetailsActivity.this, "加载数据失败！");
                    }
                    break;
                case HttpCode.ERROR://错误
                    ToastUtils.shortToast(GoodsDetailsActivity.this, "连接错误！");
                    break;
            }
            return false;
        }
    });
    /**************添加购物车时用开始*************************************************/
    private CheckBox shoppingCart;//购物车
    private Button addThing;//添加到购物车
    private BadgeView badgeView;//显示消息个数
    private ShareHelper snapHelper = ShareHelper.getInstance();
    private AddGoodsDialog addGoodsDialog;//添加弹窗
    private int drawableResID = R.drawable.mr_focus_on01;
    private int thingCount;//商品数量
    private LocalBroadcastManager localBroadcastManager;//本地广播管理器
    private int code;//标记值
    /**************添加购物车时用结束*************************************************/
    /**
     * 将下载的数据展示到指定的控件上
     */
    private void addDataToView() {
        GetViewTextUtils.setText(title, goodDetails.getTitle());//设置标题
        String promptStr = goodDetails.getPrompt();//获取提示文字
        //不为空就展示
        if (!TextUtils.isEmpty(promptStr)) {
            GetViewTextUtils.setText(prompt, promptStr);//设置提示文字
        } else {
            prompt.setVisibility(View.GONE);
        }
        //设置显示的价钱;
        price.setText(FormatUtils.getPriceText(goodDetails.getNowPrice()));
        //设置显示的品牌
        brand.setText(StaticUtils.BRAND + goodDetails.getBrand());
        //设置显示的店铺
        merchant.setText(StaticUtils.MERCHANT + goodDetails.getMerchant());
    }

    /**
     * 设置界面的布局
     *
     * @return
     */
    @Override
    protected int setLayoutID() {
        return R.layout.activity_goods_details;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        images = (ViewPager) findViewById(R.id.show_pictures);
        imagePage = (TextView) findViewById(R.id.show_page);
        title = (TextView) findViewById(R.id.show_title);
        prompt = (TextView) findViewById(R.id.show_prompt);
        price = (TextView) findViewById(R.id.show_price);
        brand = (TextView) findViewById(R.id.show_brand);
        merchant = (TextView) findViewById(R.id.show_merchant);
        focusOn = (CheckBox) findViewById(R.id.focus_on);
        shoppingCart = (CheckBox) findViewById(R.id.shopping_cart);
        addThing = (Button) findViewById(R.id.add_thing);
        badgeView = new BadgeView(this);
        addGoodsDialog = new AddGoodsDialog(this);
    }

    /**
     * 添加监听
     */
    @Override
    public void setListener() {
        images.addOnPageChangeListener(this);//滑动监听
        focusOn.setOnClickListener(this);//点击监听
        shoppingCart.setOnClickListener(this);//点击监听
        addThing.setOnClickListener(this);//点击监听
        addGoodsDialog.setOnGoodsChangeListener(this);//数量发生改变
    }

    /**
     * 设置功能
     */
    @Override
    public void setFunction() {
        titleBar.setTitle(getString(R.string.mr_goods_details));
        Intent intent = getIntent();//获取意图
        String shopID = intent.getStringExtra(StaticUtils.SHOPID);//商品ID
        String shopImage = intent.getStringExtra(StaticUtils.SHOPIMAGE);//图片的网址
        downLoadDataFromNet(shopID);//获取指定的商品详情
        imageUrls = shopImage.split(StaticUtils.THESEPARATOR);
        images.setAdapter(new ImageAdapter(imageUrls, this));//绑定适配器
        imagePage.setText(showPage(1));//设置默认显示的页数
        code = intent.getIntExtra(StaticUtils.CODE, StaticUtils.NONE_CODE);//标记值
        badgeView.setTargetView(shoppingCart);
        badgeView.setBadgeGravity(Gravity.CENTER | Gravity.TOP);
        badgeView.setBadgeMargin(20, 0, 0, 0);
        thingCount = (int) snapHelper.query(StaticUtils.NOW_COUNT, 0);
        badgeView.setBadgeCount(thingCount);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //注册广播
        localBroadcastManager.registerReceiver(receiver,
                new IntentFilter(StaticUtils.THING_COUNT));
    }

    /**
     * 下载网络数据
     */
    private void downLoadDataFromNet(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        HttpUtils.HttpPost(handler, HttpHelper.GOODS_DETAILS, map, HttpCode.DETAILS);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        imagePage.setText(showPage(++position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 格式化字符串
     *
     * @param nowPage
     * @return
     */
    private SpannableString showPage(int nowPage) {
        String message = String.format(StaticUtils.IMAGE_PAGE, nowPage, imageUrls.length);
        SpannableString spannableString = FormatUtils.getChangeTextSize(message, 0, 1, 1.4f);
        return spannableString;
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.focus_on://关注
                if (drawableResID == R.drawable.mr_focus_on01) {
                    drawableResID = R.drawable.mr_focus_on02;
                } else {
                    drawableResID = R.drawable.mr_focus_on01;
                }
                Drawable top = ContextCompat.getDrawable(this, drawableResID);
                focusOn.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                ToastUtils.shortToast(this, "功能即将开放，敬请期待！");
                break;
            case R.id.shopping_cart://购物车
                if (code == StaticUtils.NONE_CODE) {
                    startActivity(activity(ShoppingCartActivity.class));//购物车界面
                }else {
                    finish();//关闭当前界面
                }
                break;
            case R.id.add_thing://添加
                addGoodsDialog.setGoodDetails(goodDetails);
                addGoodsDialog.setFirstImageUrl(HttpHelper.HTTP_URL + imageUrls[0]);
                addGoodsDialog.show();
                break;
        }
    }

    /**
     * 数量发生改变
     *
     * @param selectCount
     */
    @Override
    public void addCount(int selectCount) {
        int changeCount = thingCount = selectCount + thingCount;//数量相加
        badgeView.setBadgeCount(changeCount);
        Intent intent = new Intent(StaticUtils.THING_COUNT)//发送广播的意图
                .putExtra(StaticUtils.NOW_COUNT, changeCount);
        localBroadcastManager.sendBroadcast(intent);//发送广播*/
    }

    //广播接收器
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int count = intent.getIntExtra(StaticUtils.NOW_COUNT, 0);//获取修改的数量
            badgeView.setBadgeCount(count);//设置数量
        }
    };

    /**
     * 界面销毁
     */
    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(receiver);
        super.onDestroy();
    }
}
