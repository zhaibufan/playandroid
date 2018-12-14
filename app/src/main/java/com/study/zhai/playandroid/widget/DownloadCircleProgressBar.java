package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import static android.view.View.MeasureSpec.getMode;

public class DownloadCircleProgressBar extends View {

    private static final String TAG = "DownloadCircleProgressBar";
    private int defaultHeight = 100;
    private int defaultWidth = 100;
    private Paint paint;

    public DownloadCircleProgressBar(Context context) {
        super(context, null);
    }

    public DownloadCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, -1);
    }

    public DownloadCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int heightMeasureSpec) {
        int height;
        int spaceMode = MeasureSpec.getMode(heightMeasureSpec);
        int spaceHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (spaceMode == MeasureSpec.EXACTLY) {
            height = spaceHeight;
        } else {
            height = Math.min(spaceHeight, defaultHeight);
        }
        return height;
    }

    private int measureWidth(int widthMeasureSpec) {
        int width;
        int specMode = getMode(widthMeasureSpec);
        int spaceSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            width = spaceSize;
        } else {
            width = Math.min(spaceSize, defaultWidth);
        }
        return width;
    }
}
