package com.study.zhai.playandroid.interfaces

/**
 * @author zhaixiaofan
 * @date 2020-03-27 20:43
 */
interface OnViewPagerListener {
    /*初始化完成*/
    fun onInitComplete()
    /*释放的监听*/
    fun onPageRelease(position : Int)
    /*选中的监听以及判断是否滑动到底部*/
    fun onPageSelected(position : Int)
}
