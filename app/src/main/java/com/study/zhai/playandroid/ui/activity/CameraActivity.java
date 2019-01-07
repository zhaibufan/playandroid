package com.study.zhai.playandroid.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.util.ConstantUtil;
import com.study.zhai.playandroid.util.FileUtils;
import com.study.zhai.playandroid.util.LogUtils;
import com.study.zhai.playandroid.util.StatusBarUtils;
import com.study.zhai.playandroid.widget.ResizeAbleSurfaceView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback{

    @BindView(R.id.surface_view)
    ResizeAbleSurfaceView mSurfaceView;

    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Camera.Parameters mParameters;
    private String filepath;

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

        // 该方法获取的屏幕宽高包括系统状态栏高度
        DisplayMetrics outMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        mSurfaceView.resize(widthPixel, heightPixel);
    }

    @Override
    public void initData() {

    }



    public void takePicture(View view) {
        mCamera.takePicture(null, null, jpeg);
    }

    //创建jpeg图片回调数据对象
    Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //自定义文件保存路径  以拍摄时间区分命名
                filepath = ConstantUtil.CUSTOM_CAMERA + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())  +  ".jpg";
                if (FileUtils.createOrExistsDir(ConstantUtil.CUSTOM_CAMERA)) {
                    File file = new File(filepath);
                    if (!FileUtils.isFileExists(file) && FileUtils.createOrExistsFile(file)) {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
                        bos.flush();// 刷新此缓冲区的输出流
                        bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
                        camera.stopPreview();//关闭预览 处理数据
                        camera.startPreview();//数据处理完后继续开始预览
                        bitmap.recycle();//回收bitmap空间
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogUtils.e(TAG, "erro = " + e.getMessage());
            }
        }
    };


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.e(TAG, "surfaceCreated");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtils.e(TAG, "surfaceChanged");
        if (mCamera == null) {
            mCamera = Camera.open();
            try {
                setCameraParameters();
                mCamera.startPreview(); //开始预览取景
                mCamera.setPreviewDisplay(mHolder); //通过SurfaceView显示取景画面
                setCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_FRONT, mCamera);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
//            /* 从列表中选取合适的分辨率 */
//            Camera.Size picSize = CameraUtils.getProperSize4Ratio(pictureSizeList, (float) mSurfaceView.getHeight() / mSurfaceView.getWidth());
//            mParameters.setPictureSize(picSize.width, picSize.height);
//            Log.e(TAG,"最终设置的picsize: picSize.width: " + picSize.width + " picSize.height: " + picSize.height);
//
//            List<Camera.Size> videoSiezes = mParameters.getSupportedVideoSizes();
//            int videoWidth = 0;
//            int videoHeight = 0;
//            if (videoSiezes != null && !videoSiezes.isEmpty()) {
////                Camera.Size videoSize = VideoUtil.getInstance().getPropVideoSize(videoSiezes,surfaceView.getWidth());
//                Camera.Size videoSize = CameraUtils.getMaxSize4Width(videoSiezes,mSurfaceView.getWidth());
//                Log.e(TAG, "获取到的：video_width===" + videoSize.width + " video_height=== " +  videoSize.height);
//                videoWidth = videoSize.width;
//                videoHeight = videoSize.height;
//            }
//            List<Camera.Size> previewSizes = mParameters.getSupportedPreviewSizes();
////            Camera.Size previewSize = VideoUtil.getInstance().getPropPreviewSize(mParameters.getSupportedPreviewSizes(), videoWidth);
//            Camera.Size previewSize = CameraUtils.getProperSize4Ratio(previewSizes,(float) videoWidth / videoHeight);
//            if (previewSize != null) {
//                mParameters.setPreviewSize(previewSize.width, previewSize.height);
//                Log.e(TAG, "最终设置的预览尺寸,previewSize.width: " + previewSize.width + " previewSize.height: " + previewSize.height);
//            }

            mParameters.setJpegQuality(100); // 设置照片质量
            if (mParameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                mParameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
            }
            mCamera.cancelAutoFocus();//自动对焦。
            mCamera.setParameters(mParameters);
        }
    }
}
