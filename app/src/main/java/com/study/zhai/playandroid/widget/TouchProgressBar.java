package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TouchProgressBar extends View {

    private Paint bgProgressPaint;
    private Paint progressPaint;
    private Paint pointPaint;

    public TouchProgressBar(Context context) {
        this(context, null);
    }

    public TouchProgressBar(Context context,AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TouchProgressBar(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgProgressPaint = new Paint();
        bgProgressPaint.setAntiAlias(true);
        bgProgressPaint.setStyle(Paint.Style.FILL);
    }
}
