package com.mingrisoft.mrshop.weight;

import android.content.Context;
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
import com.mingrisoft.mrshop.entity.base.Result;
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
 * 功能： 用户登录弹窗
 * 创建日期： 2017/6/27
 */

public class LoginDialog extends CustomDialog implements View.OnClickListener{
    private TextView et_user;      //账号
    private TextView et_pwd;        //密码
    private Button btn_login;   //登录按钮
    private String user;    //输入的账号
    private String pwd;     //输入的密码
    public LoginDialog(Context context) {
        super(context,true);
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HttpCode.LOGIN://登录
                    Type type = new TypeToken<Result>() {}.getType();
                    Logger.json(String.valueOf(msg.obj));
                    Result result = new Gson().fromJson(String.valueOf(msg.obj),type);

                    if (TextUtils.equals(HttpCode.SUCCESS, result.getReason())) {
                        ToastUtils.shortToast(getContext(), "登录成功！");
                        hintKbOne();    //隐藏输入法窗口
                        et_user.setText("");
                        et_pwd.setText("");
                        dismiss();//关闭弹窗


                    } else {
                        ToastUtils.shortToast(getContext(), "加载数据失败！");
                    }
                    break;
                case HttpCode.ERROR://错误
                    ToastUtils.shortToast(getContext(), "连接错误！");
                    break;
            }
            return false;
        }
    });

    //此方法，如果显示则隐藏，如果隐藏则显示
    private void hintKbOne() {
        InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
// 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }




    @Override
    protected void onCreateView(WindowManager windowManager) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        setDialogWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setDialogHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setDialogGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_login);
        findId();//绑定控件
    }
private  void findId(){
    et_user = (TextView) findViewById(R.id.mr_user);
    et_pwd = (TextView) findViewById(R.id.mr_password);
    btn_login = (Button) findViewById(R.id.mr_login);
    btn_login.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mr_login://登录按钮
                user= et_user.getText().toString();
                pwd=et_pwd.getText().toString();
                if(user.equals("")||pwd.equals("")){
                    ToastUtils.shortToast(getContext(),"请输入账号和密码！");
                    return;
                }else{
                    Map<String, Object> map = new HashMap<>();
                    map.put("user", user);
                    map.put("pwd",pwd);
                    HttpUtils.HttpPost(handler, HttpHelper.MINE_LOGIN, map, HttpCode.LOGIN);    //发送POST请求到服务器验证用户身份

                }
                break;
        }
    }
}
