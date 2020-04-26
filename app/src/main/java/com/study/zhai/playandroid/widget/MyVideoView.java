package com.study.zhai.playandroid.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * @author zhaixiaofan
 * @date 2020-04-22 20:29
 */
public class MyVideoView extends SurfaceView {

    private static final String TAG = "MiGuAdVideoView";
    private boolean isReady = false;
    private int position = 0;//续播时间
    private Uri url = null;
    private MediaPlayer player;

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        player = new MediaPlayer();

        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceCreated");
                isReady = true;
                player.setDisplay(getHolder());
                if (!"".equals(url) && !player.isPlaying()) {
                    try {
                        player.reset();
                        player.setDataSource(context, url);
                        player.prepare();
                        player.seekTo(position);
                        Log.d(TAG, "续播时间：" + position);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                isReady = false;

                Log.d(TAG, "surfaceDestroyed");
                if (player.isPlaying()) {
                    position = player.getCurrentPosition();
                    Log.d(TAG, "当前播放时间：" + position);
                    player.stop();
                }
            }
        });
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoPath(Context context, Uri url) {
        this.url = url;
        if (isReady) {
            try {
                player.reset();
                player.setDataSource(context, url);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCurrentPosition() {
        if (player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    public void start() {
        if (player != null && !player.isPlaying()) {
            player.start();
        }
    }

    public void seekTo(int startTime) {
        if (player != null && player.isPlaying()) {
            player.seekTo(startTime);
        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        if (player != null) {
            player.setOnPreparedListener(listener);
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        if (player != null) {
            player.setOnCompletionListener(listener);
        }
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        if (player != null) {
            player.setOnErrorListener(listener);
        }
    }

    public void release() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
