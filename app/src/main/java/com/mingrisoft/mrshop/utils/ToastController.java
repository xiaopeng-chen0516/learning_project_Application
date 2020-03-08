package com.mingrisoft.mrshop.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.mingrisoft.mrshop.R;


/**
 * 作者： LYJ
 * 功能：
 * 创建日期： 2017/3/22
 */

public class ToastController {

    private Context context;//上下文
    private ToastParams params;//配置
    private boolean isCustom;//是否为自定义效果
    public ToastController(Context context){
        this.context = context;
    }

    /**
     * 传入配置
     * @param toastParams
     */
    public void setParams(ToastParams toastParams){
        this.params = toastParams;
    }
    /**
     * 返回吐司
     * @return
     */
    public Toast getToast(){
        Toast toast = null;
        /**
         * 有自定义布局
         */
        if (null != params.mCustomView){
            toast = new Toast(context);
            toast.setView(params.mCustomView);
            isCustom = true;
        }
        /**
         * 创建Toast
         */
        if (null == toast){
            toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
        }
        /**
         * 设置显示位置
         */
        if (params.setGravity){
            toast.setGravity(params.mGravity,params.mOffsetX,params.mOffsetY);
        }
        /**
         * 设置显示时间
         */
        toast.setDuration(params.mDuration);
        /**
         * 设置显示边距
         */
        if (params.setMargin){
            toast.setMargin(params.mHorizontalMargin,params.mVerticalMargin);
        }
        /**
         * 设置显示的文字
         */
        if (isCustom == false){
            if (null != params.mMessage){
                toast.setText(params.mMessage);
            }else {
                toast.setText(params.mMessageID);
            }
        }
        return toast;
    }
    /**
     * 构造Toast的属性
     */
    public static class ToastParams {
        public View mCustomView;//自定义布局控件
        public int mDuration = Toast.LENGTH_SHORT;//吐司显示时间
        public int mGravity = Gravity.NO_GRAVITY;//内容摆放的位置
        public int mOffsetX = 0;//水平偏移量
        public int mOffsetY = 0;//垂直偏移量
        public int mMessageID = R.string.app_name;//提示文字资源ID
        public String mMessage;//提示文字
        public Context mContext;//上下文
        public float mHorizontalMargin = 0;//水平边距
        public float mVerticalMargin = 0;//垂直边距
        public boolean setMargin;//设置边距开关
        public boolean setGravity;//设置位置开关
    }
}
