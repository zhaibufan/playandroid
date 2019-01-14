package com.study.zhai.playandroid.ui.activity;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.ui.adapter.ViewPagerCommonAdapter;
import com.study.zhai.playandroid.widget.HorizontalStackTransformerWithRotation;
import com.study.zhai.playandroid.widget.ScaleTransformer;
import com.study.zhai.playandroid.widget.VerticalStackPageTransformerWithRotation;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class PagerTestActivity extends BaseActivity {

    @BindView(R.id.vp_horizontal_stack)
    ViewPager vpHorizontalStack;
    @BindView(R.id.vp_vertical_stack)
    ViewPager vpVerticalStack;
    @BindView(R.id.vp_more_pager)
    ViewPager vpMorePager;
    @BindView(R.id.vp_scale_pager)
    ViewPager vpScalePager;
    @BindView(R.id.vp_last_pager)
    ViewPager vpLastPager;

    private List<Integer> colorsRes = Arrays.asList(R.color.colorPrimary, R.color.colorPrimaryDark,
            R.color.colorPrimary, R.color.colorAccent, R.color.green, R.color.drawbrilliant,
            R.color.point);
    
    private ViewPagerCommonAdapter mHorizontalStackAdapter = new ViewPagerCommonAdapter(colorsRes) {
        @Override
        protected void renderItemView(View view, int position) {
            LinearLayout llRoot = view.findViewById(R.id.ll_root);
            TextView tvText = view.findViewById(R.id.tv_text);
            Integer itemData = (Integer) getBindItemData(position);
            llRoot.setBackgroundResource(itemData);
            tvText.setText(String.valueOf(position));
        }

        @Override
        public View createPageItemView(ViewGroup container, int position) {
            View view = LayoutInflater.from(PagerTestActivity.this).inflate(R.layout.view_pager, container, false);
            return view;
        }
    };
    private ViewPagerCommonAdapter mVerticalStackAdapter = new ViewPagerCommonAdapter(colorsRes) {
        @Override
        protected void renderItemView(View view, int position) {
            LinearLayout llRoot = view.findViewById(R.id.ll_root);
            TextView tvText = view.findViewById(R.id.tv_text);
            Integer itemData = (Integer) getBindItemData(position);
            llRoot.setBackgroundResource(itemData);
            tvText.setText(String.valueOf(position));
        }

        @Override
        public View createPageItemView(ViewGroup container, int position) {
            View view = LayoutInflater.from(PagerTestActivity.this).inflate(R.layout.view_pager, container, false);
            return view;
        }
    };

    private ViewPagerCommonAdapter mMorePagerAdapter = new ViewPagerCommonAdapter(colorsRes) {
        @Override
        protected void renderItemView(View view, int position) {
            LinearLayout llRoot = view.findViewById(R.id.ll_root);
            TextView tvText = view.findViewById(R.id.tv_text);
            Integer itemData = (Integer) getBindItemData(position);
            llRoot.setBackgroundResource(itemData);
            tvText.setText(String.valueOf(position));
        }

        @Override
        public View createPageItemView(ViewGroup container, int position) {
            View view = LayoutInflater.from(PagerTestActivity.this).inflate(R.layout.view_pager, container, false);
            return view;
        }
    };

    private ViewPagerCommonAdapter mScalePagerAdapter = new ViewPagerCommonAdapter(colorsRes) {
        @Override
        protected void renderItemView(View view, int position) {
            LinearLayout llRoot = view.findViewById(R.id.ll_root);
            TextView tvText = view.findViewById(R.id.tv_text);
            Integer itemData = (Integer) getBindItemData(position);
            llRoot.setBackgroundResource(itemData);
            tvText.setText(String.valueOf(position));
        }

        @Override
        public View createPageItemView(ViewGroup container, int position) {
            View view = LayoutInflater.from(PagerTestActivity.this).inflate(R.layout.view_pager, container, false);
            return view;
        }
    };

    private ViewPagerCommonAdapter mLastAdapter = new ViewPagerCommonAdapter(colorsRes) {
        @Override
        protected void renderItemView(View view, int position) {
            LinearLayout llRoot = view.findViewById(R.id.ll_root);
            TextView tvText = view.findViewById(R.id.tv_text);
            Integer itemData = (Integer) getBindItemData(position);
            llRoot.setBackgroundResource(itemData);
            tvText.setText(String.valueOf(position));
        }

        @Override
        public View createPageItemView(ViewGroup container, int position) {
            View view = LayoutInflater.from(PagerTestActivity.this).inflate(R.layout.view_pager, container, false);
            return view;
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_pager;
    }

    @Override
    public void initView() {
        vpHorizontalStack.setOffscreenPageLimit(3);
        vpHorizontalStack.setPageTransformer(false, new HorizontalStackTransformerWithRotation(vpHorizontalStack));
        vpHorizontalStack.setAdapter(mHorizontalStackAdapter);
        vpHorizontalStack.setCurrentItem(3);

        vpVerticalStack.setOffscreenPageLimit(3);
        vpVerticalStack.setPageTransformer(false, new VerticalStackPageTransformerWithRotation(vpVerticalStack));
        vpVerticalStack.setAdapter(mVerticalStackAdapter);
        vpVerticalStack.setCurrentItem(3);

        //ViewPager的Item之间的间隙
        vpMorePager.setPageMargin(80);
        vpMorePager.setOffscreenPageLimit(3);
        vpMorePager.setAdapter(mMorePagerAdapter);

        vpScalePager.setOffscreenPageLimit(3);
        vpScalePager.setPageTransformer(false, new ScaleTransformer());
        vpScalePager.setAdapter(mScalePagerAdapter);
        vpScalePager.setCurrentItem(3);

        vpMorePager.setPageMargin(80);
        vpLastPager.setAdapter(mLastAdapter);
    }

    @Override
    public void initData() {
    }
}
