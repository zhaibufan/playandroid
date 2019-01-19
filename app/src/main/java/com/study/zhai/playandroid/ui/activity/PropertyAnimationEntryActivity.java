package com.study.zhai.playandroid.ui.activity;

import android.content.Intent;
import android.view.View;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by cuiyan on 2018/5/3.
 */
public class PropertyAnimationEntryActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_property_activity_entry;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.tv_count_down, R.id.tv_balls_fall_down, R.id.tv_property_animator_basics_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_count_down:
                startActivity(new Intent(PropertyAnimationEntryActivity.this, CountDownActivity.class));
                break;
            case R.id.tv_balls_fall_down:
                startActivity(new Intent(PropertyAnimationEntryActivity.this, BallsFallDownActivity.class));

                break;
            case R.id.tv_property_animator_basics_1:
                break;
        }
    }
}
