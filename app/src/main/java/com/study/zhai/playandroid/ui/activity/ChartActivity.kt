package com.study.zhai.playandroid.ui.activity

import android.support.v7.widget.LinearLayoutManager
import com.study.zhai.playandroid.R
import com.study.zhai.playandroid.base.BaseActivity
import com.study.zhai.playandroid.bean.ChartBean
import com.study.zhai.playandroid.ui.adapter.ChartAdapter
import com.zhouyou.recyclerview.XRecyclerView

class ChartActivity : BaseActivity() {

    private lateinit var rvChart : XRecyclerView
    private var list = mutableListOf<ChartBean>()

    private val mLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val mAdapter by lazy {
        ChartAdapter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_chart
    }

    override fun initView() {

        requestData()

        rvChart = findViewById<XRecyclerView>(R.id.rv_chart)
        rvChart.run {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }


        mAdapter.setListAll(list)
    }

    private fun requestData() {
        val chartBean = ChartBean(1000)
        list.add(chartBean)
        list.add(chartBean)
        list.add(chartBean)
        list.add(chartBean)
    }

    override fun initData() {
    }

}