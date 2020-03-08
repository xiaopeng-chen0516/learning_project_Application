package com.mingrisoft.mrshop.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import java.text.DecimalFormat;

/**
 * 作者： LYJ
 * 功能： 格式化工具
 * 创建日期： 2017/5/9
 */

public class FormatUtils {

    /**
     * 保留两位小数
     *
     * @param values
     * @return
     */
    public static String getKeepTwoDecimalPlaces(double values) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(values);
    }

    /**
     * 将显示价格的字体的正数位置变大
     */
    public static SpannableString getPriceText(double price) {
        String priceStr = getKeepTwoDecimalPlaces(price);//保留两位小数
        String message = StaticUtils.CURRENCYYMBOL + priceStr;//加上单位
        int endIndex = message.indexOf(".");//获取更改的结束位置
        SpannableString spannableString = getChangeTextSize(message,1,endIndex,1.6f);
        return spannableString;
    }

    /**
     * 改变指定位置的字体样式
     *
     * @param string
     * @param startIndex
     * @param endIndex
     * @param values
     * @return
     */
    public static SpannableString getChangeTextSize(String string, int startIndex, int endIndex, float values) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new RelativeSizeSpan(values), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
