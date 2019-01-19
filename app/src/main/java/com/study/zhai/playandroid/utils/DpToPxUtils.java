package com.study.zhai.playandroid.utils;

import android.content.Context;
import android.util.TypedValue;

import com.study.zhai.playandroid.MyApplication;

/**
 * Created by Pei on 2016-12-28.
 */
public class DpToPxUtils {
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
    public static int sp2px(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                MyApplication.getInstance().getResources().getDisplayMetrics());
    }
    public static int sp2px(int spValue) {
        final float fontScale = MyApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
