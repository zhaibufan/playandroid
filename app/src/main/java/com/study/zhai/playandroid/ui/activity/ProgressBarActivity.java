package com.study.zhai.playandroid.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.widget.TouchProgressBar;

public class ProgressBarActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_progress_bar;
    }

    @Override
    public void initView() {
        TouchProgressBar touchProgressBar = findViewById(R.id.tpb_bar);
        final TextView tv = findViewById(R.id.tv_progress);
        touchProgressBar.setOnProgressChangedListener(new TouchProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(View view, int progress) {
                tv.setText(progress+"%");
            }
        });
        touchProgressBar.setFollowView(tv);
    }

    @Override
    public void initData() {

    }
}
