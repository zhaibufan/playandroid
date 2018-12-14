package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.study.zhai.playandroid.R;

import static android.view.View.MeasureSpec.getMode;

public class DownloadCircleProgressBar extends View {

    private static final String TAG = "CircleProgressBar";
    private Context mContext;
    private int defaultHeight = 100;
    private int defaultWidth = 100;
    private Paint textPaint;
    private Paint bgPaint;
    private Paint pbPaint;
    private int mWidth, mHeight;
    private final int mCircleLineStrokeWidth = 15;
    private RectF mRectF;
    private int mProgress = 0;

    public DownloadCircleProgressBar(Context context) {
        this(context, null);
    }

    public DownloadCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DownloadCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setColor(mContext.getResources().getColor(R.color.text_color));
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);

        bgPaint = new Paint();
        bgPaint.setColor(mContext.getResources().getColor(R.color.text_grey));
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(mCircleLineStrokeWidth);

        pbPaint = new Paint();
        pbPaint.setColor(mContext.getResources().getColor(R.color.colorAccent));
        pbPaint.setAntiAlias(true);
        pbPaint.setStyle(Paint.Style.STROKE);
        pbPaint.setStrokeWidth(mCircleLineStrokeWidth);

        mRectF = new RectF();
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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = this.getWidth();
        mHeight = this.getHeight();
        if (mHeight != mWidth) {
            mHeight = mWidth = Math.min(mHeight, mWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.left = mCircleLineStrokeWidth; // 左上角x
        mRectF.top = mCircleLineStrokeWidth; // 左上角y
        mRectF.right = mWidth - mCircleLineStrokeWidth; // 左下角x
        mRectF.bottom = mHeight - mCircleLineStrokeWidth; // 右下角y
        drawBg(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawBg(Canvas canvas) {
        // -90度是12点钟方向
        canvas.drawArc(mRectF, -90, 360, false, bgPaint);
    }

    private void drawProgress(Canvas canvas) {
        float sweepAngle = 0;
        if (mProgress != 0) {
            sweepAngle = (float) (mProgress / 100.0 * 360);
        }
        Log.d(TAG, "sweepAngle = " + sweepAngle);
        canvas.drawArc(mRectF, -90, sweepAngle, false, pbPaint);
    }

    private void drawText(Canvas canvas) {
        String text = mProgress + "%";
        int textHeight = mHeight / 4;
        textPaint.setTextSize(textHeight);
        int textWidth = (int) textPaint.measureText(text, 0, text.length());
        canvas.drawText(text, mWidth / 2 - textWidth / 2, mHeight / 2 + textHeight / 2, textPaint);
    }

    public void updateProgress(int progress) {
        mProgress = progress;
        invalidate();
    }
}
