package com.study.zhai.playandroid.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.study.zhai.playandroid.interfaces.OnViewPagerListener
import com.study.zhai.playandroid.log.LogUtils


/**
 * @author zhaixiaofan
 * @date 2020-03-27 20:24
 */
class BannerLayoutManager : LinearLayoutManager, RecyclerView.OnChildAttachStateChangeListener {


    private val mSnapHelper by lazy { PagerSnapHelper() }
    private lateinit var mListener : OnViewPagerListener
    private var mDrift: Int = 0

    constructor(context: Context) : super(context)

    fun setOnViewPagerListener(listener: OnViewPagerListener) {
        mListener = listener
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        view?.addOnChildAttachStateChangeListener(this)
        mSnapHelper.attachToRecyclerView(view)
        super.onAttachedToWindow(view)
        LogUtils.d("BannerLayoutManager", "onAttachedToWindow")
    }

    override fun onScrollStateChanged(state: Int) {
        LogUtils.d("BannerLayoutManager", "onScrollStateChanged")
        when(state) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                LogUtils.d("BannerLayoutManager", "onScrollStateChanged SCROLL_STATE_IDLE")
                val snapView = mSnapHelper.findSnapView(this)
                if (snapView != null) {
                    val position = getPosition(snapView)
                    mListener.onPageSelected(position)
                }
            }
        }

        super.onScrollStateChanged(state)
    }

    override fun onChildViewDetachedFromWindow(p0: View) {
        val position = getPosition(p0)
        mListener.onPageRelease(position)
        LogUtils.d("BannerLayoutManager", "onChildViewDetachedFromWindow")
    }

    override fun onChildViewAttachedToWindow(p0: View) {
        val position = getPosition(p0)
        mListener.onPageSelected(position)
        LogUtils.d("BannerLayoutManager", "onChildViewAttachedToWindow")
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        this.mDrift = dx
        return super.scrollHorizontallyBy(dx, recycler, state)
    }
}