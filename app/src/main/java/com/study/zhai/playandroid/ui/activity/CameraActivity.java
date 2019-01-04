package com.study.zhai.playandroid.ui.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.util.CameraUtils;
import com.study.zhai.playandroid.util.LogUtils;
import com.study.zhai.playandroid.util.StatusBarUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback{

    @BindView(R.id.surface_view)
    SurfaceView mSurfaceView;

    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Camera.Parameters mParameters;

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public void initView() {
        StatusBarUtils.fullScreen(this);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void initData() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.e(TAG, "surfaceCreated");
        if (mCamera == null) {
            mCamera = Camera.open();
            try {
                mCamera.startPreview(); //开始预览取景
                mCamera.setPreviewDisplay(mHolder); //通过SurfaceView显示取景画面
                setCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_FRONT, mCamera);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtils.e(TAG, "surfaceChanged");
        setCameraParameters();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.e(TAG, "surfaceDestroyed");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mHolder = null;
            mSurfaceView = null;
        }
    }

    /**
     * 保证预览方向正确
     *
     * @param activity
     * @param cameraId  前置/后置摄像头的参数
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
     * 设置Camera参数
     */
    private void setCameraParameters() {
        if (mCamera != null) {
            mParameters = mCamera.getParameters();
//            List<Camera.Size> pictureSizeList = mParameters.getSupportedPictureSizes();
////            /* 从列表中选取合适的分辨率 */
////            Camera.Size picSize = CameraUtils.getProperSize4Ratio(pictureSizeList, (float) mSurfaceView.getHeight() / mSurfaceView.getWidth());
////            mParameters.setPictureSize(picSize.width, picSize.height);
////            Log.e(TAG,"最终设置的picsize: picSize.width: " + picSize.width + " picSize.height: " + picSize.height);
////
////            List<Camera.Size> videoSiezes = mParameters.getSupportedVideoSizes();
////            int videoWidth = 0;
////            int videoHeight = 0;
////            if (videoSiezes != null && !videoSiezes.isEmpty()) {
//////                Camera.Size videoSize = VideoUtil.getInstance().getPropVideoSize(videoSiezes,surfaceView.getWidth());
////                Camera.Size videoSize = CameraUtils.getMaxSize4Width(videoSiezes,mSurfaceView.getWidth());
////                Log.e(TAG, "获取到的：video_width===" + videoSize.width + " video_height=== " +  videoSize.height);
////                videoWidth = videoSize.width;
////                videoHeight = videoSize.height;
////            }
////            List<Camera.Size> previewSizes = mParameters.getSupportedPreviewSizes();
//////            Camera.Size previewSize = VideoUtil.getInstance().getPropPreviewSize(mParameters.getSupportedPreviewSizes(), videoWidth);
////            Camera.Size previewSize = CameraUtils.getProperSize4Ratio(previewSizes,(float) videoWidth / videoHeight);
////            if (previewSize != null) {
////                mParameters.setPreviewSize(previewSize.width, previewSize.height);
////                Log.e(TAG, "最终设置的预览尺寸,previewSize.width: " + previewSize.width + " previewSize.height: " + previewSize.height);
////            }


            List<String> focusModes = mParameters.getSupportedFocusModes();
            if (focusModes != null && focusModes.size() > 0) {
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);  //设置自动对焦
                }
            }
            mCamera.setParameters(mParameters);
        }
    }
}
