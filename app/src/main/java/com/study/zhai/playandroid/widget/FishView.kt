package com.study.zhai.playandroid.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.study.zhai.playandroid.log.LogUtils
import kotlin.math.min

/**
 * @author zhaixiaofan
 * @date 2020-04-07 20:23
 */

const val TAG = "FishView"
class FishView : View {

    private val defauleHeight = 200
    private val defaultWidth = 500
    private var mWidth: Float = 0.toFloat()
    private var mHeight:Float = 0.toFloat()

    private val bgPaint by lazy {
        Paint()
    }

    private val riverPaint by lazy {
        Paint()
    }

    private val riverPaint2 by lazy {
        Paint()
    }

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    private fun initPaint() {
        bgPaint.run {
            this.isAntiAlias = true
            this.color = Color.BLUE
            this.style = Paint.Style.FILL
        }

        riverPaint.run {
            this.isAntiAlias = true
            this.color = Color.RED
            this.style = Paint.Style.STROKE
            this.strokeWidth = 80f
        }

        riverPaint2.run {
            this.isAntiAlias = true
            this.color = Color.GREEN
            this.style = Paint.Style.STROKE
            this.strokeWidth = 80f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(getMeasureWidth(widthMeasureSpec), getMeasureHeight(heightMeasureSpec))
    }

    private fun getMeasureHeight(heightMeasureSpec: Int) : Int {
        var height : Int
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)
        height = if (mode == MeasureSpec.EXACTLY) {
            size
        } else {
            min(size, defauleHeight)
        }
        return height
    }

    private fun getMeasureWidth(widthMeasureSpec: Int): Int {
        val width: Int
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        width = if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            specSize.coerceAtMost(defaultWidth)
        }
        return width
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mHeight = height.toFloat()
        mWidth = width.toFloat()
        LogUtils.d(TAG, "mWidth = $mWidth  mHeight = $mHeight")
        val rectF = RectF(0.0f, 0.0f, mWidth, mHeight)
        canvas?.drawRect(rectF, bgPaint)

        val endX = mWidth/4
        val startX = mWidth - mWidth/4
        canvas?.drawLine(startX, 0f, startX, (mHeight/2-150/2), riverPaint)
        val rectF1 = RectF((mWidth - mWidth/4 - 150),((mHeight/2) - 150), mWidth - mWidth/4, (mHeight/2))
        canvas?.drawArc(rectF1, 0f, 90f, false, riverPaint2)
        canvas?.drawLine(startX - 150/2, mHeight/2, endX+150/2, mHeight/2, riverPaint)
        val rectF2 = RectF(endX,((mHeight/2)), endX + 150, ((mHeight/2) + 150))
        canvas?.drawArc(rectF2, 180f, 90f, false, riverPaint2)
        canvas?.drawLine(endX, (mHeight/2-150/2+150), endX, mHeight, riverPaint)
    }
}