package com.study.zhai.playandroid.ui.adapter;

import android.content.Context;
import android.view.View;

import com.study.zhai.playandroid.R;
import com.zhouyou.recyclerview.adapter.HelperRecyclerViewDragAdapter;
import com.zhouyou.recyclerview.adapter.HelperRecyclerViewHolder;
import com.zhouyou.recyclerview.swipemenu.SwipeMenuLayout;

/**
 * @author zhaixiaofan
 * @date 2019/6/21 11:19 PM
 */
public class MyAdapter extends HelperRecyclerViewDragAdapter<String> {

    private OnCiickLister mlis;

    public MyAdapter(Context context, OnCiickLister lister) {
        super(context, R.layout.adapter_swipemenu1_layout);
        mlis = lister;
    }
    @Override
    protected void HelperBindData(final HelperRecyclerViewHolder viewHolder, final int position, String item) {
        final SwipeMenuLayout superSwipeMenuLayout = (SwipeMenuLayout) viewHolder.itemView;
        superSwipeMenuLayout.setSwipeEnable(true);   //设置是否可以侧滑

        viewHolder.setOnClickListener(R.id.tv_data, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlis.onChange(position, viewHolder);
            }
        });

    }


    public interface OnCiickLister {
        void onChange(int po, HelperRecyclerViewHolder viewHolder);
    }
}
