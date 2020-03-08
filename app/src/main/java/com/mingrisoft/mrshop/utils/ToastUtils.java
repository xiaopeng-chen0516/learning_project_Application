package com.mingrisoft.mrshop.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

/**
 * 作者： LYJ
 * 功能：吐司弹窗
 * 创建日期： 2017/3/22
 */

public class ToastUtils {
    final ToastController controller;//控制器
    /**
     * 构造方法
     *
     * @param context
     */
    public ToastUtils(Context context) {
        this.controller = new ToastController(context.getApplicationContext());
    }
    /**
     * 弹出短吐司
     *
     * @param context 传入上下文
     * @param message 传入弹出的内容
     */
    public static void shortToast(Context context, @NonNull String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出长吐司
     *
     * @param context 传入上下文
     * @param message 传入弹出的内容
     */
    public static void longToast(Context context, @NonNull String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * 构造者
     */
    public static class Builder {
        private final ToastController.ToastParams params;//设置属性

        /**
         * 构造器
         *
         * @param context
         */
        public Builder(Context context) {
            this.params = new ToastController.ToastParams();
            this.params.mContext = context.getApplicationContext();
        }

        /**
         * 设置自定义布局控件
         *
         * @param view
         * @return
         */
        public Builder setCustomView(View view) {
            this.params.mCustomView = view;
            return this;
        }
        /**
         * 设置Toast显示时间
         *
         * @param duration
         * @return
         */
        public Builder setDuration(int duration) {
            this.params.mDuration = duration;
            return this;
        }

        /**
         * 设置横向偏移量
         *
         * @param offsetX
         * @return
         */
        public Builder setOffsetX(int offsetX) {
            this.params.mOffsetX = offsetX;
            return this;
        }

        /**
         * 设置竖向偏移量
         *
         * @param offsetY
         * @return
         */
        public Builder setOffsetY(int offsetY) {
            this.params.mOffsetY = offsetY;
            return this;
        }

        /**
         * 设置内容摆放位置
         *
         * @param gravity
         * @return
         */
        public Builder setGravity(int gravity) {
            this.params.mGravity = gravity;
            return this;
        }

        /**
         * 设置水平边距
         *
         * @param horizontalMargin
         * @return
         */
        public Builder setHorizontalMargin(float horizontalMargin) {
            this.params.mHorizontalMargin = horizontalMargin;
            return this;
        }

        /**
         * 设置垂直边距
         *
         * @param verticalMargin
         * @return
         */
        public Builder setVerticalMargin(float verticalMargin) {
            this.params.mVerticalMargin = verticalMargin;
            return this;
        }

        /**
         * 设置显示的内容
         *
         * @param message
         * @return
         */
        public Builder setMessage(@NonNull String message) {
            this.params.mMessage = message;
            return this;
        }

        /**
         * 设置显示的内容的资源ID
         *
         * @param strID
         * @return
         */
        public Builder setMessage(@StringRes int strID) {
            this.params.mMessageID = strID;
            return this;
        }

        /**
         * 开启设置位置
         * @param isOpen
         * @return
         */
        public Builder openSetGravity(boolean isOpen){
            this.params.setGravity = isOpen;
            return this;
        }

        /**
         * 开启设置边距
         * @param isOpen
         * @return
         */
        public Builder openSetMargin(boolean isOpen){
            this.params.setMargin = isOpen;
            return this;
        }

        /**
         * 显示吐司
         * @return
         */
        public Builder show(){
            build().show();
            return this;
        }
        /**
         * 创建Toast对象
         *
         * @return
         */
        public Toast build() {
            ToastUtils toastUtils = new ToastUtils(params.mContext);
            toastUtils.controller.setParams(params);
            return toastUtils.controller.getToast();
        }
    }
}
