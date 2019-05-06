package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.log.LogUtils;


/**
 * 带有下拉刷新的ViewGroup且下拉刷新的效果是自定义的动画效果
 */
public class PullScrollView extends RelativeLayout {

    private static final String TAG = "PullScrollView";
    private ImageView ivRefreshing; //正在刷新的图片
    private ImageView ivPullRefrsh; //下拉刷新的图片
    private AnimationDrawable animationDrawable;
    private int startPullY;

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullScrollView(Context context) {
        super(context);
        init(context);
    }

    private Scroller mScroller;
    private int mTouchSlop;

    private void init(Context context) {
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mScroller = new Scroller(context, new DecelerateInterpolator());
    }


    private ViewGroup bottomView;
    private LinearLayout contentView;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getTopPosition();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 2) {
            throw new RuntimeException("子孩子只能有两个");
        }
        bottomView = (ViewGroup) getChildAt(0); //下拉刷新的部分
        contentView = (LinearLayout) getChildAt(1); //界面正常显示的部分

        ivRefreshing = (ImageView) bottomView.findViewById(R.id.iv_refresh);
        ivPullRefrsh = (ImageView) bottomView.findViewById(R.id.iv_pull_refresh);
    }

    private int startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getScrollY() < 0) {
            LogUtils.d(TAG, "return");
            return true;
        }
        switch (ev.getAction()) {
            // 由于事件拦截的原因在onTouchEvent回调中获取不到startY的值，故在此获取
            case MotionEvent.ACTION_DOWN:
                ivPullRefrsh.setVisibility(VISIBLE);
                startY = (int) ev.getY();
                startPullY = startY;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int delayY = moveY - startY;
                // 当手指拖动值大于mTouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (getTopPosition() && delayY > mTouchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                ivRefreshing.setVisibility(GONE);
                int delayY = (int) (event.getY() - startY);
                if (getTopPosition() && getScrollY() <= 0) {
                    pullMove((int) (-delayY * 0.5));
                }
                startY = (int) event.getY();
                float scale = (event.getY() - startPullY) / (float) (bottomHeight) * 0.7f;
                if (scale > 1.2) {
                    scale = 1.2f;
                }
                ivPullRefrsh.setScaleX(scale);
                ivPullRefrsh.setScaleY(scale);
                return true;
            case MotionEvent.ACTION_UP:
                int scrollY = getScrollY();
                LogUtils.d(TAG, "scrollY="+scrollY);
                if (state == PullState.ON_REFRESH && scrollY < 0 && Math.abs(scrollY) > bottomHeight) {
                    restView(-getScrollY() - bottomHeight);
                    return true;
                } else if (state == PullState.ON_REFRESH && scrollY < 0 && Math.abs(scrollY) < bottomHeight) {
                    return true;
                }
                if (scrollY < 0 && Math.abs(scrollY) < bottomHeight) {
                    returnView();
                } else if (scrollY < 0 && Math.abs(scrollY) > bottomHeight && state != PullState.ON_REFRESH) {
                    if (onreListener != null) {
                        state = PullState.ON_REFRESH;
                        onreListener.refresh();
                        ivRefreshing.setVisibility(VISIBLE);
                        ivPullRefrsh.setVisibility(GONE);
                        startAnimal();
                    }
                    restView(-getScrollY() - bottomHeight);
                }
                break;
        }
        return true;
    }

    private void startAnimal() {
        ivRefreshing.setImageResource(R.drawable.clife_refresh_loading);
        animationDrawable = (AnimationDrawable) ivRefreshing.getDrawable();
        animationDrawable.start();
    }

    private void stopRefreshAnimal() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }

    private PullState state = PullState.REST;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void returnView() {
        restView(-getScrollY());
    }


    private void restView(int dy) {
        mScroller.startScroll(0, getScrollY(), 0, dy, 340);
        postInvalidate();
    }


    private void pullMove(int delay) {
        if (getScrollY() <= 0 && (getScrollY() + delay) <= 0) {
            scrollBy(0, delay);
        } else {
            scrollTo(0, 0);
        }
    }

    private boolean getTopPosition() {
        if (contentView.getScrollY() <= 0) {
            return true;
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.d(TAG, "onLayout");
        bottomHeight = getBottomViewHeight();
        bottomView.layout(l, -bottomHeight, r, t);
        contentView.layout(l, 0, r, b);
    }

    private int bottomHeight = 0;

    private int getBottomViewHeight() {
        return bottomView.getMeasuredHeight();
    }


    enum PullState {
        REST, ON_REFRESH
    }

    public void stopRefresh() {
        stopRefreshAnimal();
        state = PullState.REST;
        returnView();
    }

    public void startRefresh() {
        if (onreListener != null) {
            restView(-bottomHeight);
            ivPullRefrsh.setVisibility(GONE);
            startAnimal();
            onreListener.refresh();
        }
    }

    private onRefreshListener onreListener;

    public void setOnRefreshListener(onRefreshListener onreListener) {
        this.onreListener = onreListener;
    }

    public interface onRefreshListener {
        public void refresh();
    }
}
