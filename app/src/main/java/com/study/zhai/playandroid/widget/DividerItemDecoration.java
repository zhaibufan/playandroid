package com.study.zhai.playandroid.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *  RecyclerView分割线
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;  // 分割线对应drawable
    private int lineWidth;   // 分割线的宽度
    private int margin; // 分割线距离左右的margin
    private int mOrientation;

    /**
     * 初始化分割线的参数
     *
     * @param orientation  RecyclerView的方向
     * @param dividerWidth 分割线的宽度
     * @param dividerColorRes 分割线的颜色
     * @param margin 分割线距离左右的margin
     */
    public DividerItemDecoration(int orientation, int dividerWidth, int dividerColorRes, int margin) {
        mDivider = new ColorDrawable(dividerColorRes);
        this.lineWidth = dividerWidth;
        this.margin = margin;
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + margin;
        final int right = parent.getWidth() - parent.getPaddingRight() - margin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i >= childCount - 1) {
                break;
            }
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + lineWidth;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + lineWidth;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * RecyclerView中item的偏移量
     *
     * @param outRect
     * @param itemPosition
     * @param parent
     */
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            // 相当于PaddingLeft、PaddingTop、PaddingRight、PaddingBottom
            outRect.set(0, 0, 0, lineWidth);
        } else {
            outRect.set(0, 0, lineWidth, 0);
        }
    }
}
