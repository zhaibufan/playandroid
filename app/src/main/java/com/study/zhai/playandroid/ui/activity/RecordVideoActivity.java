package com.study.zhai.playandroid.ui.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.log.LogUtils;
import com.study.zhai.playandroid.utils.ConstantUtil;
import com.study.zhai.playandroid.utils.FileUtils;
import com.study.zhai.playandroid.utils.VideoUtil;
import com.study.zhai.playandroid.utils.ZToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

public class RecordVideoActivity extends BaseActivity implements SurfaceHolder.Callback{

    @BindView(R.id.surface_view)
    SurfaceView mSurfaceView;
    @BindView(R.id.tv_pause)
    TextView tvPause;

    private static final String TAG = "RecordVideoActivity";
    private List<String> videosPathList = new ArrayList<>();
    private MediaRecorder mediaRecorder;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private int mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK; //当前选用的摄像头，0后置 1前置 默认后置
    private boolean isPause = false;
    private String targetVideoPath; //视频合并后的文件目录

    private MediaRecorder.OnErrorListener recordErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mediaRecorder, int what, int extra) {
            try {
                if (mediaRecorder != null) {
                    mediaRecorder.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_record_video;
    }

    @Override
    public void initView() {
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void initData() {
    }

    public void start(View view) {
        startRecord();
    }

    private void startRecord() {
        //录制视频前必须先解锁Camera
        mCamera.unlock();
        String filePath = createFile();
        if (!TextUtils.isEmpty(filePath)) {
            videosPathList.add(filePath);
            configMediaRecorder();
            try {
                //开始录制
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                LogUtils.e(TAG, " error = " + e.getMessage());
                videosPathList.remove(videosPathList.size() - 1);
            }
        } else {
            ZToast.showToast(this, "创建文件失败");
        }
    }

    public void end(View view) {
        finishRecord();
    }

    public void pause(View view) {
        isPause = !isPause;
        if (isPause) {
            LogUtils.e(TAG, "暂停了");
            tvPause.setText("继续");
            if (mCamera != null) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success)
                            camera.cancelAutoFocus();
                    }
                });
            }
            stopRecord();
        } else {
            LogUtils.e(TAG, "开始了");
            tvPause.setText("暂停");
            startRecord();
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera(mCurrentCameraId);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
    }


    private String createFile() {
        String filePath = null;
        if (FileUtils.createOrExistsDir(ConstantUtil.RECORD_VIDEO)) {
            filePath = ConstantUtil.RECORD_VIDEO + File.separator + getVideoName();
        }
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!FileUtils.isFileExists(file) && FileUtils.createOrExistsFile(file)) {
            return filePath;
        } else {
            return null;
        }
    }

    private void finishRecord() {
        stopRecord();
        handleRecord();
    }

    private void stopRecord() {
        if (mediaRecorder != null) {
            // 设置后不会崩
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setPreviewDisplay(null);
            //停止录制
            mediaRecorder.stop();
            mediaRecorder.reset();
            //释放资源
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private String getVideoName() {
        return "video_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".mp4";
    }

    private void configMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setOnErrorListener(recordErrorListener);

        //使用SurfaceView预览
        mediaRecorder.setPreviewDisplay(mHolder.getSurface());

        //1.设置采集声音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置采集图像
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //2.设置视频，音频的输出格式 mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //3.设置音频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置图像的编码格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置立体声
//        mediaRecorder.setAudioChannels(2);
        //设置最大录像时间 单位：毫秒
//        mediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
//        mediaRecorder.setMaxFileSize(1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mediaRecorder.setAudioEncodingBitRate(44100);
        if (mProfile.videoBitRate > 3 * 1024 * 1024) {
            mediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
        } else {
            mediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
        }
        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);

        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        mediaRecorder.setOrientationHint(90);
        //设置录像的分辨率
        mediaRecorder.setVideoSize(1280, 720);
//        mediaRecorder.setCaptureRate(60);

        //设置录像视频输出地址
        mediaRecorder.setOutputFile(videosPathList.get(videosPathList.size() - 1));
    }

    private void openCamera(int cameraId) {
        if (mCamera == null) {
            mCamera = Camera.open(cameraId);
            setCameraDisplayOrientation(this, cameraId, mCamera); //设置预览的方向
            setCameraParameters(); //配置相机参数
            try {
                mCamera.setPreviewDisplay(mHolder); //通过SurfaceView显示取景画面
                mCamera.startPreview(); //开启预览
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e("camera exception --- " + e.getMessage());
            }
        }
    }

    /**
     * 当相机界面不可见时停止预览 释放资源
     */
    private void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 保证预览方向正确
     *
     * @param activity
     * @param cameraId 前置/后置摄像头的参数
     * @param camera
     */
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) { //前置摄像头
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {  //后置摄像头
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 设置Camera参数  必须在startPreview之前
     */
    private void setCameraParameters() {
        if (mCamera != null) {
            mParameters = mCamera.getParameters();
            //设置聚焦模式
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            //缩短Recording启动时间
            mParameters.setRecordingHint(true);
            mParameters.setPreviewSize(1280, 720);
            //影像稳定能力
            if (mParameters.isVideoStabilizationSupported())
                mParameters.setVideoStabilization(true);
            mCamera.setParameters(mParameters);
        }
    }

    /**
     * 合并视频(用于暂停继续的功能)
     */
    private void handleRecord() {
        if (videosPathList.size() > 1) {
            VideoUtil.mergeVideos(ConstantUtil.RECORD_VIDEO + File.separator + getVideoName(), videosPathList, new VideoUtil.VideoMergeListener() {
                @Override
                public void onMergeStart() {
                }

                @Override
                public void onMergeSuccess(String mergedVideoPath) {
                    targetVideoPath = mergedVideoPath;
                    for (String path : videosPathList) {
                        File file = new File(path);
                        Log.e(getClass().getSimpleName(), "delete video clip " + path + " " + file.delete());
                    }
                    videosPathList.clear();
                }

                @Override
                public void onMergeFailed(Exception e) {
                }
            });
        } else {
            targetVideoPath = videosPathList.get(0);
            videosPathList.clear();
        }
    }
}
