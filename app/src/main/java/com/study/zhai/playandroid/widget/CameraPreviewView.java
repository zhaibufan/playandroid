package com.study.zhai.playandroid.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.study.zhai.playandroid.MyApplication;
import com.study.zhai.playandroid.log.LogUtils;
import com.study.zhai.playandroid.utils.CameraUtils;
import com.study.zhai.playandroid.utils.FileUtils;
import com.study.zhai.playandroid.utils.ZToast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraPreviewView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreviewView";
    private Context mContext;
    private int mWidth = -1;
    private int mHeight = -1;

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Camera.Parameters mParameters;
    private int mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK; //当前选用的摄像头，0后置 1前置 默认后置
    private String mDirPath, mPictureName; //存储照片的文件夹和图片名称
    private CameraCallBack mCallBack;

    public interface CameraCallBack {
        void onSurfaceCreated();
        void onSurfaceChanged();
        void onSurfaceDestroyed();
    }

    public CameraPreviewView(Context context) {
        this(context, null);
    }

    public CameraPreviewView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CameraPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void setOnCameraCallBack(CameraCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidth == -1 || mHeight == -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(mWidth, mHeight);
        }
    }

    /**
     * 重新绘制相机预览的宽高 防止预览变形
     * 该方法在surfaceView初始化之后就调用
     *
     * @param width  surfaceView的宽  全屏就是屏幕的宽
     * @param height surfaceView的高  全屏就是屏幕的高
     */
    public void resize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        this.getHolder().setFixedSize(width, height);
        requestLayout();
        invalidate();
    }

    private void init() {
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * 拍照
     */
    public void takePicture(String dirPath, String pictureName) {
        this.mDirPath = dirPath;
        this.mPictureName = pictureName;
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    camera.takePicture(shutterCallback, pictureCallback, jpeg);
                }
            }
        });
    }

    /**
     * 按快门瞬间的回调
     */
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            LogUtils.e(TAG, "shutterCallback");
        }
    };

    /**
     * 处理图片数据的回调
     */
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LogUtils.e(TAG, "pictureCallback");
        }
    };

    /**
     * 创建jpg图片保存到本地
     */
    Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap bitmap;
            if (mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                bitmap = rotateBitmapByDegree(bmp, 90);
            } else {
                bitmap = rotateBitmapByDegree(bmp, -90);
            }
            File file = createSavePictureFile();
            if (file == null) {
                ZToast.showToast(MyApplication.getInstance(), "创建文件夹失败");
            } else {
                if (!FileUtils.isFileExists(file) && FileUtils.createOrExistsFile(file)) {
                    saveBitmapToLocal(camera, bitmap, file);
                } else {
                    ZToast.showToast(MyApplication.getInstance(), "创建图片文件失败");
                }
            }
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCallBack.onSurfaceCreated();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCallBack.onSurfaceChanged();
        openCamera(mCurrentCameraId);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCallBack.onSurfaceDestroyed();
        stopCamera();
    }

    private void openCamera(int cameraId) {
        if (mCamera == null) {
            mCamera = Camera.open(cameraId);
            setCameraDisplayOrientation((Activity) mContext, cameraId, mCamera); //设置预览的方向
            setCameraParameters(); //配置相机参数
            mCamera.startPreview(); //开启预览
            try {
                mCamera.setPreviewDisplay(mHolder); //通过SurfaceView显示取景画面
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e("camera exception --- " + e.getMessage());
            }
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
            // 设置预览的宽高
//            setCameraPreviewSize();
            // 设置照片质量
            mParameters.setJpegQuality(100);
            // 连续对焦模式
            if (mParameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                mParameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            //自动对焦
            mCamera.cancelAutoFocus();
            mCamera.setParameters(mParameters);
        }
    }

    /**
     * 将图片按照某个角度进行旋转 保证拍照后的照片方向正确
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            LogUtils.e("CameraActivity OutOfMemoryError");
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 创建保存图片的本地文件
     *
     * @return
     */
    private File createSavePictureFile() {
        if (!FileUtils.createOrExistsDir(mDirPath)) {
            return null;
        }
        return new File(mDirPath, mPictureName);
    }

    /**
     * 图片保存到本地
     *
     * @param camera
     * @param bitmap
     * @param file
     */
    private void saveBitmapToLocal(Camera camera, Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
            bos.flush();// 刷新此缓冲区的输出流
            bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
            camera.stopPreview();//关闭预览 处理数据
            camera.startPreview();//数据处理完后继续开始预览
            bitmap.recycle();//回收bitmap空间
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当相机界面不可见时停止预览 释放资源
     */
    private void stopCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 前后摄像头切换
     */
    public void switchFrontCamera() {
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    //重新打开
                    reStartCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    reStartCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                    mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                    break;
                }
            }
        }
    }

    /**
     * 切换摄像头后重新开启相机
     *
     * @param cameraId
     */
    private void reStartCamera(int cameraId) {
        stopCamera();
        openCamera(cameraId);
    }

    /**
     * 当页面销毁时销毁相机相关资源
     */
    public void destroyCamera() {
        stopCamera();
        mHolder = null;
    }

    /**
     * 设置相机预览的宽高 防止预览的画面变形
     *
     *      跟resize的功能一样 但resize的效果更好
     */
    private void setCameraPreviewSize() {
        List<Camera.Size> pictureSizeList = mParameters.getSupportedPictureSizes();
        /* 从列表中选取合适的分辨率 */
        Camera.Size picSize = CameraUtils.getProperSize4Ratio(pictureSizeList, (float) this.getHeight() / this.getWidth());
        mParameters.setPictureSize(picSize.width, picSize.height);
        Log.e(TAG, "最终设置的picsize: picSize.width: " + picSize.width + " picSize.height: " + picSize.height);

        List<Camera.Size> videoSiezes = mParameters.getSupportedVideoSizes();
        int videoWidth = 0;
        int videoHeight = 0;
        if (videoSiezes != null && !videoSiezes.isEmpty()) {
            Camera.Size videoSize = CameraUtils.getMaxSize4Width(videoSiezes, this.getWidth());
            Log.e(TAG, "获取到的：video_width===" + videoSize.width + " video_height=== " + videoSize.height);
            videoWidth = videoSize.width;
            videoHeight = videoSize.height;
        }
        List<Camera.Size> previewSizes = mParameters.getSupportedPreviewSizes();
        Camera.Size previewSize = CameraUtils.getProperSize4Ratio(previewSizes, (float) videoWidth / videoHeight);
        if (previewSize != null) {
            mParameters.setPreviewSize(previewSize.width, previewSize.height);
            Log.e(TAG, "最终设置的预览尺寸,previewSize.width: " + previewSize.width + " previewSize.height: " + previewSize.height);
        }
    }
}
