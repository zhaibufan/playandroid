package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.log.LogUtils;

/**
 * 带有下拉刷新的ViewGroup且下拉刷新的效果不是自定义动画
 */
public class PullScrollView2 extends RelativeLayout {

    private static final String TAG = "PullScrollView";
    private TextView tvRefreshDesc; //不同状态的文字描述
    private ImageView ivPullRefrsh; //下拉刷新的图片
    private ProgressBar pbBar; //正在刷新的进度条

    public PullScrollView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public PullScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullScrollView2(Context context) {
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

        tvRefreshDesc = bottomView.findViewById(R.id.tv_desc);
        ivPullRefrsh =  bottomView.findViewById(R.id.iv_pull_refresh2);
        pbBar = bottomView.findViewById(R.id.pb_bar);
    }

    private int startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getScrollY() < 0) {
            return true;
        }
        switch (ev.getAction()) {
            // 由于事件拦截的原因在onTouchEvent回调中获取不到startY的值，故在此获取
            case MotionEvent.ACTION_DOWN:
                pbBar.setVisibility(GONE);
                ivPullRefrsh.setVisibility(VISIBLE);
                startY = (int) ev.getY();
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
                int moveY = (int) event.getY();
                int delayY = (moveY - startY);
                if (Math.abs(getScrollY()) > bottomHeight) {
                    tvRefreshDesc.setText("释放立即刷新");
                    ivPullRefrsh.setRotationX(180);
                } else {
                    tvRefreshDesc.setText("下拉刷新");
                    ivPullRefrsh.setRotationX(360);
                }
                if (getTopPosition() && getScrollY() <= 0) {
                    pullMove((int) (-delayY * 0.5));
                }
                startY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                int scrollY = getScrollY();
                if (state == PullState.ON_REFRESH && scrollY < 0 && Math.abs(scrollY) > bottomHeight) { //已经处于刷新状态
                    LogUtils.e(TAG, "已经处于刷新状态");
                    restView(-getScrollY() - bottomHeight);
                    return true;
                } else if (state == PullState.ON_REFRESH && scrollY < 0 && Math.abs(scrollY) < bottomHeight) { //处于刷新状态且已经刷新完毕
                    LogUtils.e(TAG, "处于刷新状态且已经刷新完毕");
                    return true;
                }
                if (scrollY < 0 && Math.abs(scrollY) < bottomHeight) { //未下拉到刷新位置
                    LogUtils.e(TAG, "未下拉到刷新位置");
                    returnView();
                } else if (scrollY < 0 && Math.abs(scrollY) > bottomHeight && state != PullState.ON_REFRESH) { //释放刷新
                    if (onreListener != null) {
                        state = PullState.ON_REFRESH;
                        onreListener.refresh();
                        startAnimal();
                    }
                    restView(-getScrollY() - bottomHeight);
                    pbBar.setVisibility(VISIBLE);
                    ivPullRefrsh.setVisibility(GONE);
                    tvRefreshDesc.setText("正在刷新中");
                }
                break;
        }
        return true;
    }

    private PullState state = PullState.REST;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) { //判断当前滑动是否结束
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 重置到初始状态
     */
    private void returnView() {
        restView(-getScrollY());
    }

    /**
     * 滑动相应距离
     *
     * @param dy
     */
    private void restView(int dy) {
        mScroller.startScroll(0, getScrollY(), 0, dy, 340);
        postInvalidate();
    }

    /**
     * 随着下拉位置滑动相应距离
     *
     * @param delay
     */
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
        tvRefreshDesc.setText("下拉刷新");
        ivPullRefrsh.setRotationX(360);
        pbBar.setVisibility(VISIBLE);
        ivPullRefrsh.setVisibility(GONE);
        state = PullState.REST;
        returnView();
    }

    public void startRefresh() {
        if (onreListener != null) {
            restView(-bottomHeight);
            pbBar.setVisibility(VISIBLE);
            ivPullRefrsh.setVisibility(GONE);
            startAnimal();
            onreListener.refresh();
        }
    }

    private void startAnimal() {
    }

    private void stopRefreshAnimal() {
    }

    private onRefreshListener onreListener;

    public void setOnRefreshListener(onRefreshListener onreListener) {
        this.onreListener = onreListener;
    }

    public interface onRefreshListener {
        public void refresh();
    }
}
