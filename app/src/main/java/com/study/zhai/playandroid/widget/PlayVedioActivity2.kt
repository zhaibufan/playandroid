package com.study.zhai.playandroid.widget

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.study.zhai.playandroid.R
import kotlinx.android.synthetic.main.activity_play_vedio2.*

/**
 * @author zhaixiaofan
 * @date 2020-04-22 20:30
 */
class PlayVedioActivity2 : AppCompatActivity() {

    var mUri : Uri ?= null
    var currentPosition : Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_vedio2)
        mUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.guidvideo)
        my_video.setVideoPath(this, mUri)
    }

    fun pause(view: View) {
        my_video.start()
    }




    override fun onPause() {
        super.onPause()
        currentPosition = my_video.currentPosition
        my_video.pause()
    }

    override fun onResume() {
        super.onResume()
        if(currentPosition != null) {
            my_video.seekTo(currentPosition!!)
        } else {
            my_video.start()
        }

    }
}