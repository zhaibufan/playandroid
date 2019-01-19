package com.study.zhai.playandroid.ui.activity;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.widget.CircleCountDownView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhaixiaofan
 * @date 2019/1/19 12:32 PM
 */
public class CountDownActivity extends BaseActivity {

    @BindView(R.id.view_count)
    CircleCountDownView viewCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_count_down;
    }

    @Override
    public void initView() {
        viewCount.setStartCountValue(6);
        viewCount.setAnimationInterpolator(new CircleCountDownView.AnimationInterpolator() {
            @Override
            public float getInterpolation(float inputFraction) {
                return inputFraction * inputFraction;
            }
        });
        viewCount.setCountDownListener(new CircleCountDownView.CountDownListener() {
            @Override
            public void onCountDownFinish() {

            }

            @Override
            public void restTime(long restTime) {

            }
        });
        viewCount.startCountDown();
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.tv_restart)
    public void onViewClicked() {
        viewCount.restart();
    }
}
