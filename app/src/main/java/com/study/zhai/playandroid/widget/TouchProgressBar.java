package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.util.LogUtils;

public class TouchProgressBar extends View {

    private static final String TAG = "TouchProgressBar";
    private static final int DEFAULT_PROGRESS_BG_COLOR = Color.parseColor("#CCCCCC"); //默认的进度条背景颜色
    private static final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#4097EA"); //默认的进度条颜色
    private static final int DEFAULT_POINT_COLOR = Color.parseColor("#4097EA"); //默认的触摸点颜色
    private static final int DEFAULT_START_COLOR = Color.parseColor("#EFF7FF"); //默认的渐变开始颜色
    private static final int DEFAULT_END_COLOR = Color.parseColor("#4097EA"); //默认的渐变结束颜色
    private static final int DEFAULT_POINT_RADIUS = 20; //默认的触摸点半径
    private static final int DEFAULT_PROGRESS = 50; //默认的进度
    private final int PROGRESS_MIN = 0;
    private final int PROGRESS_MAX = 100;
    private int mProgressBgColor;
    private int mProgressColor;
    private int mPointColor;
    private int mStartColor, mEndColor; //渐变的起始和结束点的颜色
    private int mPointRadius; //触摸点的半径
    private int mProgress; //当前进度
    private boolean isGradient; //进度条的颜色是否渐变
    private View mFollowView; //跟随触摸点一起移动的view

    private Paint bgProgressPaint; //进度背景的画笔
    private Paint progressPaint; //进度的画笔
    private Paint pointPaint; //触摸点的画笔

    private int mHeight, mWidth;

    private OnProgressChangedListener progressChangedListener;

    public interface OnProgressChangedListener {
        void onProgressChanged(View view, int progress);
    }

    public TouchProgressBar(Context context) {
        this(context, null);
    }

    public TouchProgressBar(Context context,AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TouchProgressBar(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TouchProgressBar);
        mProgressBgColor = ta.getColor(R.styleable.TouchProgressBar_progressBgColor, DEFAULT_PROGRESS_BG_COLOR);
        mProgressColor = ta.getColor(R.styleable.TouchProgressBar_progressColor, DEFAULT_PROGRESS_COLOR);
        mPointColor = ta.getColor(R.styleable.TouchProgressBar_pointColor, DEFAULT_POINT_COLOR);
        mStartColor = ta.getColor(R.styleable.TouchProgressBar_startColor, DEFAULT_START_COLOR);
        mEndColor = ta.getColor(R.styleable.TouchProgressBar_endColor, DEFAULT_END_COLOR);
        mPointRadius = ta.getInteger(R.styleable.TouchProgressBar_pointRadius, DEFAULT_POINT_RADIUS);
        mProgress = ta.getInteger(R.styleable.TouchProgressBar_progress, DEFAULT_PROGRESS);
        isGradient = ta.getBoolean(R.styleable.TouchProgressBar_isGradient, true);
        ta.recycle();
        init();
    }

    private void init() {
        bgProgressPaint = new Paint();
        bgProgressPaint.setAntiAlias(true);
        bgProgressPaint.setStyle(Paint.Style.FILL);
        bgProgressPaint.setColor(mProgressBgColor);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(mPointColor);
    }

    /**
     * 设置进度变化监听器
     *
     * @param onProgressChangedListener
     */
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.progressChangedListener = onProgressChangedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() < mPointRadius) {
            setProgress(PROGRESS_MIN);
            return true;
        } else if (event.getX() > mWidth - mPointRadius) {
            setProgress(PROGRESS_MAX);
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setProgress(calculateProgress(event.getX()));
                    return true;
                case MotionEvent.ACTION_MOVE:
                    setProgress(calculateProgress(event.getX()));
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHeight = getHeight();
        mWidth = getWidth();

        // 绘制底部的背景线
        RectF bgRectF = new RectF(0, mHeight/4, mWidth, 3*mHeight/4);
        canvas.drawRoundRect(bgRectF, mHeight/ 2 , mHeight/ 2, bgProgressPaint);
        // 绘制进度线
        RectF rectF = new RectF(0, mHeight/4, mProgress/100f*mWidth, 3*mHeight/4);
        if (isGradient) {
            LinearGradient linearGradient = new LinearGradient(0, 0, mWidth, mHeight, mStartColor, mEndColor, Shader.TileMode.MIRROR);
            progressPaint.setShader(linearGradient);
        } else {
            progressPaint.setColor(mProgressColor);
        }
        canvas.drawRoundRect(rectF, mHeight/ 2 , mHeight/ 2, progressPaint);
        // 绘制点
        canvas.drawCircle(getCx(), mHeight / 2, mPointRadius, pointPaint);
        // 移动跟随进度点的View 使之与进度点保持一致
        float cx = getCx();
        if (mFollowView != null) {
            mFollowView.setTranslationX(cx);
        }
    }

    /**
     * 设置百分比
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("progress 不可以小于0 或大于100");
        }
        this.mProgress = progress;
        invalidate();

        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(this, progress);
        }
    }

    /**
     * 设置圆点半径
     *
     * @param radius
     */
    public void setPointRadius(final int radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("radius 不可以小于等于0");
        }

        if (mWidth == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (radius * 2 > mWidth) {
                        throw new IllegalArgumentException("radius*2 必须小于 view.mWidth == " +mWidth);
                    }
                    mPointRadius = radius;
                }
            });
        } else {
            if (radius * 2 > mWidth) {
                throw new IllegalArgumentException("radius*2 必须小于 view.mWidth == " + mWidth);
            }
            this.mPointRadius = radius;
        }
    }

    /**
     * 设置跟随进度点移动的View
     *
     * @param view
     */
    public void setFollowView(View view) {
        mFollowView = view;
    }

    /**
     * 获取圆点的x轴坐标
     *
     * @return
     */
    private float getCx() {
        float cx;
        cx = (mWidth - mPointRadius * 2);
        if (cx < 0) {
            throw new IllegalArgumentException("TouchProgressView 宽度不可以小于 2 倍 pointRadius");
        }
        float moveX = cx / 100 * mProgress + mPointRadius;
        if (moveX < mWidth/4 && moveX > 2*mPointRadius) { //解决滑动到靠左边那块时进度条与触摸点不能重合的bug
            return moveX - mPointRadius;
        } else {
            return moveX;
        }
    }

    /**
     * 计算触摸点的百分比
     *
     * @param eventX
     * @return
     */
    private int calculateProgress(float eventX) {
        float proResult = (eventX - mPointRadius) / (mWidth - mPointRadius * 2);
        return (int) (proResult * 100);
    }
}
