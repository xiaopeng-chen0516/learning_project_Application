package com.mingrisoft.mrshop.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.activity.base.BaseActivity;
import com.mingrisoft.mrshop.fragment.CategoryFragment;
import com.mingrisoft.mrshop.fragment.MineFragment;
import com.mingrisoft.mrshop.fragment.ShoppingCartFragment;
import com.mingrisoft.mrshop.fragment.ShoppingMallFragment;
import com.mingrisoft.mrshop.helper.ShareHelper;
import com.mingrisoft.mrshop.utils.StaticUtils;
import com.mingrisoft.mrshop.utils.ToastUtils;
import com.mingrisoft.mrshop.view.BadgeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    public RadioGroup radioGroup;//底部切换按钮
    private List<Fragment> fragments;//Fragment集合
    /***********************实现角标(1)开始********************************/
    public TextView show_count;//购物车
    private int count;//计数器
    private BadgeView badgeView;//展示消息个数
    private ShareHelper snapHelper = ShareHelper.getInstance();
    private LocalBroadcastManager broadcastManager;//本地广播管理器
    /***********************实现角标(1)结束********************************/
    /***********************实现角标(2、3)开始********************************/
    // （2）第三方开源控件的BadgeView类
    // （3）创建ShareHelper类
    /***********************实现角标(2、3)结束********************************/
    /**
     * 设置界面的布局
     * @return
     */
    @Override
    public int setLayoutID() {
        return R.layout.activity_main;
    }

    /**
     * 初始化
     */
    @Override
    public void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.bottombar);//初始化控件
        fragments = new ArrayList<>();//初始化集合
        /***********************实现角标(4)开始********************************/
        show_count = (TextView) findViewById(R.id.show_count);//购物车
        badgeView = new BadgeView(this);//消息
        /***********************实现角标(4)结束********************************/
    }

    /**
     * 设置监听
     */
    @Override
    public void setListener() {
        radioGroup.setOnCheckedChangeListener(this);//设置单选的选择监听
    }

    /**
     * 设置功能
     */
    @Override
    public void setFunction() {
        radioGroup.check(R.id.mr_shoppingmall);//默认展示商城界面
        /***********************实现角标(6)开始********************************/
        count = (int) snapHelper.query(StaticUtils.NOW_COUNT,0);
        badgeView.setTargetView(show_count);//绑定控件
        badgeView.setBadgeCount(count);//设置默认显示的数量
        badgeView.setBadgeGravity(Gravity.CENTER | Gravity.TOP);//设置显示位置
        badgeView.setBadgeMargin(10,0,0,0);//设置偏移量
        broadcastManager = LocalBroadcastManager.getInstance(this);//获取本地广播管理器
        //注册广播
        broadcastManager.registerReceiver(receiver,
                new IntentFilter(StaticUtils.THING_COUNT));
        //申请读写权限
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, StaticUtils.REQUEST_CODE_1);
        /***********************实现角标(6)结束********************************/
    }

    /**
     * 选择切换界面
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (checkedId) {
            case R.id.mr_shoppingmall://商城
                selectFragmentShow(transaction, ShoppingMallFragment.class);
                break;
            case R.id.mr_category://分类
                selectFragmentShow(transaction, CategoryFragment.class);
                break;
            case R.id.mr_shoppingcart://购物车
                selectFragmentShow(transaction, ShoppingCartFragment.class);
                break;
            case R.id.mr_mine://我的
                selectFragmentShow(transaction, MineFragment.class);
                break;
        }
        transaction.commit();
    }
    /**
     * 选择要显示的Fragment
     *
     * @param transaction
     * @param <T>
     * @return
     */
    private   <T extends Fragment> T selectFragmentShow(FragmentTransaction transaction, Class<T> clazz) {
        T fragment = null;
        boolean isHave = false;
        if (fragments.size() == 0) {//默认就添加
            fragment = addFragment(transaction, clazz);
            return fragment;
        }
        //判断集合中是否有这个元素
        for (int i = 0, length = fragments.size(); i < length; i++) {
            fragment = (T) fragments.get(i);
            if (fragment.getClass().equals(clazz)) {
                transaction.show(fragment);
                isHave = true;
                continue;
            }
            transaction.hide(fragments.get(i));
        }
        if (isHave == false) {
            fragment = addFragment(transaction, clazz);
        }
        return fragment;
    }

    /**
     * 添加元素
     *
     * @param transaction
     * @param clazz
     * @param <T>
     * @return
     */
    public  <T extends Fragment> T addFragment(FragmentTransaction transaction, Class<T> clazz) {
        T fragment = null;
        try {
            fragment = clazz.newInstance();
            transaction.add(R.id.frag_home, fragment).show(fragment);
            fragments.add(fragment);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;

    }

    /**
     * 申请权限成功
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        boolean isSave = (boolean) snapHelper.query(StaticUtils.REQUEST_WRITE,false);
        if (requestCode == StaticUtils.REQUEST_CODE_1 && !isSave){
            ToastUtils.shortToast(this,"申请文件读写权限成功！");
            snapHelper.save(StaticUtils.REQUEST_WRITE,!isSave).commit();
        }
    }

    /**
     * 申请权限失败
     * @param requestCode
     */
    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
        if (requestCode == StaticUtils.REQUEST_CODE_1)
            ToastUtils.shortToast(this,"申请文件读写权限失败！");
    }

    /***********************实现角标(5)开始********************************/
    /**
     * 广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int count = intent.getIntExtra(StaticUtils.NOW_COUNT,0);//获取修改的数量
            badgeView.setBadgeCount(count);//设置数量
            snapHelper.save(StaticUtils.NOW_COUNT,count).commit();//保存数量
        }
    };
    /***********************实现角标(5)结束********************************/
    /***********************实现角标(7)开始********************************/
    /**
     * 界面销毁
     */
    @Override
    protected void onDestroy() {
        //解除广播的绑定
        broadcastManager.unregisterReceiver(receiver);
        super.onDestroy();
    }
    /***********************实现角标(7)结束********************************/
}
