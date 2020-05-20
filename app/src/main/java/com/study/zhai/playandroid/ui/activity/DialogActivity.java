package com.study.zhai.playandroid.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;

/**
 * @author zhaixiaofan
 * @date 2020-05-08 20:30
 */
public class DialogActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    public void initView() {
        Button btn = findViewById(R.id.btn_1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "hahha", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void initData() {

    }
}
