package com.study.zhai.playandroid.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class ViewPagerCommonAdapter<T> extends PagerAdapter {

    private List<T> mData;
    private SparseArray<View> viewCache = new SparseArray<>();

    public ViewPagerCommonAdapter(List<T> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = createItemPager(container, position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    protected View createItemPager(ViewGroup container, int position) {
        View itemView = viewCache.get(position);
        if (itemView == null) {
            itemView = createPageItemView(container, position);
            viewCache.put(position, itemView);
        }
        renderItemView(itemView, position);
        container.addView(itemView);
        return itemView;
    }

    /**
     * 获取指定item上绑定的数据
     *
     * @param position
     * @return
     */
    public T getBindItemData(int position) {
        if (mData != null && mData.size() > position && position >= 0) {
            return mData.get(position);
        }
        return null;
    }

    /**
     * 渲染ItemView 实际就是给Item中的每个VIew赋值
     *
     * @param container
     * @param position
     */
    protected abstract void renderItemView(View container, int position);

    /**
     * 创建VIewPager的每一个Item
     *
     * @see #createPageItemView(ViewGroup, int)
     */
    public abstract View createPageItemView(ViewGroup container, final int position);
}
