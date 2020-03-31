package com.study.zhai.playandroid.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import com.study.zhai.playandroid.base.BaseActivity
import com.study.zhai.playandroid.interfaces.OnViewPagerListener
import com.study.zhai.playandroid.ui.adapter.BannerAdapter
import com.study.zhai.playandroid.widget.BannerLayoutManager
import kotlinx.android.synthetic.main.activity_banner.*


/**
 * @author zhaixiaofan
 * @date 2020-03-26 21:14
 */
class BannerActivity : BaseActivity() {

    private val mLayoutMannger by lazy {
        BannerLayoutManager(this)
    }

    private val mAdapter by lazy {
        BannerAdapter(this)
    }

    var helper: PagerSnapHelper = object : PagerSnapHelper() {

        override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
            return super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        }
    }

    override fun getLayoutId() = com.study.zhai.playandroid.R.layout.activity_banner

    override fun initView() {
        mLayoutMannger.setOnViewPagerListener(mListener)
        rv_banner.layoutManager = mLayoutMannger
        mLayoutMannger.orientation = LinearLayoutManager.HORIZONTAL
        rv_banner.adapter = mAdapter

    }

    override fun initData() {

    }

    private val mListener : OnViewPagerListener = object : OnViewPagerListener {
        override fun onInitComplete() {
        }

        override fun onPageRelease(position: Int) {
        }

        override fun onPageSelected(position: Int) {

        }

    }

}