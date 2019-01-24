package com.study.zhai.playandroid.ui.activity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.widget.AudioSignalView;
import com.study.zhai.playandroid.widget.WaveView;

import butterknife.BindView;

public class RecordingAnimalActivity extends BaseActivity {

    @BindView(R.id.wave_view)
    WaveView waveView;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.audio_signal_view_1)
    AudioSignalView audioSignalView1;
    @BindView(R.id.audio_signal_view_2)
    AudioSignalView audioSignalView2;
    @BindView(R.id.audio_signal_view_3)
    AudioSignalView audioSignalView3;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recording_animal;
    }

    @Override
    public void initView() {
        btnRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        waveView.start();
                        audioSignalView1.startAudioSignal();
                        audioSignalView2.startAudioSignal();
                        audioSignalView3.startAudioSignal();
                        break;
                    case MotionEvent.ACTION_UP:
                        waveView.stop();
                        audioSignalView1.stopAudioSignal();
                        audioSignalView2.stopAudioSignal();
                        audioSignalView3.stopAudioSignal();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
    }
}
