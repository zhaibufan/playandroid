package com.study.zhai.playandroid.widget;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * 渐变 缩小
 */
public class ScaleTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.70f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
            page.setAlpha(MIN_ALPHA);
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        } else if (position < 0) {
            float scaleX = 1 + 0.3f * position;
            Log.d("google_lenve_fb", "transformPage: scaleX:" + scaleX);
            page.setScaleX(scaleX);
            page.setScaleY(scaleX);
            page.setAlpha(MIN_ALPHA + (1+position) * (1-MIN_ALPHA));
        } else if (position > 0){
            float scaleX = 1 - 0.3f * position;
            page.setScaleX(scaleX);
            page.setScaleY(scaleX);
            page.setAlpha(MIN_ALPHA + (1-position) * (1-MIN_ALPHA));
        } else {
            page.setAlpha(1);
        }

    }
}

