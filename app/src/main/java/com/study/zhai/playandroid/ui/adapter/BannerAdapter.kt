package com.study.zhai.playandroid.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.study.zhai.playandroid.R


/**
 * @author zhaixiaofan
 * @date 2020-03-26 21:16
 */
class BannerAdapter(val context : Context) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    private val mContext = context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(com.study.zhai.playandroid.R.layout.banner_item, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val i = p1 % 4
        p0.tvNum?.setText(i.toString())
    }


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var tvNum : TextView ?= null
        init {
            tvNum = view.findViewById<TextView>(R.id.tv_num)
        }
    }
}

