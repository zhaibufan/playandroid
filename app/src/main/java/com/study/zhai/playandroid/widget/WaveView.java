package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.study.zhai.playandroid.utils.DpToPxUtils;

/**
 * 自定义三条音频波形图
 *
 *      三条波形图的初始点和振幅不同 其他一致
 */
public class WaveView extends View {

    private Paint wavePaint1;
    private Paint wavePaint2;
    private Paint wavePaint3;
    private Path wavePath1;
    private Path wavePath2;
    private Path wavePath3;
    private int defaultWidth = 300, defaultHeight = 100; //默认的宽高 当宽高为wrap_content时用默认宽高
    private int mWidth, mHeight;
    private float amplitude; //振幅
    private float offsetAngle1 = -(float) Math.PI / 4; //波形图1的初始点在Π/4处
    private float offsetAngle2 = (float) Math.PI / 2; //波形图2的初始点在Π/2处
    private float offsetAngle3 = (float) Math.PI; //波形图1的初始点在Π处
    private boolean isRunning;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        wavePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint1.setStyle(Paint.Style.STROKE);
        wavePaint1.setStrokeWidth(DpToPxUtils.dp2px(2));
        wavePath1 = new Path();

        wavePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint2.setStyle(Paint.Style.STROKE);
        wavePaint2.setStrokeWidth(DpToPxUtils.dp2px(2));
        wavePath2 = new Path();

        wavePaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint3.setStyle(Paint.Style.STROKE);
        wavePaint3.setStrokeWidth(DpToPxUtils.dp2px(2));
        wavePath3 = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(defaultWidth, widthMeasureSpec);
        int height = measureHeight(defaultHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        initPaintShader();
    }

    private int measureWidth(int defaultWidth, int widthMeasureSpec) {
        int result;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = width;
        } else {
            result = Math.min(width, defaultWidth);
        }
        return result;
    }

    private int measureHeight(int defaultHeight, int heightMeasureSpec) {
        int result;
        int width = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = width;
        } else {
            result = Math.min(width, defaultHeight);
        }
        return result;
    }

    /**
     * 设置三条波浪线的渐变颜色
     */
    private void initPaintShader() {
        amplitude = mHeight / 2 - wavePaint1.getStrokeWidth();
        float[] positions = {0.1f, 0.3f, 0.95f};
        int[] colors = {Color.parseColor("#ff0000"), Color.parseColor("#ff8400"), Color.parseColor("#E64fc6be")};
        int[] colors1 = {Color.parseColor("#80ffa15f"), Color.parseColor("#ff8400"), Color.parseColor("#80ffa15f")};
        /**
         * float x0:渐变起始点x坐标
         * float y0:渐变起始点y坐标
         * float x1:渐变结束点x坐标
         * float y1:渐变结束点y坐标
         * int[] colors:颜色 的int 数组
         * float[] positions: 相对位置的颜色数组，可为null，若为null，颜色沿渐变线均匀分布
         * Shader.TileMode tile: 渲染器平铺模式
         */
        LinearGradient gradient = new LinearGradient(0, 0, mWidth, mHeight, colors, positions, Shader.TileMode.CLAMP);
        LinearGradient gradient1 = new LinearGradient(0, 0, mWidth, mHeight, colors1, null, Shader.TileMode.CLAMP);
        wavePaint1.setShader(gradient);
        wavePaint2.setShader(gradient1);
        wavePaint3.setColor(Color.parseColor("#000000"));

        //设置画笔遮罩滤镜(阴影背景效果),传入度数和样式
        float blurMaskRadius = DpToPxUtils.dp2px(20);
        wavePaint1.setMaskFilter(new BlurMaskFilter(blurMaskRadius, BlurMaskFilter.Blur.NORMAL));
        wavePaint2.setMaskFilter(new BlurMaskFilter(blurMaskRadius, BlurMaskFilter.Blur.OUTER));
        wavePaint3.setMaskFilter(new BlurMaskFilter(blurMaskRadius, BlurMaskFilter.Blur.SOLID));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mHeight <= 0 || mWidth <= 0) {
            return;
        }
        wavePath1.reset();
        wavePath2.reset();
        wavePath3.reset();
        // 振幅  amplitude1 > amplitude2 > amplitude3
        float tempAmplitude = amplitude;
        wavePath1.moveTo(0, (float) (Math.sin(offsetAngle1) * tempAmplitude + mHeight / 2));
        for (float i = 0; i < mWidth; i++) {
            float angle = (float) (i / mWidth * Math.PI * 4) + offsetAngle1;
            float y = (float) (Math.sin(angle) * tempAmplitude + mHeight / 2);
            wavePath1.lineTo(i, y);
        }

        tempAmplitude = amplitude - Math.min(DpToPxUtils.dp2px(5), mHeight / 5);
        wavePath2.moveTo(0, (float) (Math.sin(offsetAngle2) * tempAmplitude + mHeight / 2));
        for (float i = 0; i < mWidth; i++) {
            float angle = (float) (i / mWidth * Math.PI * 4) + offsetAngle2;
            float y = (float) (Math.sin(angle) * tempAmplitude + mHeight / 2);
            wavePath2.lineTo(i, y);
        }

        tempAmplitude = amplitude - 2 * Math.min(DpToPxUtils.dp2px(5), mHeight / 5);
        wavePath3.moveTo(0, (float) (Math.sin(offsetAngle3) * tempAmplitude + mHeight / 2));
        for (float i = 0; i < mWidth; i++) {
            float angle = (float) (i / mWidth * Math.PI * 4) + offsetAngle3;
            float y = (float) (Math.sin(angle) * tempAmplitude + mHeight / 2);
            wavePath3.lineTo(i, y);
        }

        // 变换初始点使其波形图有波动的效果
        if (isRunning) {
            offsetAngle1 -= Math.PI * 0.05;
            offsetAngle2 -= Math.PI * 0.04;
            offsetAngle3 -= Math.PI * 0.03;
            invalidate();
        }

        canvas.drawPath(wavePath1, wavePaint1);
        canvas.drawPath(wavePath2, wavePaint2);
        canvas.drawPath(wavePath3, wavePaint3);
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            invalidate();
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
        }
    }

}
