package com.study.zhai.playandroid.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.ui.adapter.MyAdapter;
import com.study.zhai.playandroid.ui.adapter.MyAdapter2;
import com.zhouyou.recyclerview.adapter.HelperRecyclerViewHolder;
import com.zhouyou.recyclerview.swipemenu.SwipeMenuRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author zhaixiaofan
 * @date 2019/6/21 11:10 PM
 */
public class RecyclerActivity extends BaseActivity {

    @BindView(R.id.rv)
    SwipeMenuRecyclerView superSwipeMenuRecyclerView;
    private SwipeMenuRecyclerView footerRv;

    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<String> footerData = new ArrayList<>();

    private MyAdapter myAdapter;
    private MyAdapter2 myAdapter2;
    private View footerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recycler;
    }


    @Override
    public void initView() {

        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");
        footerData.add("1");

        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view, (ViewGroup) superSwipeMenuRecyclerView.getRootView(), false);
        footerRv = footerView.findViewById(R.id.rv_footer);
        initRv();
        initFoot();
    }

    private void initFoot() {
        myAdapter2 = new MyAdapter2(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        footerRv.setLayoutManager(layoutManager);
        footerRv.setPullRefreshEnabled(false);
        footerRv.setLoadingMoreEnabled(false);
        footerRv.setSwipeDirection(SwipeMenuRecyclerView.DIRECTION_LEFT);//左滑（默认）
        footerRv.setAdapter(myAdapter2);

        myAdapter2.setListAll(footerData);
    }


    private void initRv() {
        myAdapter = new MyAdapter(this, new MyAdapter.OnCiickLister() {
            @Override
            public void onChange(int po, HelperRecyclerViewHolder viewHolder) {
                TextView tv = viewHolder.getView(R.id.tv_data);
                tv.setText("hahah");
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        superSwipeMenuRecyclerView.setLayoutManager(layoutManager);
        superSwipeMenuRecyclerView.setPullRefreshEnabled(true);
        superSwipeMenuRecyclerView.setLoadingMoreEnabled(false);
        superSwipeMenuRecyclerView.addFooterView(footerView);
        superSwipeMenuRecyclerView.setItemViewCacheSize(30);
        superSwipeMenuRecyclerView.setSwipeDirection(SwipeMenuRecyclerView.DIRECTION_LEFT);//左滑（默认）
        superSwipeMenuRecyclerView.setAdapter(myAdapter);

        myAdapter.setListAll(data);
    }

    @Override
    public void initData() {

    }

}
