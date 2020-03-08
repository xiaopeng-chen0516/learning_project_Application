package com.mingrisoft.mrshop.weight;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.activity.MainActivity;
import com.mingrisoft.mrshop.entity.base.Result;
import com.mingrisoft.mrshop.fragment.ShoppingCartFragment;
import com.mingrisoft.mrshop.network.HttpCode;
import com.mingrisoft.mrshop.network.HttpHelper;
import com.mingrisoft.mrshop.network.HttpUtils;
import com.mingrisoft.mrshop.utils.ToastUtils;
import com.mingrisoft.mrshop.weight.custom.CustomDialog;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能： 支付弹窗
 * 创建日期： 2017/6/29
 */

public class PayDialog extends CustomDialog implements View.OnClickListener{
    private Button btn_pay;   //完成支付按钮
    public PayDialog(Context context) {
        super(context,true);
    }
    @Override
    protected void onCreateView(WindowManager windowManager) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        setDialogWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setDialogHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setDialogGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_pay);  //设置布局文件
        findId();//绑定控件
    }
private  void findId(){
    btn_pay = (Button) findViewById(R.id.mr_finish);
    btn_pay.setOnClickListener(this);//设置监听器
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mr_finish://完成按钮
                dismiss();  //关闭弹窗
                break;
        }
    }
}
