package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.study.zhai.playandroid.log.LogUtils;
import com.study.zhai.playandroid.utils.DpToPxUtils;

import static android.view.View.MeasureSpec.getMode;

/**
 * 音频条状图
 */
public class AudioView extends View {

    private static final String TAG = "AudioView";
    private int defauleHeight = 200;
    private int defaultWidth = 500;
    private float mWidth, mHeight;
    private float distanceY; // 单个音频条的宽度
    private Context mContext;
    private float spaceing; // 每个音频条之间的间距
    private Paint mPaint;
    private float random;

    public AudioView(Context context) {
        this(context, null);
    }

    public AudioView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public AudioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量宽高
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int heightMeasureSpec) {
        int height;
        int spaceMode = MeasureSpec.getMode(heightMeasureSpec);
        int spaceHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (spaceMode == MeasureSpec.EXACTLY) {
            height = spaceHeight;
        } else {
            height = Math.min(spaceHeight, defauleHeight);
        }
        return height;
    }

    private int measureWidth(int widthMeasureSpec) {
        int width;
        int specMode = getMode(widthMeasureSpec);
        int spceSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            width = spceSize;
        } else {
            width = Math.min(spceSize, defaultWidth);
        }
        return width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        computeViewSize();
        drawAudioView(canvas);
    }

    private void computeViewSize() {
        spaceing = DpToPxUtils.dp2px(mContext, 2);
        mWidth = getWidth();
        mHeight = getHeight();
        distanceY = (mWidth - 39 * spaceing) / 40;
        LogUtils.d(TAG, "mWidth=" + mWidth + "--mHeight=" + mHeight + "--distanceY=" + distanceY+"--spaceing="+spaceing);
    }

    private void drawAudioView(Canvas canvas) {
        for (int i = 0; i < 40; i++) {
            random = (float) ((Math.random()*4+6)/10); //生成6-10之间的随机数
            LogUtils.d(TAG, "random="+random);
            canvas.drawRect(spaceing * i + distanceY * i, mHeight / 2 * random, distanceY * (i + 1) + spaceing * i, mHeight / 2, mPaint);
            //bottom = mHeight/2+(mHeight/2-mHeight/2*random) = mHeight - mHeight/2*random
            canvas.drawRect(spaceing * i + distanceY * i, mHeight / 2, distanceY * (i + 1) + spaceing * i, mHeight - mHeight / 2 * random, mPaint);
        }
    }

    public void changeHeight(float db){
        random = db;
        invalidate();
    }
}
