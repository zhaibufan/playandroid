package com.study.zhai.playandroid.ui.activity;

import android.os.Handler;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.widget.PullScrollView;
import com.study.zhai.playandroid.widget.PullScrollView2;

import butterknife.BindView;

public class PullRefreshActivity extends BaseActivity {

    @BindView(R.id.pull_scroll_view)
    PullScrollView pullScrollView;

    @BindView(R.id.pull_scroll_view2)
    PullScrollView2 pullScrollView2;

    private Handler mHandler = new Handler();

    @Override
    public int getLayoutId() {
        return R.layout.activity_pull_refresh;
    }

    @Override
    public void initView() {
        pullScrollView.setOnRefreshListener(new PullScrollView.onRefreshListener() {
            @Override
            public void refresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullScrollView.stopRefresh();
                    }
                }, 3000);
            }
        });

        pullScrollView2.setOnRefreshListener(new PullScrollView2.onRefreshListener() {
            @Override
            public void refresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullScrollView2.stopRefresh();
                    }
                }, 3000);
            }
        });

        pullScrollView2.post(new Runnable() {
            @Override
            public void run() {
                pullScrollView2.startRefresh();
            }
        });
    }

    @Override
    public void initData() {
    }
}
