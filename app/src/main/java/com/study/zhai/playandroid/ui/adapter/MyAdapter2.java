package com.study.zhai.playandroid.ui.adapter;

import android.content.Context;

import com.study.zhai.playandroid.R;
import com.zhouyou.recyclerview.adapter.HelperRecyclerViewDragAdapter;
import com.zhouyou.recyclerview.adapter.HelperRecyclerViewHolder;
import com.zhouyou.recyclerview.swipemenu.SwipeMenuLayout;

/**
 * @author zhaixiaofan
 * @date 2019/6/21 11:19 PM
 */
public class MyAdapter2 extends HelperRecyclerViewDragAdapter<String> {

    public MyAdapter2(Context context) {
        super(context, R.layout.adapter_swipemenu1_layout);
    }
    @Override
    protected void HelperBindData(HelperRecyclerViewHolder viewHolder, int position, String item) {
        final SwipeMenuLayout superSwipeMenuLayout = (SwipeMenuLayout) viewHolder.itemView;
        superSwipeMenuLayout.setSwipeEnable(true);   //设置是否可以侧滑
    }
}
