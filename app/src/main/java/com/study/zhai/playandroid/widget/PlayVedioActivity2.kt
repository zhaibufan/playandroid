package com.study.zhai.playandroid.widget

<<<<<<< HEAD
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
=======
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
>>>>>>> 1.video
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
<<<<<<< HEAD
        my_video.setVideoPath(this, mUri)
    }

    fun pause(view: View) {
        my_video.start()
    }


=======
        my_video.setVideoPath(mUri.toString())
        my_video.start()

        my_video.setOnPreparedListener {
            it.setOnInfoListener(object : MediaPlayer.OnInfoListener {
                override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                    if (what == MEDIA_INFO_VIDEO_RENDERING_START) {
                        my_video.setBackgroundColor(Color.TRANSPARENT)
                    }
                    return true
                }

            })
        }
    }
>>>>>>> 1.video


    override fun onPause() {
        super.onPause()
<<<<<<< HEAD
        currentPosition = my_video.currentPosition
        my_video.pause()
=======
//        my_video.stopPlayback()
>>>>>>> 1.video
    }

    override fun onResume() {
        super.onResume()
<<<<<<< HEAD
        if(currentPosition != null) {
            my_video.seekTo(currentPosition!!)
        } else {
            my_video.start()
        }

=======
//        my_video.resume()
//        my_video.start()
>>>>>>> 1.video
    }
}