package com.study.zhai.playandroid.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.utils.ApplicationUtils;
import com.study.zhai.playandroid.utils.DpToPxUtils;
import com.study.zhai.playandroid.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhaixiaofan
 * @date 2019/1/19 12:36 PM
 */
public class BallsFallDownActivity extends BaseActivity {

    @BindView(R.id.view_free_fall)
     View freeFallView;
    @BindView(R.id.view_horizontal_projectile_motion)
     View horizontalProjectileMotionView;

    // 模拟自由落体
    private ObjectAnimator freeFallAnimator;
    // 模拟平抛1
    private ValueAnimator horizontalProjectileAnimator1;
    // 模拟平抛2
    private ObjectAnimator horizontalProjectileAnimator2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ball_fall;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            initAnimation();
        }
    }

    private void initAnimation() {
        final int verticalDistance = (int) (ApplicationUtils.getScreenHeight() - freeFallView.getBottom() - StatusBarUtils.getStatusBarHeight(this) - DpToPxUtils.dp2px(44));
        int horizontalDistance = freeFallView.getLeft() - horizontalProjectileMotionView.getRight();
        // 自由落体，动画系统利用反射直接修改view平移属性
        freeFallAnimator = ObjectAnimator.ofFloat(freeFallView, "translationY", 0, verticalDistance);
        // 自由落体动画设置方式1
        freeFallAnimator.setInterpolator(new LinearInterpolator());
        freeFallAnimator.setEvaluator(new TypeEvaluator<Float>() {
            /**
             * 设置该动画属性的值
             *
             * @param fraction 0-1 它的值取决于Interpolator的类型 也可以animation.getAnimatedFraction()获取
             * @param startValue
             * @param endValue
             * @return
             */
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return fraction * fraction * (endValue - startValue) + startValue;
            }
        });
        // 自由落体动画设置方式2
//        freeFallAnimator.setInterpolator(new AccelerateInterpolator());
        // 自由落体动画设置方式3
//        freeFallAnimator.setInterpolator(new TimeInterpolator() {
//            @Override
//            public float getInterpolation(float input) {
//                // 效果等同于freeFallAnimator.setInterpolator(new AccelerateInterpolator());
//                return input * input;
//            }
//        });

        // 平抛1，需手动修改平移属性
        horizontalProjectileAnimator1 = ValueAnimator.ofObject(new MyTypeEvaluator(), new Point(0, 0), new Point(horizontalDistance, verticalDistance));
        horizontalProjectileAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                horizontalProjectileMotionView.setTranslationX(point.getPointX());
                horizontalProjectileMotionView.setTranslationY(point.getPointY());
            }
        });
        horizontalProjectileAnimator1.setInterpolator(new LinearInterpolator());

        // 平抛2，动画系统通过反射直接修改view平移属性
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0, horizontalDistance);
        pvhX.setEvaluator(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return fraction * (endValue - startValue) + startValue;
            }
        });
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 0, verticalDistance);
        pvhY.setEvaluator(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return fraction * fraction * (endValue - startValue) + startValue;
            }
        });
        horizontalProjectileAnimator2 = ObjectAnimator.ofPropertyValuesHolder(horizontalProjectileMotionView, pvhX, pvhY);
        horizontalProjectileAnimator2.setInterpolator(new LinearInterpolator());
    }

    private void runAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(freeFallAnimator).with(horizontalProjectileAnimator);
//        animatorSet.playTogether(freeFallAnimator, horizontalProjectileAnimator1);
//        animatorSet.playTogether(freeFallAnimator, horizontalProjectileAnimator1);
        animatorSet.playTogether(freeFallAnimator, horizontalProjectileAnimator2);

        animatorSet.setDuration(1500);
        animatorSet.start();
    }

    private void reset() {
        freeFallView.setTranslationY(0);
        horizontalProjectileMotionView.setTranslationX(0);
        horizontalProjectileMotionView.setTranslationY(0);
    }

    @OnClick({R.id.tv_reset, R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset:
                reset();
                break;
            case R.id.tv_start:
                runAnimation();
                break;
        }
    }

    // 描述小球运动轨迹坐标
    private class Point {
        private float pointX;
        private float pointY;

        private Point(float pointX, float pointY) {
            this.pointX = pointX;
            this.pointY = pointY;
        }

        private float getPointX() {
            return pointX;
        }

        private void setPointX(float pointX) {
            this.pointX = pointX;
        }

        private float getPointY() {
            return pointY;
        }

        private void setPointY(float pointY) {
            this.pointY = pointY;
        }
    }

    // 模拟平抛运动轨迹的估值器
    private class MyTypeEvaluator implements TypeEvaluator<Point> {
        /**
         * @param fraction   从开始值到结束值的分数
         * @param startValue 开始值
         * @param endValue   结束值
         * @return
         */
        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            float pointX = fraction * (endValue.getPointX() - startValue.getPointY()) + startValue.getPointX();
            float pointY = fraction * fraction * (endValue.getPointY() - startValue.getPointY()) + startValue.getPointY();
            return new Point(pointX, pointY);
        }
    }

}
